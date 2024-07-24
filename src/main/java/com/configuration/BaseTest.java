package com.configuration;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.utilities.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.*;
import org.testng.annotations.*;
import com.pages.LogInPage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class BaseTest {

	protected BaseDriver baseDriver = null;
	protected String testName = null;
	protected static String clientName = null;
	protected String browser = null;
	protected static String outputDirPath = null;

	public static ExtentReports extent;
	protected ExtentTest test;

	@BeforeSuite
	@Parameters({"Browser" , "Client"})
	public void setConfiguration(String brw,String client) {
		this.browser = brw;
		clientName = client;
		outputDirPath = "." + File.separator + "TestResult" + File.separator + client
				+ File.separator + FileConnector.getTimeStamp() + File.separator + browser ;

		try {
			FrameworkConfig.init(System.getProperty("user.dir")
					+ "/src/test/java/resource/configuration/FW_Config.properties");

			UserDetailsConfig.init(System.getProperty("user.dir")
					+ "/clients/" + clientName + "/TestData/TestData.properties");

		} catch (Exception e) {
			Assert.fail("Framework configuration file initialization error" + e.getMessage());
		}

		try {
			FileConnector.createDir(outputDirPath);
			ExtentSparkReporter htmlReporter = new ExtentSparkReporter(outputDirPath
					+ File.separator + "report.html");
			extent = new ExtentReports();
			extent.attachReporter(htmlReporter);

		} catch (Exception e) {
			Assert.fail("Error while creating directory for extent report" + e.getMessage());
		}

	}

	@BeforeMethod
	public void launchBrowser(ITestResult result) {
		int implicitTimeout = 0;
		String serverURL = null;
		String projectURL = null;
		testName = result.getMethod().getMethodName();
		test = extent.createTest(testName);

		try {
			implicitTimeout = Integer.parseInt(FrameworkConfig.get("WAIT_STANDARD"));
			serverURL = FrameworkConfig.get("SERVER_URL");
			projectURL = UserDetailsConfig.get("PRODUCT_URL");

		} catch (Exception e) {
			Assert.fail("Framework configuration file reading error" + e.getMessage());
		}

		// Open browser
		try {
			baseDriver = new BaseDriver(browser, serverURL,implicitTimeout);
			baseDriver.gotoUrl(projectURL);

		} catch (Exception e) {
			Assert.fail("Open browser failed!" + e.getMessage());
		}
	}

	@AfterMethod
	public void closeBrowser() {
		if (baseDriver != null) {
			baseDriver.quit();
		}
	}

	@AfterSuite
	public void reportGenerated() throws IOException {
		extent.flush();
	}

	public LogInPage ObLogIn() {
		return new LogInPage(baseDriver,test);
	}

	protected void infoLog(ExtentTest test, String msg) {
		test.log(Status.INFO, msg);
	}

	protected void errorLog(ExtentTest test, String msg) {
		baseDriver.captureFailTestScreenshot(test,msg);
		Assert.fail(msg);
	}
}
	
	