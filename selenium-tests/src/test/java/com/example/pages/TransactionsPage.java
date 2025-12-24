package com.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class TransactionsPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private By pageTitle = By.cssSelector("h1, .page-title");
    private By addTransactionButton = By.id("addTransactionBtn");
    private By transactionsList = By.className("transactions-list");
    private By transactionItems = By.className("transaction-item");
    private By filterButtons = By.className("filter-btn");
    private By searchInput = By.id("searchTransaction");
    private By dateFilter = By.id("dateFilter");
    private By categoryFilter = By.id("categoryFilter");
    private By amountDisplay = By.className("amount");
    private By exportButton = By.id("exportBtn");
    
    // Transaction form locators
    private By transactionModal = By.id("transactionModal");
    private By amountInput = By.id("amount");
    private By descriptionInput = By.id("description");
    private By categorySelect = By.id("category");
    private By dateInput = By.id("date");
    private By typeSelect = By.id("type");
    private By saveButton = By.cssSelector("button[type='submit']");
    private By cancelButton = By.className("cancel-btn");

    public TransactionsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public boolean isPageLoaded() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getPageTitle() {
        try {
            return driver.findElement(pageTitle).getText();
        } catch (Exception e) {
            return driver.getTitle();
        }
    }

    public void clickAddTransaction() {
        try {
            WebElement addBtn = wait.until(ExpectedConditions.elementToBeClickable(addTransactionButton));
            addBtn.click();
        } catch (Exception e) {
            System.out.println("Add transaction button not found");
        }
    }

    public boolean isTransactionModalVisible() {
        try {
            WebElement modal = wait.until(ExpectedConditions.visibilityOfElementLocated(transactionModal));
            return modal.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void fillTransactionForm(String amount, String description, String category, String date, String type) {
        try {
            if (amount != null && !amount.isEmpty()) {
                driver.findElement(amountInput).clear();
                driver.findElement(amountInput).sendKeys(amount);
            }
            
            if (description != null && !description.isEmpty()) {
                driver.findElement(descriptionInput).clear();
                driver.findElement(descriptionInput).sendKeys(description);
            }
            
            if (category != null && !category.isEmpty()) {
                driver.findElement(categorySelect).sendKeys(category);
            }
            
            if (date != null && !date.isEmpty()) {
                driver.findElement(dateInput).clear();
                driver.findElement(dateInput).sendKeys(date);
            }
            
            if (type != null && !type.isEmpty()) {
                driver.findElement(typeSelect).sendKeys(type);
            }
        } catch (Exception e) {
            System.out.println("Error filling transaction form: " + e.getMessage());
        }
    }

    public void saveTransaction() {
        try {
            driver.findElement(saveButton).click();
        } catch (Exception e) {
            System.out.println("Save button not found");
        }
    }

    public void cancelTransaction() {
        try {
            driver.findElement(cancelButton).click();
        } catch (Exception e) {
            System.out.println("Cancel button not found");
        }
    }

    public int getTransactionCount() {
        try {
            List<WebElement> transactions = driver.findElements(transactionItems);
            return transactions.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean isTransactionsListVisible() {
        try {
            return driver.findElement(transactionsList).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void searchTransaction(String searchTerm) {
        try {
            WebElement search = driver.findElement(searchInput);
            search.clear();
            search.sendKeys(searchTerm);
        } catch (Exception e) {
            System.out.println("Search input not found");
        }
    }

    public void filterByCategory(String category) {
        try {
            driver.findElement(categoryFilter).sendKeys(category);
        } catch (Exception e) {
            System.out.println("Category filter not found");
        }
    }

    public void clickExport() {
        try {
            driver.findElement(exportButton).click();
        } catch (Exception e) {
            System.out.println("Export button not found");
        }
    }

    public int getFilterButtonsCount() {
        try {
            return driver.findElements(filterButtons).size();
        } catch (Exception e) {
            return 0;
        }
    }
}
