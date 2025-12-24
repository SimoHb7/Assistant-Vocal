package com.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class HistoryPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private By pageTitle = By.cssSelector("h1, .page-title");
    private By conversationsList = By.className("conversations-list");
    private By conversationItems = By.className("conversation-item");
    private By searchInput = By.id("searchConversation");
    private By dateRangeFilter = By.id("dateRange");
    private By clearHistoryButton = By.id("clearHistoryBtn");
    private By exportHistoryButton = By.id("exportHistoryBtn");
    private By conversationDetails = By.className("conversation-details");
    private By emptyStateMessage = By.className("empty-state");
    private By loadMoreButton = By.id("loadMoreBtn");
    private By filterOptions = By.className("filter-option");

    public HistoryPage(WebDriver driver) {
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

    public boolean isConversationsListVisible() {
        try {
            return driver.findElement(conversationsList).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public int getConversationCount() {
        try {
            List<WebElement> conversations = driver.findElements(conversationItems);
            return conversations.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public void searchConversation(String searchTerm) {
        try {
            WebElement search = driver.findElement(searchInput);
            search.clear();
            search.sendKeys(searchTerm);
        } catch (Exception e) {
            System.out.println("Search input not found");
        }
    }

    public void clickConversation(int index) {
        try {
            List<WebElement> conversations = driver.findElements(conversationItems);
            if (index < conversations.size()) {
                conversations.get(index).click();
            }
        } catch (Exception e) {
            System.out.println("Error clicking conversation: " + e.getMessage());
        }
    }

    public boolean isConversationDetailsVisible() {
        try {
            return driver.findElement(conversationDetails).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickClearHistory() {
        try {
            driver.findElement(clearHistoryButton).click();
        } catch (Exception e) {
            System.out.println("Clear history button not found");
        }
    }

    public void clickExportHistory() {
        try {
            driver.findElement(exportHistoryButton).click();
        } catch (Exception e) {
            System.out.println("Export history button not found");
        }
    }

    public boolean isEmptyStateVisible() {
        try {
            return driver.findElement(emptyStateMessage).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickLoadMore() {
        try {
            driver.findElement(loadMoreButton).click();
        } catch (Exception e) {
            System.out.println("Load more button not found");
        }
    }

    public void selectDateRange(String range) {
        try {
            driver.findElement(dateRangeFilter).sendKeys(range);
        } catch (Exception e) {
            System.out.println("Date range filter not found");
        }
    }

    public int getFilterOptionsCount() {
        try {
            return driver.findElements(filterOptions).size();
        } catch (Exception e) {
            return 0;
        }
    }
}
