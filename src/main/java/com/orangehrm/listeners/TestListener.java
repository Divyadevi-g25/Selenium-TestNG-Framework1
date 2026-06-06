package com.orangehrm.listeners;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.testng.IAnnotationTransformer;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.RetryAnalyzer;

public class TestListener implements ITestListener, IAnnotationTransformer  {

	// Right click -> source -> click on override methods -> we can add below
	// methods
	
	@Override
	public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
		annotation.setRetryAnalyzer(RetryAnalyzer.class);
	}
	
	//Triggered when a test start
	@Override
	public void onTestStart(ITestResult result) {

		String testName = result.getMethod().getMethodName();
		// start logging in extent report
		ExtentManager.startTest(testName);
		ExtentManager.logStep("Test Started: " + testName);
	}

	// Triggered when a Test succeeds
	@Override
	public void onTestSuccess(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		
		if (!result.getTestClass().getName().toLowerCase().contains("api")) {
			ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Test passed successfully!","Test End: " + testName + " - Test Passed ");
		}
		else {
			ExtentManager.logStepValidationForAPI("Test End: " + testName + " - Test Passed ");
		}

	}

	// Triggered when a Test Fails
	@Override
	public void onTestFailure(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		String failureMessage = result.getThrowable().getMessage();
		ExtentManager.logStep(failureMessage);
		
		if (!result.getTestClass().getName().toLowerCase().contains("api")) {
			ExtentManager.logFailure(BaseClass.getDriver(), "Test Failed!", "Test End:" + testName + " - Test Failed ");
		}
		else {
			ExtentManager.logFailureAPI("Test End:" + testName + " - Test Failed ");
		}
		
	}

	// Triggered when a Test skips
	@Override
	public void onTestSkipped(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		ExtentManager.logSkip("Test Skipped " + testName);
	}

	// Triggered when suite starts
	@Override
	public void onStart(ITestContext context) {
		// Initialize the extent reports
		ExtentManager.getReporter();
	}

	// Triggered when suite ends
	@Override
	public void onFinish(ITestContext context) {
		// Flush the extent reports
		ExtentManager.endTest();
	}

}
