package com.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class DashboardPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private By userProfile = By.className("user-profile");
    private By logoutButton = By.id("logoutBtn");
    private By balanceCard = By.className("balance-card");
    private By transactionsList = By.className("transactions-list");
    private By quickActionsButtons = By.className("action-btn");

    public DashboardPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public boolean isUserLoggedIn() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(userProfile));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isBalanceCardDisplayed() {
        try {
            return driver.findElement(balanceCard).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void logout() {
        WebElement logoutBtn = driver.findElement(logoutButton);
        logoutBtn.click();
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public int getQuickActionsCount() {
        return driver.findElements(quickActionsButtons).size();
    }
}
