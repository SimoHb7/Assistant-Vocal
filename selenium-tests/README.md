# Selenium Test Suite for Assistant Financier

This directory contains automated UI tests using Selenium WebDriver for the Assistant Financier application.

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- Chrome or Firefox browser installed
- Application running on http://localhost:3000

## Project Structure

```
selenium-tests/
├── src/
│   └── test/
│       ├── java/
│       │   └── com/
│       │       └── example/
│       │           ├── base/
│       │           │   └── BaseTest.java
│       │           ├── pages/
│       │           │   ├── LoginPage.java
│       │           │   └── DashboardPage.java
│       │           └── tests/
│       │               ├── LoginTest.java
│       │               ├── RegistrationTest.java
│       │               └── DashboardTest.java
│       └── resources/
│           └── testng.xml
├── pom.xml
└── README.md
```

## Running Tests Locally

### Run all tests
```bash
mvn clean test
```

### Run with specific browser
```bash
# Chrome (default)
mvn clean test -Dbrowser=chrome

# Firefox
mvn clean test -Dbrowser=firefox
```

### Run in headless mode
```bash
mvn clean test -Dheadless=true
```

### Run with custom base URL
```bash
mvn clean test -Dbase.url=http://localhost:3000
```

### Run specific test class
```bash
mvn clean test -Dtest=LoginTest
```

### Run with all parameters
```bash
mvn clean test -Dbase.url=http://localhost:3000 -Dbrowser=chrome -Dheadless=true
```

## Test Coverage

### LoginTest
- Verify login page loads successfully
- Test login with invalid credentials
- Test login with valid credentials
- Test empty fields validation

### RegistrationTest
- Verify registration page loads
- Verify form elements are present
- Test navigation between login and registration

### DashboardTest
- Verify dashboard accessibility
- Verify main page elements
- Test quick actions presence
- Test language selector

## CI/CD Integration

These tests are integrated into the Jenkins pipeline and run automatically on every build.

## Test Reports

After running tests, reports are generated in:
- `target/surefire-reports/` - XML and HTML reports
- TestNG generates detailed HTML reports

## Configuration

Key configurations in `pom.xml`:
- Selenium version: 4.16.1
- TestNG version: 7.9.0
- WebDriverManager: 5.6.3 (automatic driver management)

## Troubleshooting

### Tests failing to start browser
- Ensure Chrome/Firefox is installed
- WebDriverManager should automatically download drivers
- Check if headless mode works: `-Dheadless=true`

### Connection refused errors
- Verify the application is running on the specified base URL
- Check if all Docker services are up: `docker compose ps`

### Timeout errors
- Increase timeout in BaseTest.java
- Check network connectivity
- Verify application is fully loaded

## Adding New Tests

1. Create a new Page Object in `src/test/java/com/example/pages/`
2. Create test class in `src/test/java/com/example/tests/`
3. Extend `BaseTest` class
4. Add test class to `testng.xml`

## Best Practices

- Use Page Object Model (POM) pattern
- Keep tests independent and isolated
- Use meaningful test names and descriptions
- Add proper waits (avoid Thread.sleep in production tests)
- Clean up resources in @AfterMethod
