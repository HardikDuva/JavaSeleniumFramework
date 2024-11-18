package com.testCases;

import com.configuration.ObjectRepo;

import org.testng.annotations.Test;

import static com.utilities.TestDataConstants.*;

public class LoginWithInCorrectUsername extends ObjectRepo {

	@Test
    public void LoginInWithIncorrectUsername() {
		ObLogIn().enterUsername(USERNAME)
				.enterPassword(PASSWORD)
				.clickOnLoginInButton();

		//This is just the demo purpose we can add more validation
		if(ObHome().getPageURL().contains(HOME_PAGE_URL)) {
			baseDriver.errorLog("User Logged-In Successfully even password is incorrect");
		}
		else {
			baseDriver.infoLog("User is not Logged-In when password is incorrect");
		}
	}
}


