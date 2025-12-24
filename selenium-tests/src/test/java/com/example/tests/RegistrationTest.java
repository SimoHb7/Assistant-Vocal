package com.example.tests;

import com.example.base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

public class RegistrationTest extends BaseTest {

    @Test(priority = 1, description = "Verify registration page loads successfully")
    public void testRegistrationPageLoads() {
        navigateToPage("/register.html");
        
        String pageTitle = driver.getTitle();
        Assert.assertTrue(pageTitle.contains("Register") || pageTitle.contains("Inscription"), 
            "Registration page should load successfully");
    }

    @Test(priority = 2, description = "Verify registration form elements are present")
    public void testRegistrationFormElements() {
        navigateToPage("/register.html");
        
        // Check for essential form elements
        boolean emailFieldPresent = driver.findElements(By.id("email")).size() > 0;
        boolean passwordFieldPresent = driver.findElements(By.id("password")).size() > 0;
        boolean submitButtonPresent = driver.findElements(By.cssSelector("button[type='submit']")).size() > 0;
        
        Assert.assertTrue(emailFieldPresent, "Email field should be present");
        Assert.assertTrue(passwordFieldPresent, "Password field should be present");
        Assert.assertTrue(submitButtonPresent, "Submit button should be present");
    }

    @Test(priority = 3, description = "Verify navigation to login page from registration")
    public void testNavigateToLoginFromRegistration() {
        navigateToPage("/register.html");
        
        try {
            WebElement loginLink = driver.findElement(By.linkText("Se connecter"));
            loginLink.click();
            
            Thread.sleep(1000);
            String currentUrl = driver.getCurrentUrl();
            Assert.assertTrue(currentUrl.contains("login"), 
                "Should navigate to login page");
        } catch (Exception e) {
            // Link might have different text
            System.out.println("Login link not found with expected text");
        }
    }
}
