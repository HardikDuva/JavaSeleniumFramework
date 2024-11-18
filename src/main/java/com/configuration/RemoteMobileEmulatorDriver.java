package com.configuration;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import static com.configuration.BaseDriver.printMsgOnConsole;

public class RemoteMobileEmulatorDriver {

	private WebDriver driver = null;
	private String browser;
	MutableCapabilities options = null;
	private String URL = null;
	private String device = null;
	ExtentTest extentTest = null;
	DesiredCapabilities caps = new DesiredCapabilities();

	public RemoteMobileEmulatorDriver() {
	}

	public WebDriver launchBrowser(ExtentTest ext,String dvc,String brw, String serverURL) {
		this.extentTest = ext;
		this.device = dvc;
		this.browser = brw;
		this.URL = serverURL;
		this.options = parseMutableCapabilities();
		return loadMobileDriverObject();
	}

	/**
	 * Set Mutable Capabilities
	 */
	public MutableCapabilities parseMutableCapabilities() {
		MutableCapabilities option = null;
		try {
			switch (this.browser.toUpperCase()) {
				case "FIREFOX":
					option = new FirefoxOptions();
					break;
				case "CHROME":
					ChromeOptions cOption = new ChromeOptions();
					cOption.setExperimentalOption("mobileEmulation", Map.of("deviceName", this.device));
					option = cOption;
					break;
				case "EDGE":
					option = new EdgeOptions();
					break;
			}
		}

		catch (Exception e) {
			extentTest.log(Status.FAIL,"Failed while initiating browser capabilities!" + e.getMessage());
			Assert.fail(e.getMessage());
		}

		return option;

	}

	/**
	 * Load Web Driver Object
	 */
	private WebDriver loadMobileDriverObject() {
		try {
			if (null != this.options) {
				driver = new RemoteWebDriver(new URL(this.URL), this.options);

				printMsgOnConsole("Remote Driver is initialized for the " +
						"\n Platform : Windows " +
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

		return driver;
	}

}