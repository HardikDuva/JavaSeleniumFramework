package com.configuration;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.utilities.*;
import org.openqa.selenium.WebDriver;
import org.testng.*;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;

import static com.utilities.TestDataConstants.*;

public class BaseTest {

	protected BaseDriver baseDriver = null;
    protected static String clientName = null;
	private static String platform = null;
	private static String device = null;
	private static String browser = null;
	protected static String outputDirPath = null;
	private String finalOutputReport = null;
	public static ExtentReports extent;
	protected ExtentTest extentTest;

    @BeforeSuite
	@Parameters({"Platform","Device","Browser","Client"})
	public void setConfiguration(String pForm,String dwc,String brw,String client) {
		platform = pForm;
		device = dwc;
		browser = brw;
		clientName = client;
		outputDirPath = "." + File.separator + "TestResult" + File.separator + client
				+ File.separator + DateTimeConnector.getTimeStamp() + File.separator + platform +
				File.separator + device + File.separator + browser ;

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
		extentTest = extent.createTest(testName);

		WebDriver driver;
		// Open browser
		try {
			infoLog("WebDriver is started initiating for the \n Platform : " + platform
							+ " Device : " + device +
					"\n Browser : " + browser);
			if(platform.equalsIgnoreCase("windows")) {
				RemoteWebDriverFactory remoteWebDriver = new RemoteWebDriverFactory();
				driver = remoteWebDriver.launchBrowser(extentTest,device,browser, SERVER_URL);
			}

			else if (platform.equalsIgnoreCase("mobile")) {
				RemoteMobileEmulatorDriver remoteMobileEmulatorDriver =
						new RemoteMobileEmulatorDriver();
				driver = remoteMobileEmulatorDriver.launchBrowser(extentTest,device,browser, SERVER_URL);
			}

			else {
				RemoteWebDriverFactory remoteWebDriver = new RemoteWebDriverFactory();
				driver = remoteWebDriver.launchBrowser(extentTest,device,browser, SERVER_URL);
			}

			baseDriver = new BaseDriver(driver,WAIT_STANDARD);
			baseDriver.gotoUrl(PRODUCT_URL);

		} catch (Exception e) {
			extentTest.log(Status.FAIL,"Open browser failed!" + e.getMessage());
		}
	}

	@AfterMethod
	public void closeBrowser() {
		if (baseDriver != null) {
			baseDriver.quit();
		}
	}

	@AfterSuite
	public void generateReport() throws IOException {
		extent.flush();
		if(SEND_EMAIL) {
			sendEmail();
		}
	}

	protected void infoLog(String msg) {
		printMsgOnConsole(msg);
		extentTest.log(Status.INFO, msg);
	}

	protected void errorLog(String msg) {
		if(baseDriver!=null) {
			baseDriver.captureFailTestScreenshot(extentTest, msg);
		}

		printMsgOnConsole(msg);
		extentTest.log(Status.FAIL,msg);
		Assert.fail(msg);
	}

	private void sendEmail() {
		//Send Report
		EmailConnector emailConnector = new EmailConnector();
		String subject = "Execution Report for the client " + clientName;
		String bodyContent = "Please Find Attached Execution Report with" +
				"\n Browser : " + browser +
				"\n Time    : " + DateTimeConnector.getTimeStampWithLocaleEnglish();
		File tempFile = new File(finalOutputReport);

		emailConnector.sendEmailWithAttachment(EMAIL_TO,subject,bodyContent,tempFile);
	}

	public static void printMsgOnConsole(String msg) {
		System.out.println(msg);
	}

}
	
	