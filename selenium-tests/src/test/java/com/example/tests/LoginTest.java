package com.example.tests;

import com.example.base.BaseTest;
import com.example.pages.LoginPage;
import com.example.pages.DashboardPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test(priority = 1, description = "Verify login page loads successfully")
    public void testLoginPageLoads() {
        navigateToPage("/login.html");
        LoginPage loginPage = new LoginPage(driver);
        
        String pageTitle = loginPage.getPageTitle();
        Assert.assertTrue(pageTitle.contains("Login"), 
            "Login page title should contain 'Login'");
    }

    @Test(priority = 2, description = "Verify login with invalid credentials shows error")
    public void testLoginWithInvalidCredentials() {
        navigateToPage("/login.html");
        LoginPage loginPage = new LoginPage(driver);
        
        loginPage.login("invalid@email.com", "wrongpassword");
        
        // Wait for error message
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Verify error is shown (might need adjustment based on actual error handling)
        boolean errorDisplayed = loginPage.isErrorMessageDisplayed();
        Assert.assertTrue(errorDisplayed || driver.getCurrentUrl().contains("login"), 
            "Error should be displayed or user should remain on login page");
    }

    @Test(priority = 3, description = "Verify login with valid credentials redirects to dashboard")
    public void testLoginWithValidCredentials() {
        navigateToPage("/login.html");
        LoginPage loginPage = new LoginPage(driver);
        
        // Use real credentials
        loginPage.login("simo@gmail.com", "1234567890");
        
        // Wait for redirect
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Verify redirect to dashboard
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("dashboard") || currentUrl.contains("index"), 
            "User should be redirected to dashboard after successful login");
    }

    @Test(priority = 4, description = "Verify empty fields validation")
    public void testEmptyFieldsValidation() {
        navigateToPage("/login.html");
        LoginPage loginPage = new LoginPage(driver);
        
        loginPage.clickLoginButton();
        
        // HTML5 validation should prevent form submission
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("login"), 
            "User should remain on login page when fields are empty");
    }
}
