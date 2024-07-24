package com.configuration;


import com.aventstack.extentreports.ExtentTest;
import org.openqa.selenium.support.PageFactory;

public class AbstractionPOM extends BaseTest {

	// Default constructor
	public AbstractionPOM(BaseDriver baseDriver, ExtentTest test) {
		this.baseDriver = baseDriver;
		this.test = test;
		// This initElements method will create all WebElements
		PageFactory.initElements(baseDriver.driver, this);
	}

	public void updateBDriver(BaseDriver baseDriver) {
		this.baseDriver = baseDriver;
		// This initElements method will create all WebElements
				PageFactory.initElements(baseDriver.driver, this);
	}
}
