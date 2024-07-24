package com.pages;

import com.aventstack.extentreports.ExtentTest;
import com.configuration.AbstractionPOM;
import com.configuration.BaseDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import org.testng.Assert;

public class LogInPage extends AbstractionPOM {

    public LogInPage(BaseDriver baseDriver,ExtentTest test) {
        super(baseDriver,test);
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
            infoLog(test,"Enter Username :" + userName);
            baseDriver.waitForElementVisible(usernameInputEle);
            baseDriver.inputText(usernameInputEle,userName);
        } catch (Exception e) {
            errorLog(test,"Username field is not editable");
        }
        return this;
    }

    /**
     * The user enter password
     */
    public LogInPage enterPassword(String password) {
        try {
            infoLog(test,"Enter Password :" + password);
            baseDriver.waitForElementVisible(passwordInputEle);
            baseDriver.inputText(passwordInputEle,password);
        } catch (Exception e) {
            errorLog(test,"Password field is not editable");
        }
        return this;
    }

    /**
     * The user click on Login button
     */
    public LogInPage clickOnLoginInButton() {
        try {
            infoLog(test,"Click on Login Button");
            baseDriver.clickAndWait(loginButtonClickEle);
        } catch (Exception e) {
            errorLog(test,"Login Button field is not clickable");
        }
        return this;
    }

    /**
     * Get current page URL
     */
    //will Navigate to Product URL Page
    public String getPageURL() {
        infoLog(test,"Get Current Page URL");
        return baseDriver.getCurrentUrl();
    }

}
