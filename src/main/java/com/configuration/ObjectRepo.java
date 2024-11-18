package com.configuration;

import com.pages.HomePage;
import com.pages.LogInPage;

public class ObjectRepo extends BaseTest{

	public LogInPage ObLogIn() {
		return new LogInPage(baseDriver);
	}

	public HomePage ObHome() {
		return new HomePage(baseDriver);
	}
}
	
	