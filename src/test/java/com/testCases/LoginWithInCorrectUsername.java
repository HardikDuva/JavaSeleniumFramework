package com.testCases;

import com.configuration.BaseTest;
import com.utilities.UserDetailsConfig;

import org.testng.annotations.Test;

import static com.utilities.TestDataConstants.HOME_PAGE_URL;
import static com.utilities.TestDataConstants.PASSWORD;

public class LoginWithInCorrectUsername extends BaseTest {

	@Test
	public void LoginInCheckWithIncorrectUsername() {
		ObLogIn().enterUsername("UserName")
				.enterPassword(PASSWORD)
				.clickOnLoginInButton();

		if(ObLogIn().getPageURL().contains(HOME_PAGE_URL)) {
			errorLog(test,"User Logged-In Successfully even password is incorrect");
		}
		else {
			infoLog(test,"User is not Logged-In when password is incorrect");
		}
	}
}


