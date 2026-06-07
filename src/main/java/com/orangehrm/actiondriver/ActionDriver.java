package com.orangehrm.actiondriver;

import java.time.Duration;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

public class ActionDriver {

	// variables declared
	private WebDriver driver;
	private WebDriverWait wait;
	public static final Logger logger = BaseClass.logger; // Instance of logger class

	//Test checking
	// constructor
	// initialize the variables
	public ActionDriver(WebDriver driver) {
		this.driver = driver;
		//int explicitWait = Integer.parseInt(BaseClass.getProp().getProperty("explicitWait"));
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		logger.info("Webdriver instance is created");
	}

	// Method to click an Element
	public void click(By by) {
		// String elementDescription = getElementDescription(by);
		try {
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(by));

			applyBorderSafe(element, "green");

			element.click();

			String elementDescription = by.toString();
			ExtentManager.logStep("Clicked an element: " + elementDescription);
			logger.info("Clicked an element--->" + elementDescription);
		} catch (Exception e) {
			// applyBorder(by, "red");
			logger.error("Unable to click the element:" + e.getMessage());
			ExtentManager.logFailure(BaseClass.getDriver(), "Unable to click the element: ", by.toString());
			throw e;
		}
	}

	// Method to enter text into an input field --Avoid code duplication
	public void enterText(By by, String value) {
		try {
			WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(by));

			applyBorderSafe(element, "green");

			// WebElement element = driver.findElement(by);
			element.clear();
			element.sendKeys(value);
			logger.info("Entered text on:" + getElementDescription(by) + "---> " + value);
		} catch (Exception e) {
			// applyBorderSafe(element, "red");
			logger.error("Unable to enter the value:" + e.getMessage());
			throw e;
		}
	}

	// Method to get text from an input field - old
	public String getText(By by) {
		try {
			WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(by));

			applyBorderSafe(element, "green");
			return element.getText();
		} catch (Exception e) {
			// applyBorderSafe(element, "red");
			logger.error("Unable to get the text:" + e.getMessage());
			throw e;
		}
	}

	// Method to get text from an input field - new
	/*
	 * public String getText(By by) { String elementDescription =
	 * getElementDescription(by); int attempts = 0; String text = "";
	 * 
	 * while (attempts < 3) { try { waitForElementToBeVisible(by);
	 * 
	 * WebElement element = driver.findElement(by);
	 * 
	 * // Apply border safely applyBorder(by, "green");
	 * 
	 * text = element.getText().trim();
	 * 
	 * logger.info("Fetched text from " + elementDescription + " ---> " + text);
	 * ExtentManager.logStep("Fetched text from " + elementDescription + " ---> " +
	 * text);
	 * 
	 * return text;
	 * 
	 * } catch (org.openqa.selenium.StaleElementReferenceException e) { attempts++;
	 * logger.warn("Stale element while getting text from " + elementDescription +
	 * " | Retry attempt: " + attempts);
	 * 
	 * } catch (org.openqa.selenium.NoSuchElementException e) {
	 * logger.error("Element not found: " + elementDescription + " | " +
	 * e.getMessage()); break;
	 * 
	 * } catch (Exception e) { logger.error("Unable to get text from " +
	 * elementDescription + " | " + e.getMessage()); break; } }
	 * 
	 * // Final failure handling try { applyBorder(by, "red"); } catch (Exception
	 * ignored) {}
	 * 
	 * ExtentManager.logFailure(BaseClass.getDriver(), "Get Text Failed",
	 * "Failed to fetch text from: " + elementDescription);
	 * 
	 * logger.error("Failed to fetch text after retries from " +
	 * elementDescription); return ""; }
	 */

	// method to handle auto suggestion
	public void enterTextAndSelect(By by, String expectedText) {
		try {
			waitForElementToBeVisible(by);

			WebElement input = driver.findElement(by);

			// ✅ Clear existing value
			input.clear();

			// ✅ Enter text
			input.sendKeys(expectedText);
			logger.info("Entered text: " + expectedText);

			// ✅ Wait for dropdown suggestions
			By suggestions = By.xpath("//div[@role='listbox']//span");

			wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(suggestions, 0));

			// ✅ Capture all options
			List<WebElement> options = driver.findElements(suggestions);

			for (WebElement option : options) {
				String actualText = option.getText().trim();

				if (actualText.toLowerCase().contains(expectedText.toLowerCase())) {
					option.click();
					logger.info("Selected dropdown value: " + actualText);
					return;
				}
			}

			throw new RuntimeException("No matching dropdown value found for: " + expectedText);
		}

		catch (Exception e) {
			logger.error("Dropdown selection failed: " + e.getMessage());
			throw e;
		}
	}

	// Method to compare Two text -- changed the return type
	public boolean compareText(By by, String expectedText) {
		try {
			WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
			String actualText = element.getText().trim();
			actualText = actualText.replaceAll("\\s+", " ");
			expectedText = expectedText.trim().replaceAll("\\s+", " ");
			if (expectedText.equals(actualText)) {
				applyBorderSafe(element, "green");
				logger.info("Texts are matching:" + actualText + " equals " + expectedText);
				ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Compare Text",
						"Text verified successfully! " + actualText + " equals " + expectedText);
				return true;
			} else {
				applyBorderSafe(element, "red");
				logger.error("Texts are not matching:" + actualText + " not equals " + expectedText);
				ExtentManager.logFailure(BaseClass.getDriver(), "Text Comparison Failed!",
						"Text comparison failed! " + actualText + " not equals " + expectedText);
				return false;
			}
		} catch (Exception e) {
			// applyBorderSafe(element, "red");
			logger.error("Unable to compare texts:" + e.getMessage());
		}
		return false;
	}

	/*
	 * method to check if an element is displayed public boolean isDisplayed(By by)
	 * { try { waitForElementToBeVisible(by); boolean isDisplayed =
	 * driver.findElement(by).isDisplayed(); if (isDisplayed) {
	 * System.out.println("Element is visible"); return isDisplayed; } else { return
	 * isDisplayed; } } catch (Exception e) {
	 * System.out.println("Element is not displayed:" + e.getMessage()); return
	 * false; } }
	 */

	// simplified the method and remove redundant conditions
	public boolean isDisplayed(By by) {
		try {
			WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
			applyBorderSafe(element, "green");
			logger.info("Element is displayed: " + by.toString());
			ExtentManager.logStep("Element is displayed: " + by.toString());
			ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Element is displayed: ",
					"Element is displayed: " + by.toString());
			return element.isDisplayed();

		} catch (Exception e) {
			// applyBorder(by, "red");
			logger.error("Element is not displayed:" + e.getMessage());
			ExtentManager.logFailure(BaseClass.getDriver(), "Element is not displayed: ",
					"Element is not displayed: " + by.toString());
			return false;

		}

	}

	

	// Wait for the page to load
	public void waitForPageLoad(int timeOutInSec) {
		try {
			// webdriver will pass one command to js executor and it completes
			wait.withTimeout(Duration.ofSeconds(timeOutInSec)).until(WebDriver -> ((JavascriptExecutor) WebDriver))
					.executeScript("return document.readyState").equals("complete");
			logger.info("Page loaded successfully.");
		} catch (Exception e) {
			logger.error("Page did not load within " + timeOutInSec + " seconds. Exception: " + e.getMessage());
		}
	}

	// Scroll to an element
	public void scrollToElement(By by) {
		try {
			WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
			applyBorderSafe(element, "green");
			JavascriptExecutor js = (JavascriptExecutor) driver;
			// WebElement element = driver.findElement(by);
			js.executeScript("arguments[0].scrollIntoView(true);", element);
		} catch (Exception e) {
			// applyBorder(by, "red");
			logger.error("Unable to locate element:" + e.getMessage());
			throw e;
		}
	}

	// wait for Element to be clickable
	public void waitForElementToBeClickable(By by) {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(by));
		} catch (Exception e) {
			logger.error("Element is not clickable:" + e.getMessage());

		}
	}

	// wait for Element to be visible
	public void waitForElementToBeVisible(By by) {
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		} catch (Exception e) {
			logger.error("Element is not visible:" + e.getMessage());
			throw e;
		}
	}

	// Method to get the description of an element using By locator
	public String getElementDescription(By locator) {
		// check for null driver or locator to avoid Nullpointer exception
		if (driver == null)
			return "driver is null";
		if (locator == null)
			return "Locator is null";

		try {
			// find the element using the locator
			WebElement element = driver.findElement(locator);

			// Get Element Attributes
			String name = element.getDomAttribute("name");
			String id = element.getDomAttribute("id");
			String text = element.getText();
			String className = element.getDomAttribute("class");
			String placeHolder = element.getDomAttribute("placeHolder");

			// Return the description based on element attributes
			if (isNotEmpty(name)) {
				return "Element with name:" + name;
			} else if (isNotEmpty(id)) {
				return "Element with id:" + id;
			} else if (isNotEmpty(text)) {
				return "Element with text:" + truncate(text, 50);
			} else if (isNotEmpty(className)) {
				return "Element with class:" + className;
			} else if (isNotEmpty(placeHolder)) {
				return "Element with placeHolder:" + placeHolder;
			}
		} catch (Exception e) {
			logger.error("Unable to describe the element" + e.getMessage());

		}
		return "Unable to describe the element";

	}

	// Utility method to check a string is not NULL or empty
	private boolean isNotEmpty(String value) {
		return value != null && !value.isEmpty();
	}

	// Utility method to truncate long string
	private String truncate(String value, int maxLength) {
		if (value == null || value.length() <= maxLength) {
			return value;
		}
		return value.substring(0, maxLength) + "....";
	}

	// Utility method to border an element
	public void applyBorderSafe(WebElement element, String color) {
		try {
			// Locate the element
			// WebElement element = driver.findElement(by);
			// Apply the border
			String script = "arguments[0].style.border='3px solid " + color + "'";
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript(script, element);
			// logger.info("Applied the border with the color " + color + " to element " +
			// getElementDescription(by));
		} catch (Exception e) {
			// logger.warn("Failed to apply the border to an element " +
			// getElementDescription(by), e.getMessage());
			logger.warn("Failed to apply border");

		}
	}

}
