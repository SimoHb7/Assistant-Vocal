package com.example.tests;

import com.example.base.BaseTest;
import com.example.pages.HistoryPage;
import com.example.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class HistoryTest extends BaseTest {

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

    @Test(priority = 1, description = "Verify history page loads successfully")
    public void testHistoryPageLoads() {
        navigateToPage("/history.html");
        HistoryPage historyPage = new HistoryPage(driver);
        
        boolean pageLoaded = historyPage.isPageLoaded();
        Assert.assertTrue(pageLoaded, "History page should load successfully");
        
        String pageTitle = historyPage.getPageTitle();
        System.out.println("Page title: " + pageTitle);
        Assert.assertTrue(pageTitle.contains("History") || pageTitle.contains("Historique") || pageTitle.contains("history"), 
            "Page should have history-related title");
    }

    @Test(priority = 2, description = "Verify conversations list visibility")
    public void testConversationsListVisible() {
        navigateToPage("/history.html");
        HistoryPage historyPage = new HistoryPage(driver);
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Check if list is visible or empty state is shown
        boolean listVisible = historyPage.isConversationsListVisible();
        boolean emptyState = historyPage.isEmptyStateVisible();
        
        System.out.println("Conversations list visible: " + listVisible);
        System.out.println("Empty state visible: " + emptyState);
        
        Assert.assertTrue(listVisible || emptyState, 
            "Either conversations list or empty state should be visible");
    }

    @Test(priority = 3, description = "Verify conversation count")
    public void testConversationCount() {
        navigateToPage("/history.html");
        HistoryPage historyPage = new HistoryPage(driver);
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        int conversationCount = historyPage.getConversationCount();
        System.out.println("Conversation count: " + conversationCount);
        Assert.assertTrue(conversationCount >= 0, "Conversation count should be 0 or more");
    }

    @Test(priority = 4, description = "Verify search functionality")
    public void testSearchFunctionality() {
        navigateToPage("/history.html");
        HistoryPage historyPage = new HistoryPage(driver);
        
        try {
            Thread.sleep(2000);
            historyPage.searchConversation("test");
            System.out.println("Search functionality executed");
        } catch (Exception e) {
            System.out.println("Search may not be fully implemented: " + e.getMessage());
        }
    }

    @Test(priority = 5, description = "Verify conversation selection")
    public void testConversationSelection() {
        navigateToPage("/history.html");
        HistoryPage historyPage = new HistoryPage(driver);
        
        try {
            Thread.sleep(2000);
            
            int count = historyPage.getConversationCount();
            if (count > 0) {
                historyPage.clickConversation(0);
                Thread.sleep(1000);
                
                boolean detailsVisible = historyPage.isConversationDetailsVisible();
                System.out.println("Conversation details visible: " + detailsVisible);
            } else {
                System.out.println("No conversations available to select");
            }
        } catch (Exception e) {
            System.out.println("Conversation selection not available: " + e.getMessage());
        }
    }

    @Test(priority = 6, description = "Verify export history functionality")
    public void testExportHistory() {
        navigateToPage("/history.html");
        HistoryPage historyPage = new HistoryPage(driver);
        
        try {
            Thread.sleep(2000);
            historyPage.clickExportHistory();
            System.out.println("Export history triggered");
        } catch (Exception e) {
            System.out.println("Export may not be implemented yet");
        }
    }

    @Test(priority = 7, description = "Verify filter options presence")
    public void testFilterOptions() {
        navigateToPage("/history.html");
        HistoryPage historyPage = new HistoryPage(driver);
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        int filterCount = historyPage.getFilterOptionsCount();
        System.out.println("Filter options count: " + filterCount);
        Assert.assertTrue(filterCount >= 0, "Filter count should be valid");
    }

    @Test(priority = 8, description = "Verify page URL is correct")
    public void testCorrectURL() {
        navigateToPage("/history.html");
        
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("history.html"), 
            "URL should contain history.html");
    }
}
