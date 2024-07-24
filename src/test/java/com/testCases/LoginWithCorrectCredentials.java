package com.testCases;

import com.configuration.BaseTest;
import com.utilities.UserDetailsConfig;

import org.testng.annotations.Test;

public class LoginWithCorrectCredentials extends BaseTest {

	@Test
	public void LoginInWithCorrectCredential()  {
		String username = UserDetailsConfig.get("UserName");
		String password = UserDetailsConfig.get("Password");
		String productExpURL = UserDetailsConfig.get("productHomePageURL");

		ObLogIn().enterUsername(username)
				.enterPassword(password)
				.clickOnLoginInButton();

		if(ObLogIn().getPageURL().contains(productExpURL)) {
			infoLog(test,"User Logged-In Successfully");
		}
		else {
			errorLog(test,"User is not logged In Successfully");
		}
	}

}


