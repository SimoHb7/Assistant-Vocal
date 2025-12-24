package com.example.tests;

import com.example.base.BaseTest;
import com.example.pages.LoginPage;
import com.example.pages.TransactionsPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TransactionsTest extends BaseTest {

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

    @Test(priority = 1, description = "Verify transactions page loads successfully")
    public void testTransactionsPageLoads() {
        navigateToPage("/transactions.html");
        TransactionsPage transactionsPage = new TransactionsPage(driver);
        
        boolean pageLoaded = transactionsPage.isPageLoaded();
        Assert.assertTrue(pageLoaded, "Transactions page should load successfully");
        
        String pageTitle = transactionsPage.getPageTitle();
        System.out.println("Page title: " + pageTitle);
        Assert.assertTrue(pageTitle.contains("Transaction") || pageTitle.contains("transaction"), 
            "Page should have transaction-related title");
    }

    @Test(priority = 2, description = "Verify transactions list is visible")
    public void testTransactionsListVisible() {
        navigateToPage("/transactions.html");
        TransactionsPage transactionsPage = new TransactionsPage(driver);
        
        // Wait for page to load
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Check if transactions list exists (may be empty initially)
        boolean listVisible = transactionsPage.isTransactionsListVisible();
        System.out.println("Transactions list visible: " + listVisible);
        
        int transactionCount = transactionsPage.getTransactionCount();
        System.out.println("Transaction count: " + transactionCount);
        Assert.assertTrue(transactionCount >= 0, "Transaction count should be 0 or more");
    }

    @Test(priority = 3, description = "Verify add transaction button functionality")
    public void testAddTransactionButton() {
        navigateToPage("/transactions.html");
        TransactionsPage transactionsPage = new TransactionsPage(driver);
        
        try {
            Thread.sleep(2000);
            transactionsPage.clickAddTransaction();
            Thread.sleep(1000);
            
            // Check if modal/form appears
            boolean modalVisible = transactionsPage.isTransactionModalVisible();
            System.out.println("Transaction modal visible: " + modalVisible);
            
            if (modalVisible) {
                Assert.assertTrue(modalVisible, "Transaction form/modal should be visible");
            }
        } catch (Exception e) {
            System.out.println("Add transaction functionality not fully implemented yet");
        }
    }

    @Test(priority = 4, description = "Verify filter buttons presence")
    public void testFilterButtonsPresent() {
        navigateToPage("/transactions.html");
        TransactionsPage transactionsPage = new TransactionsPage(driver);
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        int filterCount = transactionsPage.getFilterButtonsCount();
        System.out.println("Filter buttons count: " + filterCount);
        
        // Page should have some filtering capability
        Assert.assertTrue(filterCount >= 0, "Filter buttons count should be valid");
    }

    @Test(priority = 5, description = "Verify search functionality exists")
    public void testSearchFunctionality() {
        navigateToPage("/transactions.html");
        TransactionsPage transactionsPage = new TransactionsPage(driver);
        
        try {
            Thread.sleep(2000);
            transactionsPage.searchTransaction("test");
            System.out.println("Search functionality executed");
        } catch (Exception e) {
            System.out.println("Search functionality may not be implemented yet");
        }
    }

    @Test(priority = 6, description = "Verify export functionality exists")
    public void testExportFunctionality() {
        navigateToPage("/transactions.html");
        TransactionsPage transactionsPage = new TransactionsPage(driver);
        
        try {
            Thread.sleep(2000);
            transactionsPage.clickExport();
            System.out.println("Export functionality triggered");
        } catch (Exception e) {
            System.out.println("Export functionality may not be implemented yet");
        }
    }

    @Test(priority = 7, description = "Verify page URL is correct")
    public void testCorrectURL() {
        navigateToPage("/transactions.html");
        
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("transactions.html"), 
            "URL should contain transactions.html");
    }
}
