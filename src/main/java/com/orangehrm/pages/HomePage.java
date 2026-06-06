package com.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;

public class HomePage {
	
	private ActionDriver actionDriver;  //declare variable to use action driver class

	
	//Define locators using By Class
	private By adminTab = By.xpath("//span[text()='Admin']");
	private By userIDButton = By.className("oxd-userdropdown-name");
	private By logoutButton = By.xpath("//a[text()='Logout']");
	private By orangeHRMLogo = By.xpath("//div[@class=\"oxd-brand-banner\"]//img");
	
	private By pimTab = By.xpath("//span[text()='PIM']");
	private By employeeSearch = By.xpath("//label[text()='Employee Name']/ancestor::div[contains(@class,'oxd-input-group')]//input");
	
	//label[text()='Employee Name']/parent::div/following-sibling::div/div/div/input
	private By searchButton = By.xpath("//button[@type='submit']");
	//private By emplFirstAndMiddleName = By.xpath("//div[@class='oxd-table-card']/div/div[3]");   
	private By emplFirstAndMiddleName = By.xpath("(//div[@class='oxd-table-card'])[\" + rowIndex + \"]//div[3]");
	
	
	//private By emplFirstAndMiddleName = By.xpath("By.xpath(\"//input[@placeholder='Type for hints...']\")");
	//private By emplLastName = By.xpath("//div[@class='oxd-table-card']/div/div[4]");
	private By emplLastName = By.xpath("(//div[@class='oxd-table-card'])[\" + rowIndex + \"]//div[4]");
	
	//To initialize action driver object, by passing webdriver instance
		/*public HomePage(WebDriver driver) {
			this.actionDriver = new ActionDriver(driver);    //Create object of Action driver class
		}*/
	
	public HomePage(WebDriver driver) {
		this.actionDriver = BaseClass.getActionDriver();
	}
	
	//Method to verify if Admin tab is visible	
		
	public boolean isAdminTabVisible() {
		
		return actionDriver.isDisplayed(adminTab);
	}
	
    public boolean verifyOrangeHRMLogo() {
		
		return actionDriver.isDisplayed(orangeHRMLogo);
	}
    
    //Method to navigate PIM tab
    public void clickOnPIMTab() {
    	actionDriver.click(pimTab);
    	
    }
    
    //Employee search
    public void employeeSearch(String value) {
    	actionDriver.enterTextAndSelect(employeeSearch, value);
    	actionDriver.click(searchButton);
    	actionDriver.waitForElementToBeVisible(emplFirstAndMiddleName);
    	actionDriver.scrollToElement(emplFirstAndMiddleName);
    	    	
    }
    
    //verify employee first and middle name
    public boolean verifyEmployeeFirstAndMiddleName(String emplFirstAndMiddleNameFromDB) {
    	return actionDriver.compareText(emplFirstAndMiddleName, emplFirstAndMiddleNameFromDB);
    }
    
    //verify employee last name
    public boolean verifyEmployeeLastName(String emplLastNameFromDB) {
    	return actionDriver.compareText(emplLastName, emplLastNameFromDB);
    }
    
    //Method to perform Logout operation
    
    public void logout() {
    	
    	actionDriver.click(userIDButton);
    	actionDriver.click(logoutButton);
    }
	

}
