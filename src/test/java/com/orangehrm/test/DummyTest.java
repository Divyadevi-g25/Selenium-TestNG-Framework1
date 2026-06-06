package com.orangehrm.test;

import org.testng.SkipException;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

public class DummyTest extends BaseClass{
	
	@Test
		public void dummyTest() {
		
		// ExtentManager.startTest("DummyTest1 test"); //this has been implemented in test listener
		String title = getDriver().getTitle();
		ExtentManager.logStep("Verifying the title");
		assert title.equals("OrangeHRM"):"Test Failed - Title is not matching";
		System.out.println("Test Passed - Title is matching");
		ExtentManager.logSkip("This case is skipped");
		throw new SkipException("Skipping the test as part of testing");
		
	}

}
