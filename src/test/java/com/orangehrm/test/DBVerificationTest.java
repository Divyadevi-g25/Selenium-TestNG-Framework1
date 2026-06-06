package com.orangehrm.test;

import java.util.Map;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DBConnection;
import com.orangehrm.utilities.DataProviders;
import com.orangehrm.utilities.ExtentManager;

public class DBVerificationTest extends BaseClass {

	// To initialize page classes create private variables
	private LoginPage loginPage;
	private HomePage homePage;

	@BeforeMethod
	public void setupPages() {

		// Create object of the pages
		loginPage = new LoginPage(getDriver());
		homePage = new HomePage(getDriver());
	}

	@Test(dataProvider="emplVerification", dataProviderClass = DataProviders.class)
	public void verifyEmployeeNameFromDB(String empID, String empName) {
		
		//Create soft assert object 
		SoftAssert softAssert = getSoftAssert();
		
		ExtentManager.logStep("Logging with Admin Credentials");
		loginPage.login(prop.getProperty("username"), prop.getProperty("password"));

		ExtentManager.logStep("Click on PIM Tab");
		homePage.clickOnPIMTab();

		ExtentManager.logStep("Search for Employee");
		homePage.employeeSearch(empName);

		ExtentManager.logStep("Get the Employee name from DB");
		String employee_id = empID;

		// Fetch the data into a map
		Map<String, String> employeeDetails = DBConnection.getEmployeeDetails(employee_id);

		String emplFirstName = employeeDetails.get("firstName");
		String emplMiddleName = employeeDetails.get("middleName");
		String emplLastName = employeeDetails.get("lastName");

		String emplFirstAndMiddleName = (emplFirstName + " " + emplMiddleName).trim();
		
		//Validation for first and middle name
		ExtentManager.logStep("Verify the Employee First and Middle Name");
		softAssert.assertTrue(homePage.verifyEmployeeFirstAndMiddleName(emplFirstAndMiddleName),
				"First and Middle Name are not matching");

		//Validation for last name
		ExtentManager.logStep("Verify the Employee Last Name");
		softAssert.assertTrue(homePage.verifyEmployeeLastName(emplLastName), "Last name is not matching");

		ExtentManager.logStep("DB validation completed");
		
		softAssert.assertAll(); //collect all failures

	}

}
