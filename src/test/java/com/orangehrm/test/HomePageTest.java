package com.orangehrm.test;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DataProviders;
import com.orangehrm.utilities.ExtentManager;

public class HomePageTest extends BaseClass {
	
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
		public void verifyOrangeHRMLogo(String username, String password) {
			
			//ExtentManager.startTest("Homepage verify logo test");  //this has been implemented in test listener
			ExtentManager.logStep("Navigate to login page entering username and password");
			loginPage.login(username, password);
			ExtentManager.logStep("Verifying logo is visible or not");
			Assert.assertTrue(homePage.verifyOrangeHRMLogo(),"Logo is not visible");
			ExtentManager.logStep("Validation Successful");
			ExtentManager.logStep("Logged Out Successfully");
			
		}

}
