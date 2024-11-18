package com.pages;

import com.configuration.AbstractionPOM;
import com.configuration.BaseDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;


public class LogInPage extends AbstractionPOM {

    public LogInPage(BaseDriver baseDriver) {
        super(baseDriver);
    }

    @FindBy(xpath = "//div[@class=\"login-box\"]//input[@id=\"user-name\"]")
    private WebElement usernameInputEle;

    @FindBy(xpath = "//div[@class=\"login-box\"]//input[@id=\"password\"]")
    private WebElement passwordInputEle;

    @FindBy(xpath = "//div[@class=\"login-box\"]//input[@id=\"login-button\"]")
    private WebElement loginButtonClickEle;

    /**
     * The user enter username
     */
    public LogInPage enterUsername(String userName) {
        try {
            baseDriver.infoLog("Enter Username :" + userName);
            baseDriver.waitForElementVisible(usernameInputEle);
            baseDriver.inputText(usernameInputEle,userName);
        } catch (Exception e) {
            baseDriver.errorLog("Username field is not editable");
        }
        return this;
    }

    /**
     * The user enter password
     */
    public LogInPage enterPassword(String password) {
        try {
            baseDriver.infoLog("Enter Password :" + password);
            baseDriver.waitForElementVisible(passwordInputEle);
            baseDriver.inputText(passwordInputEle,password);
        } catch (Exception e) {
            baseDriver.errorLog("Password field is not editable");
        }
        return this;
    }

    /**
     * The user click on Login button
     */
    public LogInPage clickOnLoginInButton() {
        try {
            baseDriver.infoLog("Click on Login Button");
            baseDriver.clickAndWait(loginButtonClickEle);
        } catch (Exception e) {
            baseDriver.errorLog("Login Button field is not clickable");
        }
        return this;
    }

    /**
     * Get current page URL
     */
    //will Navigate to Product URL Page
    public String getPageURL() {
        baseDriver.infoLog("Get Current Page URL");
        return baseDriver.getCurrentUrl();
    }

}
