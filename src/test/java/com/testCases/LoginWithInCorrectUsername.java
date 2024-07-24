package com.testCases;

import com.configuration.BaseTest;
import com.utilities.UserDetailsConfig;

import org.testng.annotations.Test;

public class LoginWithInCorrectUsername extends BaseTest {

	@Test
	public void LoginInCheckWithIncorrectUsername() {
		String password = UserDetailsConfig.get("Password");
		String productExpURL = UserDetailsConfig.get("productHomePageURL");

		ObLogIn().enterUsername("UserName")
				.enterPassword(password)
				.clickOnLoginInButton();

		if(ObLogIn().getPageURL().contains(productExpURL)) {
			errorLog(test,"User Logged-In Successfully even password is incorrect");
		}
		else {
			infoLog(test,"User is not Logged-In when password is incorrect");
		}
	}
}


