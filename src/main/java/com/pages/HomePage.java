package com.pages;

import com.configuration.AbstractionPOM;
import com.configuration.BaseDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomePage extends AbstractionPOM {

    public HomePage(BaseDriver baseDriver) {
        super(baseDriver);
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
