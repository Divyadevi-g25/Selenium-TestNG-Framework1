package com.orangehrm.test;

import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

public class DummyTest2 extends BaseClass{
	
	@Test
		public void dummyTest2() {
		
		//ExtentManager.startTest("DummyTest2 test"); //this has been implemented in test listener
		String title = getDriver().getTitle();
		ExtentManager.logStep("Verifying the title");
		assert title.equals("OrangeHRM"):"Test Failed - Title is not matching";
		
		System.out.println("Test Passed - Title is matching");
		ExtentManager.logStep("Validation successful");
		
	}

}
