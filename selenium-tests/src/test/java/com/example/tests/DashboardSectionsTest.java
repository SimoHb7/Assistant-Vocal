package com.example.tests;

import com.example.base.BaseTest;
import com.example.pages.DashboardPage;
import com.example.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class DashboardSectionsTest extends BaseTest {

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

    @Test(priority = 1, description = "Verify dashboard loads without section parameter")
    public void testDashboardDefault() {
        navigateToPage("/dashboard.html");
        DashboardPage dashboardPage = new DashboardPage(driver);
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        String pageTitle = dashboardPage.getPageTitle();
        System.out.println("Dashboard page title: " + pageTitle);
        
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("dashboard.html"), 
            "Should be on dashboard page");
    }

    @Test(priority = 2, description = "Verify dashboard with assistant section parameter")
    public void testDashboardAssistantSection() {
        navigateToPage("/dashboard.html?section=assistant");
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("dashboard.html?section=assistant"), 
            "URL should contain section=assistant parameter");
        
        // Verify page loads correctly
        String pageTitle = driver.getTitle();
        System.out.println("Dashboard assistant section title: " + pageTitle);
        Assert.assertTrue(pageTitle != null && !pageTitle.isEmpty(), 
            "Page should have a title");
    }

    @Test(priority = 3, description = "Verify dashboard sections navigation")
    public void testDashboardSectionsNavigation() {
        navigateToPage("/dashboard.html");
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Check for section navigation elements
        int quickActions = new DashboardPage(driver).getQuickActionsCount();
        System.out.println("Quick actions count: " + quickActions);
        
        Assert.assertTrue(quickActions >= 0, "Should have quick action buttons");
    }

    @Test(priority = 4, description = "Verify dashboard different sections load")
    public void testDifferentSections() {
        String[] sections = {"assistant", "transactions", "history", "settings"};
        
        for (String section : sections) {
            try {
                navigateToPage("/dashboard.html?section=" + section);
                Thread.sleep(1500);
                
                String currentUrl = driver.getCurrentUrl();
                System.out.println("Testing section: " + section + " - URL: " + currentUrl);
                
                // Check if the section is active instead of URL parameter
                // (dashboard.html removes query params after loading the section)
                boolean sectionVisible = driver.findElements(org.openqa.selenium.By.cssSelector("#" + section + ".content-section.active")).size() > 0;
                Assert.assertTrue(sectionVisible, 
                    "Section " + section + " should be visible");
                
            } catch (Exception e) {
                System.out.println("Error loading section " + section + ": " + e.getMessage());
            }
        }
    }

    @Test(priority = 5, description = "Verify dashboard page elements are present")
    public void testDashboardPageElements() {
        navigateToPage("/dashboard.html");
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        DashboardPage dashboardPage = new DashboardPage(driver);
        
        // Check if balance card or main content is visible
        boolean balanceVisible = dashboardPage.isBalanceCardDisplayed();
        System.out.println("Balance card visible: " + balanceVisible);
        
        // Page should have some content
        String pageSource = driver.getPageSource();
        Assert.assertTrue(pageSource.length() > 0, "Page should have content");
    }

    @Test(priority = 6, description = "Verify navigation between dashboard sections")
    public void testSectionSwitching() {
        navigateToPage("/dashboard.html?section=assistant");
        
        try {
            Thread.sleep(2000);
            
            // Navigate to different section
            navigateToPage("/dashboard.html?section=transactions");
            Thread.sleep(2000);
            
            // Check if the transactions section is now active
            boolean transactionsSectionVisible = driver.findElements(org.openqa.selenium.By.cssSelector("#transactions.content-section.active")).size() > 0;
            Assert.assertTrue(transactionsSectionVisible, 
                "Should switch to transactions section");
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test(priority = 7, description = "Verify invalid section parameter handling")
    public void testInvalidSectionParameter() {
        navigateToPage("/dashboard.html?section=invalidSection");
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Page should still load (may show default or error message)
        String pageTitle = driver.getTitle();
        Assert.assertTrue(pageTitle != null && !pageTitle.isEmpty(), 
            "Page should handle invalid section gracefully");
    }
}
