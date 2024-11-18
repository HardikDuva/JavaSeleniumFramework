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

		//This is just the demo purpose we can add more validation
		if(ObHome().getPageURL().contains(HOME_PAGE_URL)) {
			baseDriver.infoLog("User Logged-In Successfully");
		}

		else {
			baseDriver.errorLog("User is not logged In Successfully");
		}
	}
}
