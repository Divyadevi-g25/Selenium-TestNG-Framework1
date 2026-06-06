package com.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;

public class LoginPage {
	
	private ActionDriver actionDriver;
	
	//Define locators using By Class
	
	private By userNameField = By.name("username");
	private By passwordField = By.cssSelector("input[type='password']");
	//button xpath //*[@id="app"]/div[1]/div/div[1]/div/div[2]/div[2]/form/div[3]/button
	//to reduce the xpath length, we can use the same xpath as simple as below
	private By loginButton = By.xpath("//button[text() = ' Login ']");
	private By errorMessage = By.xpath("//p[text()='Invalid credentials']");    //*[@id=\"app\"]/div[1]/div/div[1]/div/div[2]/div[2]/div/div[1]/div[1]/p
	
	//To initialize action driver object, by passing webdriver instance
	/*public LoginPage(WebDriver driver) {
		this.actionDriver = new ActionDriver(driver);    //Create object of Action driver class
	}*/
	
	public LoginPage(WebDriver driver) {
		this.actionDriver = BaseClass.getActionDriver();
	}
	
	//Method to perform login
	public void login(String userName, String password) {
		actionDriver.enterText(userNameField, userName);
		actionDriver.enterText(passwordField, password);
		actionDriver.click(loginButton);
	}
	
	//Method to check if error message is displayed
	public boolean isErrorMessageDisplayed() {
		return actionDriver.isDisplayed(errorMessage);
	}

	//Method to get the text from error message
	public String getErrorMEssageText() {
		return actionDriver.getText(errorMessage);
	}
	
	//verify if error is correct or not 
	public boolean verifyErrorMessage(String expectedError) {
		return actionDriver.compareText(errorMessage, expectedError);
				
	}
	
}
