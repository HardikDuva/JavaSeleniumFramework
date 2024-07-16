package configuration;

import org.testng.*;
import org.testng.annotations.*;
import pages.LogInPage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class BaseTest implements ITestListener {

	protected BaseDriver baseDriver = null;
	public ReadXMLData fwConfigData =
			new ReadXMLData("./configuration/Configuration.xml");
	protected AutoLogger logger = new AutoLogger(BaseTest.class);
	protected String testName = null;
	protected static String clientName = null;
	protected String browser = null;
	protected static String outputDirPath = null;
	protected static ReadXMLData TestData = null;

	@BeforeSuite
	@Parameters({"Browser" , "Client"})
	public void setConfiguration(String brw,String client) {
		this.browser = brw;
		clientName = client;
		outputDirPath = "." + File.separator + "TestResult" + File.separator + client
				+ File.separator + browser + File.separator + Utilities.getTimeStamp();

		Utilities.deleteDir("." + File.separator + "ExecutionReportWithScreenshot");
		Utilities.createDir(outputDirPath);

		TestData = new ReadXMLData(
				"./clients/" + clientName + "/TestData/TestData.xml");
	}

	@BeforeMethod
	public void launchBrowser(ITestResult result) {
		// Open browser
		try {
			testName = result.getMethod().getMethodName();
			int implicitTimeout = Integer.parseInt(fwConfigData.get("Configuration", "ImplicitTimeout"));
			String projectURL = fwConfigData.get("Configuration", "PRODUCT_URL");
			String serverURL = fwConfigData.get("Configuration", "SERVER_URL");;
			baseDriver = new BaseDriver(browser, serverURL,implicitTimeout, outputDirPath);
			baseDriver.gotoUrl(projectURL);
		} catch (Exception e) {
			Assert.fail("Open browser failed!" + e);
		}
	}

	@AfterMethod
	public void closeBrowser() {
		if (baseDriver != null) {
			baseDriver.quit();
		}
	}

	@AfterSuite
	public void sendResultToEmail() throws IOException {
		String htmlReportPath = "." + File.separator + "test-output"
				+ File.separator + "html";
		String sendZipFolderPath = "." + File.separator + "ExecutionReportWithScreenshot"
				+ File.separator + "Report";
		String zipFilePath = sendZipFolderPath + ".zip";

		//Copy the test-execution and failed screenshot folders into one folder
		Utilities.createDir(sendZipFolderPath);
		Path targetDir = Path.of(sendZipFolderPath);
		Utilities.copyDirectory(Path.of(outputDirPath), targetDir);
		Utilities.copyDirectory(Path.of(htmlReportPath), targetDir);

		//Create Zip Folder
		Utilities.zipDirectory(targetDir,Path.of(zipFilePath));
		EmailConnector emailConnector = new EmailConnector();

		String to = "";
		String subject = "Execution Report";
		String bodyContent = "Please Find Execution Report";
		File tempFile = new File(zipFilePath);

		emailConnector.sendEmailWithAttachment(to,subject,bodyContent,tempFile);
	}

	public LogInPage ObLogIn() {
		return new LogInPage(baseDriver);
	}

}
	
	