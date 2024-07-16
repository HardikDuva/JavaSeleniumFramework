package pages;

import configuration.BaseDriver;
import configuration.BaseTest;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import org.testng.Assert;

import java.util.List;

public class LogInPage extends AbstractionPOM{

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
            baseDriver.waitForElementVisible(usernameInputEle);
            baseDriver.inputText(usernameInputEle,userName);
        } catch (Exception e) {
            Assert.fail("Username field is not editable");
        }
        return this;
    }

    /**
     * The user enter password
     */
    public LogInPage enterPassword(String password) {
        try {
            baseDriver.waitForElementVisible(passwordInputEle);
            baseDriver.inputText(passwordInputEle,password);
        } catch (Exception e) {
            Assert.fail("Password field is not editable");
        }
        return this;
    }

    /**
     * The user click on Login button
     */
    public LogInPage clickOnLoginInButton() {
        try {
            baseDriver.clickAndWait(loginButtonClickEle);
        } catch (Exception e) {
            Assert.fail("Login Button field is not clickable");
        }

        return this;
    }

    /**
     * Checks if user Logged In
     */
    //will Navigate to Product URL Page
    public String getPageURL() {
        return baseDriver.getCurrentUrl();
    }

}
