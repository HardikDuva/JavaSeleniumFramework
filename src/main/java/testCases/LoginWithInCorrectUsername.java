package testCases;

import configuration.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginWithInCorrectUsername extends BaseTest {

	@Test
	public void LoginInCheckWithIncorrectUsername() {
		String productExpURL = "inventory.html";
		boolean testResult = false;
		try {
			String password = TestData.get("UserDetails", "Password");
			logger.i("Check if User Can Login with Incorrect Username");

			ObLogIn().enterUsername("UserName")
					.enterPassword(password)
					.clickOnLoginInButton();

			Assert.assertTrue(ObLogIn().getPageURL()
					.contains(productExpURL));
			testResult = true;
		}

		finally{
			if(!testResult) {
				baseDriver.takeFailedScenarioScreenshot(testName);
			}
		}
	}
}


