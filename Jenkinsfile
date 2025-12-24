pipeline {
    agent any
    
    environment {
        // Docker configuration
        DOCKER_COMPOSE_FILE = 'docker/docker-compose.yml'
        
        // Test configuration
        BASE_URL = 'http://localhost:3000'
        BROWSER = 'chrome'
        HEADLESS = 'true'
        
        // Maven configuration
        MAVEN_OPTS = '-Dmaven.repo.local=.m2/repository'
    }
    
    tools {
        maven 'maven' // Using Jenkins default Maven installation
        // JDK will use system default
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out source code...'
                checkout scm
            }
        }
        
        stage('Build Backend') {
            steps {
                echo 'Building Spring Boot backend...'
                dir('backend') {
                    bat 'mvn clean package -DskipTests'
                }
            }
        }
        
        stage('Start Docker Services') {
            steps {
                echo 'Starting Docker containers...'
                dir('docker') {
                    bat 'docker compose down -v'
                    bat 'docker compose up -d --build'
                }
                
                echo 'Waiting for services to be ready...'
                sleep time: 30, unit: 'SECONDS'
                
                // Health check
                script {
                    retry(5) {
                        sleep time: 10, unit: 'SECONDS'
                        bat 'curl -f http://localhost:8081/actuator/health || exit 0'
                    }
                }
            }
        }
        
        stage('Run Backend Unit Tests') {
            steps {
                echo 'Running backend unit tests...'
                dir('backend') {
                    bat 'mvn test'
                }
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }
        
        stage('Run Selenium Tests') {
            steps {
                echo 'Running Selenium UI tests...'
                dir('selenium-tests') {
                    bat """
                        mvn clean test ^
                        -Dbase.url=${BASE_URL} ^
                        -Dbrowser=${BROWSER} ^
                        -Dheadless=${HEADLESS}
                    """
                }
            }
            post {
                always {
                    // Publish TestNG results
                    publishHTML([
                        allowMissing: false,
                        alwaysLinkToLastBuild: true,
                        keepAll: true,
                        reportDir: 'selenium-tests/target/surefire-reports',
                        reportFiles: 'index.html',
                        reportName: 'Selenium Test Report',
                        reportTitles: 'Selenium Tests'
                    ])
                    
                    // Archive test results
                    junit 'selenium-tests/target/surefire-reports/*.xml'
                    
                    // Archive screenshots if any
                    archiveArtifacts artifacts: 'selenium-tests/screenshots/**/*.png', allowEmptyArchive: true
                }
            }
        }
        
        stage('Code Quality Analysis') {
            steps {
                echo 'Running code quality checks...'
                dir('backend') {
                    // Optional: SonarQube analysis
                    // bat 'mvn sonar:sonar'
                    echo 'Code quality analysis placeholder'
                }
            }
        }
        
        stage('Security Scan') {
            steps {
                echo 'Running security scans...'
                // Optional: OWASP Dependency Check
                // dir('backend') {
                //     bat 'mvn org.owasp:dependency-check-maven:check'
                // }
                echo 'Security scan placeholder'
            }
        }
    }
    
    post {
        always {
            echo 'Cleaning up...'
            dir('docker') {
                bat 'docker compose logs > docker-logs.txt'
                archiveArtifacts artifacts: 'docker-logs.txt', allowEmptyArchive: true
                bat 'docker compose down -v'
            }
            
            // Clean workspace
            cleanWs()
        }
        
        success {
            echo 'Pipeline completed successfully!'
            // Optional: Send success notification
            // emailext subject: "Pipeline Success: ${env.JOB_NAME} - ${env.BUILD_NUMBER}",
            //          body: "The pipeline has completed successfully.",
            //          to: "team@example.com"
        }
        
        failure {
            echo 'Pipeline failed!'
            // Optional: Send failure notification
            // emailext subject: "Pipeline Failed: ${env.JOB_NAME} - ${env.BUILD_NUMBER}",
            //          body: "The pipeline has failed. Please check the logs.",
            //          to: "team@example.com"
        }
    }
}
