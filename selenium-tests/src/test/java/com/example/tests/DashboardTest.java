package com.example.tests;

import com.example.base.BaseTest;
import com.example.pages.LoginPage;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class DashboardTest extends BaseTest {

    @BeforeMethod
    public void login() {
        // Login before each test
        navigateToPage("/login.html");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("simo@gmail.com", "1234567890");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test(priority = 1, description = "Verify dashboard page accessibility")
    public void testDashboardPageLoads() {
        navigateToPage("/dashboard.html");
        
        String pageTitle = driver.getTitle();
        Assert.assertTrue(pageTitle.contains("Dashboard") || pageTitle.contains("Tableau"), 
            "Dashboard page should load");
    }

    @Test(priority = 2, description = "Verify main page loads")
    public void testMainPageLoads() {
        navigateToPage("/index.html");
        
        String pageTitle = driver.getTitle();
        Assert.assertTrue(pageTitle.contains("Assistant"), 
            "Main page should contain 'Assistant' in title");
        
        // Check for key elements
        boolean conversationAreaPresent = driver.findElements(By.id("conversationArea")).size() > 0;
        Assert.assertTrue(conversationAreaPresent, "Conversation area should be present");
    }

    @Test(priority = 3, description = "Verify quick actions are present on main page")
    public void testQuickActionsPresent() {
        navigateToPage("/index.html");
        
        // Wait for page to load
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        int quickActionButtons = driver.findElements(By.className("action-btn")).size();
        Assert.assertTrue(quickActionButtons > 0, 
            "Quick action buttons should be present");
    }

    @Test(priority = 4, description = "Verify language selector is present")
    public void testLanguageSelectorPresent() {
        navigateToPage("/index.html");
        
        boolean languageSelectPresent = driver.findElements(By.id("languageSelect")).size() > 0;
        Assert.assertTrue(languageSelectPresent, 
            "Language selector should be present");
    }
}
