# Jenkins Pipeline Setup Guide for Assistant Financier

This guide provides complete steps to set up Jenkins CI/CD pipeline with Selenium tests for the Assistant Financier project.

## Table of Contents
1. [Prerequisites](#prerequisites)
2. [Jenkins Installation](#jenkins-installation)
3. [Jenkins Configuration](#jenkins-configuration)
4. [Pipeline Setup](#pipeline-setup)
5. [Running the Pipeline](#running-the-pipeline)
6. [Troubleshooting](#troubleshooting)

---

## Prerequisites

### Required Software
- **Java 17** - Required for Jenkins and the application
- **Maven 3.9+** - For building the project
- **Docker Desktop** - For containerization
- **Git** - For source control
- **Chrome/Firefox** - For Selenium tests

### System Requirements
- Minimum 4GB RAM (8GB recommended)
- 20GB free disk space
- Windows 10/11, Linux, or macOS

---

## Jenkins Installation

### Option 1: Install Jenkins as Windows Service

1. **Download Jenkins**
   ```powershell
   # Download Jenkins LTS Windows installer
   # Visit: https://www.jenkins.io/download/
   # Download the Windows installer (.msi file)
   ```

2. **Install Jenkins**
   - Run the installer
   - Choose installation directory (e.g., `C:\Program Files\Jenkins`)
   - Jenkins will run on port 8080 by default
   - Service will start automatically

3. **Access Jenkins**
   - Open browser: http://localhost:8080
   - Get initial admin password:
   ```powershell
   Get-Content "C:\Program Files\Jenkins\secrets\initialAdminPassword"
   ```

### Option 2: Run Jenkins in Docker

```powershell
# Create Jenkins home directory
mkdir C:\jenkins_home

# Run Jenkins container
docker run -d `
  --name jenkins `
  -p 8080:8080 -p 50000:50000 `
  -v C:\jenkins_home:/var/jenkins_home `
  -v /var/run/docker.sock:/var/run/docker.sock `
  jenkins/jenkins:lts-jdk17
```

### Option 3: Run Jenkins with Docker Compose

Create `jenkins/docker-compose.yml`:
```yaml
version: '3.8'
services:
  jenkins:
    image: jenkins/jenkins:lts-jdk17
    privileged: true
    user: root
    ports:
      - "8080:8080"
      - "50000:50000"
    volumes:
      - ./jenkins_home:/var/jenkins_home
      - /var/run/docker.sock:/var/run/docker.sock
    environment:
      - JAVA_OPTS=-Djenkins.install.runSetupWizard=false
```

Run:
```powershell
cd jenkins
docker compose up -d
```

---

## Jenkins Configuration

### Initial Setup

1. **Unlock Jenkins**
   - Use the initial admin password from installation
   - Install suggested plugins

2. **Create Admin User**
   - Username: admin
   - Password: (choose a strong password)
   - Full name: Your Name
   - Email: your@email.com

### Install Required Plugins

Go to: **Manage Jenkins** → **Manage Plugins** → **Available**

Install these plugins:
- **Pipeline** (should be installed by default)
- **Git plugin**
- **Docker Pipeline**
- **HTML Publisher** (for test reports)
- **JUnit Plugin** (for test results)
- **TestNG Results Plugin**
- **Email Extension Plugin** (optional)
- **Slack Notification Plugin** (optional)

Click **Download now and install after restart**

### Configure Global Tools

Go to: **Manage Jenkins** → **Global Tool Configuration**

#### 1. JDK Configuration
- Click **Add JDK**
- Name: `JDK-17`
- Uncheck "Install automatically"
- JAVA_HOME: `C:\Program Files\Java\jdk-17` (adjust to your Java installation path)

Or install automatically:
- Check "Install automatically"
- Select version: Java 17

#### 2. Maven Configuration
- Click **Add Maven**
- Name: `Maven-3.9.0`
- Check "Install automatically"
- Version: Select 3.9.0 or later

Or manual configuration:
- Uncheck "Install automatically"
- MAVEN_HOME: `C:\Program Files\Maven\apache-maven-3.9.0`

#### 3. Git Configuration
- Usually auto-detected
- If needed, set path: `C:\Program Files\Git\bin\git.exe`

### Configure System Settings

Go to: **Manage Jenkins** → **Configure System**

1. **Jenkins Location**
   - Jenkins URL: http://localhost:8080/

2. **Email Notification** (Optional)
   - SMTP server: smtp.gmail.com
   - Use SSL: Yes
   - Port: 465
   - Add credentials for email account

---

## Pipeline Setup

### Method 1: Pipeline from SCM (Recommended)

1. **Create New Pipeline Job**
   - Click **New Item**
   - Enter name: `Assistant-Financier-Pipeline`
   - Select **Pipeline**
   - Click **OK**

2. **Configure Pipeline**
   - Description: `CI/CD Pipeline for Assistant Financier with Selenium Tests`
   
3. **Build Triggers** (Optional)
   - Check **Poll SCM** for automatic builds
   - Schedule: `H/5 * * * *` (check every 5 minutes)
   - Or use **GitHub webhook** for instant triggers

4. **Pipeline Definition**
   - Definition: **Pipeline script from SCM**
   - SCM: **Git**
   - Repository URL: `https://github.com/your-username/assistant-vocal.git`
   - Credentials: Add your GitHub credentials
   - Branch: `*/main` or `*/master`
   - Script Path: `Jenkinsfile`

5. **Save**

### Method 2: Direct Pipeline Script

If not using Git:
- Definition: **Pipeline script**
- Copy the content from `Jenkinsfile` into the script box
- Save

---

## Jenkinsfile Explanation

The pipeline has these stages:

### 1. Checkout
```groovy
stage('Checkout') {
    steps {
        checkout scm
    }
}
```
- Checks out code from Git repository

### 2. Build Backend
```groovy
stage('Build Backend') {
    steps {
        dir('backend') {
            bat 'mvn clean package -DskipTests'
        }
    }
}
```
- Compiles Java Spring Boot application
- Creates JAR file
- Skips tests (will run separately)

### 3. Start Docker Services
```groovy
stage('Start Docker Services') {
    steps {
        dir('docker') {
            bat 'docker compose down -v'
            bat 'docker compose up -d --build'
        }
        sleep time: 30, unit: 'SECONDS'
    }
}
```
- Stops any existing containers
- Builds and starts all services (postgres, backend, frontend-web)
- Waits for services to be ready

### 4. Run Backend Unit Tests
```groovy
stage('Run Backend Unit Tests') {
    steps {
        dir('backend') {
            bat 'mvn test'
        }
    }
}
```
- Runs JUnit tests
- Publishes test results

### 5. Run Selenium Tests
```groovy
stage('Run Selenium Tests') {
    steps {
        dir('selenium-tests') {
            bat """
                mvn clean test ^
                -Dbase.url=${BASE_URL} ^
                -Dbrowser=${BROWSER} ^
                -Dheadless=${HEADLESS}
            """
        }
    }
}
```
- Runs Selenium UI tests
- Generates HTML reports
- Archives screenshots

### 6. Cleanup
```groovy
post {
    always {
        dir('docker') {
            bat 'docker compose down -v'
        }
        cleanWs()
    }
}
```
- Stops Docker containers
- Cleans workspace

---

## Running the Pipeline

### Manual Build

1. **Navigate to Pipeline**
   - Go to Jenkins Dashboard
   - Click on `Assistant-Financier-Pipeline`

2. **Start Build**
   - Click **Build Now**
   - Build will appear in **Build History**

3. **Monitor Progress**
   - Click on build number (e.g., #1)
   - Click **Console Output** to see logs
   - Or view **Pipeline Stage View** for visual progress

### Automatic Builds

**Option 1: Poll SCM**
- Configured in pipeline settings
- Jenkins checks Git for changes periodically

**Option 2: GitHub Webhook**
1. Go to GitHub repository → Settings → Webhooks
2. Add webhook:
   - Payload URL: `http://your-jenkins-url:8080/github-webhook/`
   - Content type: `application/json`
   - Events: Just the push event
3. Save webhook

**Option 3: Scheduled Builds**
- Add to pipeline configuration
- Build Triggers → Build periodically
- Schedule: `H 2 * * *` (daily at 2 AM)

---

## Viewing Test Results

### 1. TestNG/JUnit Results
- Click on build number
- Click **Test Result**
- See passed/failed tests
- Drill down for details

### 2. HTML Reports
- Click on build number
- Click **Selenium Test Report** (in left menu)
- View detailed HTML report

### 3. Screenshots
- Click on build number
- Click **Build Artifacts**
- Download screenshots of failures

### 4. Docker Logs
- Click on build number
- Click **Build Artifacts**
- View `docker-logs.txt`

---

## Pipeline Parameters (Advanced)

Add parameters to make pipeline configurable:

```groovy
pipeline {
    parameters {
        choice(name: 'BROWSER', choices: ['chrome', 'firefox'], description: 'Browser for tests')
        booleanParam(name: 'HEADLESS', defaultValue: true, description: 'Run in headless mode')
        string(name: 'BASE_URL', defaultValue: 'http://localhost:3000', description: 'Application URL')
    }
    // ... rest of pipeline
}
```

Users can then select options before building.

---

## Troubleshooting

### Issue: Maven not found
**Solution:**
- Verify Maven is configured in Global Tool Configuration
- Check MAVEN_HOME environment variable
- Ensure Maven bin directory is in PATH

### Issue: Docker commands fail
**Solution:**
```powershell
# Ensure Docker is running
docker ps

# Check Docker service
Get-Service docker

# Start Docker if needed
Start-Service docker
```

### Issue: Port conflicts
**Solution:**
```powershell
# Check what's using port 8080
netstat -ano | findstr :8080

# Kill process if needed
taskkill /PID <process_id> /F
```

### Issue: Selenium tests fail
**Solution:**
- Check if application is accessible: http://localhost:3000
- Verify Docker services are running: `docker ps`
- Check Docker logs: `docker logs <container_name>`
- Ensure headless mode is enabled in Jenkins
- Check Chrome/ChromeDriver compatibility

### Issue: Java version mismatch
**Solution:**
```powershell
# Check Java version
java -version

# Should be Java 17
# Set JAVA_HOME in Jenkins if needed
```

### Issue: Permission denied errors
**Solution:**
- Run Jenkins as Administrator (Windows)
- Check file permissions in workspace
- Ensure Jenkins user has Docker permissions

---

## Best Practices

1. **Version Control**
   - Always commit Jenkinsfile to repository
   - Use branches for testing pipeline changes

2. **Notifications**
   - Set up email notifications for failures
   - Use Slack for team notifications

3. **Artifact Management**
   - Archive important artifacts (JAR files, test reports)
   - Set artifact retention policy

4. **Security**
   - Use credentials plugin for sensitive data
   - Don't hardcode passwords in Jenkinsfile
   - Enable CSRF protection

5. **Performance**
   - Use parallel stages when possible
   - Clean workspace regularly
   - Limit build history retention

6. **Monitoring**
   - Monitor Jenkins disk space
   - Check build trends
   - Review failed builds regularly

---

## Next Steps

1. **Add More Tests**
   - Create more Selenium test scenarios
   - Add API integration tests
   - Add performance tests

2. **Enhance Pipeline**
   - Add code quality gates (SonarQube)
   - Add security scanning (OWASP)
   - Add deployment stages

3. **Set Up Environments**
   - Development environment
   - Staging environment
   - Production deployment

4. **Improve Reporting**
   - Integrate Allure reports
   - Add test coverage reports
   - Create custom dashboards

---

## Support

For issues or questions:
1. Check Jenkins logs: `/var/log/jenkins/jenkins.log` (Linux) or Windows Event Viewer
2. Review console output of failed builds
3. Check official documentation: https://www.jenkins.io/doc/

---

## Summary

You now have:
✅ Complete Jenkins setup
✅ Automated CI/CD pipeline
✅ Selenium test integration
✅ Automated test reporting
✅ Docker integration
✅ Build notifications

Your pipeline will automatically:
- Build the backend
- Start Docker services
- Run unit tests
- Run Selenium UI tests
- Generate reports
- Clean up resources
