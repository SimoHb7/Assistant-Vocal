package com.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class SettingsPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private By pageTitle = By.cssSelector("h1, .page-title");
    private By settingsTabs = By.className("settings-tab");
    private By profileTab = By.id("profileTab");
    private By securityTab = By.id("securityTab");
    private By notificationsTab = By.id("notificationsTab");
    private By preferencesTab = By.id("preferencesTab");
    
    // Profile settings
    private By nameInput = By.id("name");
    private By emailInput = By.id("email");
    private By phoneInput = By.id("phone");
    private By saveProfileButton = By.id("saveProfileBtn");
    
    // Security settings
    private By currentPasswordInput = By.id("currentPassword");
    private By newPasswordInput = By.id("newPassword");
    private By confirmPasswordInput = By.id("confirmPassword");
    private By changePasswordButton = By.id("changePasswordBtn");
    private By twoFactorToggle = By.id("twoFactorAuth");
    
    // Notification settings
    private By emailNotificationsToggle = By.id("emailNotifications");
    private By pushNotificationsToggle = By.id("pushNotifications");
    private By smsNotificationsToggle = By.id("smsNotifications");
    
    // Preferences
    private By languageSelect = By.id("language");
    private By currencySelect = By.id("currency");
    private By themeToggle = By.id("darkMode");
    private By savePreferencesButton = By.id("savePreferencesBtn");
    
    // Common
    private By successMessage = By.className("success-message");
    private By errorMessage = By.className("error-message");
    private By deleteAccountButton = By.id("deleteAccountBtn");

    public SettingsPage(WebDriver driver) {
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

    public int getTabsCount() {
        try {
            List<WebElement> tabs = driver.findElements(settingsTabs);
            return tabs.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public void clickProfileTab() {
        try {
            driver.findElement(profileTab).click();
        } catch (Exception e) {
            System.out.println("Profile tab not found");
        }
    }

    public void clickSecurityTab() {
        try {
            driver.findElement(securityTab).click();
        } catch (Exception e) {
            System.out.println("Security tab not found");
        }
    }

    public void clickNotificationsTab() {
        try {
            driver.findElement(notificationsTab).click();
        } catch (Exception e) {
            System.out.println("Notifications tab not found");
        }
    }

    public void clickPreferencesTab() {
        try {
            driver.findElement(preferencesTab).click();
        } catch (Exception e) {
            System.out.println("Preferences tab not found");
        }
    }

    // Profile methods
    public void updateProfile(String name, String email, String phone) {
        try {
            if (name != null && !name.isEmpty()) {
                WebElement nameField = driver.findElement(nameInput);
                nameField.clear();
                nameField.sendKeys(name);
            }
            
            if (email != null && !email.isEmpty()) {
                WebElement emailField = driver.findElement(emailInput);
                emailField.clear();
                emailField.sendKeys(email);
            }
            
            if (phone != null && !phone.isEmpty()) {
                WebElement phoneField = driver.findElement(phoneInput);
                phoneField.clear();
                phoneField.sendKeys(phone);
            }
        } catch (Exception e) {
            System.out.println("Error updating profile: " + e.getMessage());
        }
    }

    public void saveProfile() {
        try {
            driver.findElement(saveProfileButton).click();
        } catch (Exception e) {
            System.out.println("Save profile button not found");
        }
    }

    // Security methods
    public void changePassword(String currentPassword, String newPassword, String confirmPassword) {
        try {
            driver.findElement(currentPasswordInput).sendKeys(currentPassword);
            driver.findElement(newPasswordInput).sendKeys(newPassword);
            driver.findElement(confirmPasswordInput).sendKeys(confirmPassword);
            driver.findElement(changePasswordButton).click();
        } catch (Exception e) {
            System.out.println("Error changing password: " + e.getMessage());
        }
    }

    public void toggleTwoFactorAuth() {
        try {
            driver.findElement(twoFactorToggle).click();
        } catch (Exception e) {
            System.out.println("2FA toggle not found");
        }
    }

    // Notification methods
    public void toggleEmailNotifications() {
        try {
            driver.findElement(emailNotificationsToggle).click();
        } catch (Exception e) {
            System.out.println("Email notifications toggle not found");
        }
    }

    public void togglePushNotifications() {
        try {
            driver.findElement(pushNotificationsToggle).click();
        } catch (Exception e) {
            System.out.println("Push notifications toggle not found");
        }
    }

    // Preferences methods
    public void selectLanguage(String language) {
        try {
            driver.findElement(languageSelect).sendKeys(language);
        } catch (Exception e) {
            System.out.println("Language select not found");
        }
    }

    public void selectCurrency(String currency) {
        try {
            driver.findElement(currencySelect).sendKeys(currency);
        } catch (Exception e) {
            System.out.println("Currency select not found");
        }
    }

    public void toggleTheme() {
        try {
            driver.findElement(themeToggle).click();
        } catch (Exception e) {
            System.out.println("Theme toggle not found");
        }
    }

    public void savePreferences() {
        try {
            driver.findElement(savePreferencesButton).click();
        } catch (Exception e) {
            System.out.println("Save preferences button not found");
        }
    }

    // Common methods
    public boolean isSuccessMessageVisible() {
        try {
            return driver.findElement(successMessage).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isErrorMessageVisible() {
        try {
            return driver.findElement(errorMessage).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getSuccessMessage() {
        try {
            return driver.findElement(successMessage).getText();
        } catch (Exception e) {
            return "";
        }
    }

    public String getErrorMessage() {
        try {
            return driver.findElement(errorMessage).getText();
        } catch (Exception e) {
            return "";
        }
    }
}
