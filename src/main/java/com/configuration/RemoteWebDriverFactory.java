package com.configuration;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;

import java.net.MalformedURLException;
import java.net.URL;

import static com.configuration.BaseTest.printMsgOnConsole;

public class RemoteWebDriverFactory {

	public WebDriver driver = null;
	private String browser;
	MutableCapabilities options = null;
	private String URL = null;
	private String device;
	ExtentTest extentTest = null;

	public RemoteWebDriverFactory() {
	}

	public WebDriver launchBrowser(ExtentTest tst,String dwc, String brw, String serverURL) {
		this.extentTest = tst;
		this.device = dwc;
		this.browser = brw;
		this.URL = serverURL;
		this.options = parseMutableCapabilities();
		return loadWebDriverObject();
	}

	/**
	 * Set Mutable Capabilities
	 */
	public MutableCapabilities parseMutableCapabilities() {
		MutableCapabilities option = null;
		try {
			switch (this.browser.toUpperCase()) {
				case "FIREFOX" -> option = new FirefoxOptions();
				case "CHROME" -> option = new ChromeOptions();
				case "EDGE" -> option = new EdgeOptions();
			}

			if (null != option) {
				option = option.merge(option);
			}
		}

		catch (Exception e) {
				extentTest.log(Status.FAIL,"Failed while initiating browser capabilities!" + e.getMessage());
				Assert.fail();
		}
		return option;
	}

	/**
	 * Load Web Driver Object
	 */
	private WebDriver loadWebDriverObject() {
		try {
			if (null != this.options) {
				driver = new RemoteWebDriver(new URL(this.URL), this.options);
				printMsgOnConsole("Remote Driver is initialized for the " +
						"\n Platform : Mobile " +
						"\n ,Device : " + device +
						"\n ,Browser : " + browser);
			}

			else {
				extentTest.log(Status.FAIL,"Remote Driver is initialized for the " +
						"\n Platform : Mobile " +
						"\n ,Device : " + device +
						"\n ,Browser : " + browser);
			}

		} catch (MalformedURLException e) {
			extentTest.log(Status.FAIL,"\n Malformed URL Exception while connecting to the Selenium GRID Hub \n" + e.getMessage());
			Assert.fail(e.getMessage());
		} catch (SessionNotCreatedException e) {
			extentTest.log(Status.FAIL,"Selenium Grid was unable to create a session using the following capabilities: "+
					"\n Platform : Mobile " +
					"\n ,Device : " + device +
					"\n ,Browser : " + browser);
			Assert.fail(e.getMessage());
		}

		driver.manage()
				.window()
				.maximize();

		return driver;
	}


}