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

import static com.utilities.TestDataConstants.*;

public class BaseTest {

	protected BaseDriver baseDriver = null;
    protected static String clientName = null;
	private String browser = null;
	protected static String outputDirPath = null;
	private String finalOutputReport = null;
	public static ExtentReports extent;
	protected ExtentTest test;

	@BeforeSuite
	@Parameters({"Browser" , "Client"})
	public void setConfiguration(String brw,String client) {
		this.browser = brw;
		clientName = client;
		outputDirPath = "." + File.separator + "TestResult" + File.separator + client
				+ File.separator + DateTimeConnector.getTimeStamp() + File.separator + browser ;

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
			finalOutputReport = outputDirPath
					+ File.separator + "report.html";
			ExtentSparkReporter htmlReporter = new ExtentSparkReporter(finalOutputReport);
			extent = new ExtentReports();
			extent.attachReporter(htmlReporter);

		} catch (Exception e) {
			Assert.fail("Error while creating directory for extent report" + e.getMessage());
		}
	}

	@BeforeMethod
	public void launchBrowser(ITestResult result) {
        String testName = result.getMethod().getMethodName();
		test = extent.createTest(testName);

		// Open browser
		try {
			baseDriver = new BaseDriver(browser, SERVER_URL,WAIT_STANDARD);
			baseDriver.gotoUrl(PRODUCT_URL);

		} catch (Exception e) {
			errorLog(test,"Open browser failed!" + e.getMessage());
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
		if(SEND_EMAIL) {
			sendEmail();
		}
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

	public void sendEmail() {
		//Send Report
		EmailConnector emailConnector = new EmailConnector();
		String subject = "Execution Report for the client " + clientName;
		String bodyContent = "Please Find Attached Execution Report with" +
				"\n Browser : " + browser +
				"\n Time    : " + DateTimeConnector.getTimeStampWithLocaleEnglish();
		File tempFile = new File(finalOutputReport);

		emailConnector.sendEmailWithAttachment(EMAIL_TO,subject,bodyContent,tempFile);
	}
}
	
	