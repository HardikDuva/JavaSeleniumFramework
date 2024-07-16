package testCases;

import configuration.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginWithCorrectCredentials extends BaseTest {

	@Test
	public void LoginInWithCorrectCredential()  {
		String productExpURL = "inventory.html";
		boolean testResult = false;
		try {
			String username = TestData.get("UserDetails", "UserName");
			String password = TestData.get("UserDetails", "Password");
			logger.i("Check if User Can Login with correct credentials");

			ObLogIn().enterUsername(username)
					.enterPassword(password)
					.clickOnLoginInButton();

			Assert.assertTrue(ObLogIn().getPageURL()
					.contains(productExpURL));
			testResult = true;
		}

		finally {
			if(!testResult) {
				baseDriver.takeFailedScenarioScreenshot(testName);
			}
		}
	}

}


