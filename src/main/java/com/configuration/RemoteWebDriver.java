package com.configuration;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.Assert;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.ArrayList;
import java.util.List;

public class RemoteWebDriver {

	public WebDriver driver = null;
	private final Browsers browser;
	MutableCapabilities options = null;
	private String URL = null;

	public RemoteWebDriver(String browser,String serverURL) {
		this.browser = parseBrowser(browser);
		this.options = parseMutableCapabilities();
		this.URL = serverURL;
	}

	public WebDriver launchBrowser() {
		return loadWebDriverObject();
	}

	public enum Browsers {
		FIREFOX, EDGE, CHROME
	}
	/**
	 * Parse Browser
	 *
	 * @param browserStr - The browser name
	 */
	public Browsers parseBrowser(String browserStr) {
		Browsers browser;
		List<String> ffKeys = new ArrayList<>();
		ffKeys.add("firefox");
		ffKeys.add("ff");
		ffKeys.add("Firefox");
		ffKeys.add("MozilaFirefox");
		ffKeys.add("Mozilafirefox");
		ffKeys.add("mozilafirefox");

		List<String> edgeKeys = new ArrayList<>();
		edgeKeys.add("edge");
		edgeKeys.add("Edge");
		edgeKeys.add("MicrosoftEdge");

		List<String> chromeKeys = new ArrayList<>();
		chromeKeys.add("googlechrome");
		chromeKeys.add("chrome");
		chromeKeys.add("Chrome");

		if (ffKeys.contains(browserStr)) {
			browser = Browsers.FIREFOX;
		}
		else if (edgeKeys.contains(browserStr)) {
			browser = Browsers.EDGE;
		}else if (chromeKeys.contains(browserStr)) {
			browser = Browsers.CHROME;
		} else {
			browser = Browsers.CHROME;
		}

		return browser;
	}

	/**
	 * Set Mutable Capabilities
	 */
	public MutableCapabilities parseMutableCapabilities() {
		MutableCapabilities option = null;
		switch (this.browser.toString()) {
			case "FIREFOX" -> option = new FirefoxOptions();
			case "CHROME" -> option = new ChromeOptions();
			case "EDGE" -> option = new EdgeOptions();
		}

		if (null != option) {
			option = option.merge(option);
		}

		return option;
	}

	/**
	 * Load Web Driver Object
	 */
	private WebDriver loadWebDriverObject() {
		try {
			if (null != this.options) {
				driver = new org.openqa.selenium.remote.RemoteWebDriver(new URL(this.URL), this.options);
			}
			else {
				Assert.fail("Web-Driver is not initialized.");
			}

		} catch (MalformedURLException e) {
			System.err.println("\nMalformed URL Exception while connecting to the Selenium GRID Hub\n" + e.getMessage());
		} catch (SessionNotCreatedException e) {
			System.err.println("Selenium Grid was unable to create a session using the following capabilities: \n"
					+ "BrowserName = " + browser);
		}

		driver.manage()
				.window()
				.maximize();

		return driver;
	}


}