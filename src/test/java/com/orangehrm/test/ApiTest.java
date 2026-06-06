package com.orangehrm.test;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.orangehrm.utilities.ApiUtility;
import com.orangehrm.utilities.ExtentManager;
//import com.orangehrm.utilities.RetryAnalyzer;

import io.restassured.response.Response;

public class ApiTest {
	
    @Test
	public void verifyGetUserAPI() {
    	
    	SoftAssert softAssert = new SoftAssert();

		// Step 1: Define API endpoint
		String endPoint = "https://jsonplaceholder.typicode.com/users/1";
		ExtentManager.logStep("API Endpoint: " + endPoint);

		// Step 2: Send Get Request
		ExtentManager.logStep("Sending Get Request to API");
		Response response = ApiUtility.sendGetRequest(endPoint);

		// Step 3: Validate the status code
		ExtentManager.logStep("Validating API Response status code");
		boolean isStatusCodeValid = ApiUtility.validateStatusCode(response, 200);

		softAssert.assertTrue(isStatusCodeValid, "Status code is not expected");

		if (isStatusCodeValid) {
			ExtentManager.logStepValidationForAPI("Status code validation Passed!");
		} else {
			ExtentManager.logFailureAPI("Status code validation Failed!");
		}

		// Step 4: Validate user name
		ExtentManager.logStep("Validating response body for user name");
		String userName = ApiUtility.getJsonValue(response, "username");
		boolean isUserNameValid = "Bret".equals(userName);
		softAssert.assertTrue(isUserNameValid, "Username is not valid");

		if (isUserNameValid) {
			ExtentManager.logStepValidationForAPI("User name validation Passed!");

		} else {
			ExtentManager.logFailureAPI("User name validation Failed!");
		}

		// Step 5: Validate email
		ExtentManager.logStep("Validating response body for email");
		String userEmail = ApiUtility.getJsonValue(response, "email");
		boolean isEmailValid = "Sincere@april.biz".equals(userEmail);
		softAssert.assertTrue(isEmailValid, "Email is not valid");

		if (isEmailValid) {
			ExtentManager.logStepValidationForAPI("Email validation Passed!");

		} else {
			ExtentManager.logFailureAPI("Email validation Failed!");
		}
		
		softAssert.assertAll();

	}

}
