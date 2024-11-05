package com.testCases;

import com.configuration.ObjectRepo;

import org.testng.annotations.Test;

import static com.utilities.TestDataConstants.*;

public class LoginWithCorrectCredentials extends ObjectRepo {

	@Test
	public void LoginInWithCorrectCredential()  {
		ObLogIn().enterUsername(USERNAME)
				.enterPassword(PASSWORD)
				.clickOnLoginInButton();

		if(ObLogIn().getPageURL().contains(HOME_PAGE_URL)) {
			infoLog("User Logged-In Successfully");
		}
		else {
			errorLog("User is not logged In Successfully");
		}
	}
}
