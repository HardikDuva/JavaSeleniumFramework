package com.configuration;


import org.openqa.selenium.support.PageFactory;

public class AbstractionPOM extends BaseTest {

	// Default constructor
	public AbstractionPOM(BaseDriver baseDriver) {
		this.baseDriver = baseDriver;
		// This initElements method will create all WebElements
		PageFactory.initElements(baseDriver.driver, this);
	}

}
