package com.orangehrm.test;


import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DataProviders;
import com.orangehrm.utilities.ExtentManager;

//extends for using driver

    public class LoginPageTest extends BaseClass {
	
	//To initialize page classes create private variables
	private LoginPage loginPage;
	private HomePage homePage;
	
	@BeforeMethod	
	public void setupPages() {
		
		//Create object of the pages
		loginPage = new LoginPage(getDriver());
		homePage = new HomePage(getDriver());
	}

	
	@Test(dataProvider="validLoginData", dataProviderClass = DataProviders.class)
	public void verifyValidLoginTest(String username, String password) {
		
		
		// ExtentManager.startTest("Valid login test"); //this has been implemented in test listener
		System.out.println("Running testmethod1 on thread: " +Thread.currentThread().getId());
		ExtentManager.logStep("Navigate to login page entering username and password");
		loginPage.login(username, password);
		ExtentManager.logStep("Verifying Admin tab is visible or not");
		Assert.assertTrue(homePage.isAdminTabVisible(),"Admin tab should be visible after successful login");
		ExtentManager.logStep("Validation Successful");
		homePage.logout();
		ExtentManager.logStep("Logged Out Successfully");
		staticWait(2); //we can call this directly, because we extends baseclass here
		
	}
	
	@Test(dataProvider="invalidLoginData", dataProviderClass = DataProviders.class)
	public void inValidLoginTest(String username, String password) {
		
		// ExtentManager.startTest("Invalid login test");  //this has been implemented in test listener
		System.out.println("Running testmethod2 on thread: " +Thread.currentThread().getId());
		ExtentManager.logStep("Navigate to login page entering username and password");
		loginPage.login(username, password);
		String expectedErrorMessage = "Invalid credentials";
		Assert.assertTrue(loginPage.verifyErrorMessage(expectedErrorMessage),"Test Failed: Invalid error message");
		ExtentManager.logStep("Validation Successful");
		ExtentManager.logStep("Logged Out Successfully");
		
	}
}
