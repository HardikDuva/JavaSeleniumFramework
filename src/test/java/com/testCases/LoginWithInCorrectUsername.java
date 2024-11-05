package com.testCases;

import com.configuration.ObjectRepo;

import org.testng.annotations.Test;

import static com.utilities.TestDataConstants.*;


public class LoginWithInCorrectUsername extends ObjectRepo {

	@Test
    public void LoginInCheckWithIncorrectUsername() {
		ObLogIn().enterUsername(USERNAME)
				.enterPassword(PASSWORD)
				.clickOnLoginInButton();

		if(ObLogIn().getPageURL().contains(HOME_PAGE_URL)) {
			errorLog("User Logged-In Successfully even password is incorrect");
		}
		else {
			infoLog("User is not Logged-In when password is incorrect");
		}
	}
}


