package com.orangehrm.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.asserts.SoftAssert;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.LoggerManager;



public class BaseClass {
	
	protected static Properties prop; 
	//protected static WebDriver driver;
	//private static ActionDriver actionDriver;
	
	// create driver and action driver variables by using object of thread local
	
	private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	private static ThreadLocal<ActionDriver> actionDriver = new ThreadLocal<>();
	public static final Logger logger = LoggerManager.getLogger(BaseClass.class);
	
	//To use the object within the same package
	protected ThreadLocal<SoftAssert> softAssert = ThreadLocal.withInitial(SoftAssert::new);
	
	//To access the softassert object in other class - getter method
	public SoftAssert getSoftAssert() {
		return softAssert.get();
	}
	
	
	
//It can be access within same package. If other package wants to access the base class should be extended by other class
	//if file is not found it throws exception
	//if file is not read throws io
	
	@BeforeSuite
	public void loadconfig() throws IOException {
		
		//Load the config file. so create the object 
    	prop = new Properties();
    	FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/config.properties");
    	prop.load(fis);
    	logger.info("config.properties file loaded");
    	
    	//start the extent report
    	//ExtentManager.getReporter(); // This has been implemented in testlistener
		
	}
	
	//synchronized - only one method execute at time in thread
	@BeforeMethod
    public synchronized void setup() throws IOException {
    	
		System.out.println("Setting up webDriver for:"+this.getClass().getSimpleName());
		launchBrowser();
		configureBrowser();
		staticWait(2);
    	logger.info("WebDriver Initialized and Browser MAximized");
    	logger.trace("This is a Trace message");
    	logger.error("This is a error message");
    	logger.debug("This is a debug message");
    	logger.fatal("This is a fatal message");
    	logger.warn("This is a warn message");
    	
		
		//initialize actiondriver only once
		/*if (actionDriver == null) {
			actionDriver = new ActionDriver(driver);
			logger.info("ActionDriver instance is created. "+Thread.currentThread().getId());
		}*/
    	
    	//Initialize action driver for the current thread
    	actionDriver.set(new ActionDriver(getDriver()));
    	logger.info("ActionDriver initialized for thread: "+ Thread.currentThread().getId());//.getId() = .threadId()
    	
    }
	
	//Initialize web driver based on browser defined in config file
	private synchronized void launchBrowser() {
		
    	String browser = prop.getProperty("browser");
    	
    	if(browser.equalsIgnoreCase("chrome")) {
    		
    		//Create ChromeOptions
    		ChromeOptions options = new ChromeOptions();
    		 boolean isHeadless = false;
			 if (isHeadless) {
    		        options.addArguments("--headless=new"); //Run chrome in headless mode
    		        options.addArguments("--window-size=1920,1080"); //set window size
    		        options.addArguments("--disable-notifications"); //Disable browser notifications
    		    }
    		
    		//options.addArguments("--disable-gpu"); //Disable gpu for headless mode
    		//options.addArguments("--window-size=1920,1080"); 
    		//
    		//options.addArguments("--no-sandbox"); //Required for some CI environments like
    		//options.addArguments("--disable-dev-shm-usage"); //Resolve issues in resources
    		//driver = new ChromeDriver(options);
    		//driver = new ChromeDriver();
    		driver.set(new ChromeDriver());  //new changes as per thread
    		ExtentManager.registerDriver(getDriver());
    		logger.info("ChromeDriver Instance is created");
    	}
    	
    	else if(browser.equalsIgnoreCase("Firefox")) {
    		
    		//Create FirefoxOptions
    		FirefoxOptions options = new FirefoxOptions();
    		options.addArguments("--headless"); //Run Firefox in headless mode
    		options.addArguments("--disable-gpu"); //Disable gpu rendering useful for headless mode
    		options.addArguments("--width=1920"); //set browser width
    		options.addArguments("--height=1080"); //set browser height
    		options.addArguments("--disable-notifications"); //Disable browser notifications
    		options.addArguments("--no-sandbox"); //Needed for CI/CD environments
    		options.addArguments("--disable-dev-shm-usage"); //Prevent crashes in low-resources
    		
    		//driver = new FirefoxDriver();
    		driver.set(new FirefoxDriver());  //new changes as per thread
    		ExtentManager.registerDriver(getDriver());
    		logger.info("FirefoxDriver Instance is created");
    	}
    	
    	else if(browser.equalsIgnoreCase("edge")) {
    		
    		//Create EdgeOptions
    		EdgeOptions options = new EdgeOptions();
    		options.addArguments("--headless=new"); //Run edge in headless mode
    		options.addArguments("--disable-gpu"); //Disable gpu acceleration
    		options.addArguments("--window-size=1920,1080"); //set window size
    		options.addArguments("--disable-notifications"); //Disable pop-up notifications
    		options.addArguments("--no-sandbox"); //Needed for CI/CD environments
    		options.addArguments("--disable-dev-shm-usage"); //Prevent resource-limited
    		
    		//driver = new EdgeDriver();
    		driver.set(new EdgeDriver());   //new changes as per thread
    		ExtentManager.registerDriver(getDriver());
    		logger.info("EdgeDriver Instance is created");
       	}
    	
    	else {
    		throw new IllegalArgumentException("Browser not supported");
    	}
		
	}
	
	//configure browser settings
	private void configureBrowser() {
	
		//ImplicitWait - global wait
    	//int implicitWait = Integer.parseInt(prop.getProperty("implicitWait"));
    	getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
    	
    	//maximize browser
    	
    	getDriver().manage().window().maximize();
    	
    	//Navigate to URL
    	
    	try {
    		getDriver().get(prop.getProperty("url"));
    		//wait for page load
    		 new WebDriverWait(getDriver(), Duration.ofSeconds(20))
             .until(webDriver -> ((JavascriptExecutor) webDriver)
                 .executeScript("return document.readyState").equals("complete"));
    		 
    		 //wait for login page - very important
    		// new WebDriverWait(getDriver(), Duration.ofSeconds(15)).until(ExpectedConditions.visibilityOfElementLocated(By.name("username")));
		} catch (Exception e) {
			System.out.println("Failed to navigate to URL:"+e.getMessage());
			throw e;
		}
		
	}
	
	//driver.get() = getDriver()
	@AfterMethod
    public synchronized void tearDown() {
    	if(getDriver() != null) {
    		try {
    			getDriver().quit();   //close all active sessions
			} catch (Exception e) {
				System.out.println("Unable to close the driver:"+e.getMessage());
			} 
    	}
    	logger.info("WebDriver instance is closed");
    	driver.remove();
    	actionDriver.remove();
    	//driver = null;
    	//actionDriver = null;
    	//ExtentManager.endTest();  //this has been implemented in test listener
    }
	
	
	
	//By using getter setter, we can access driver from outside the class also
	//Driver getter method
	/*public WebDriver getDriver() {
		return driver;
	}*/
	
	//Getter method for webdriver
	public static WebDriver getDriver() {
		
		if(driver.get()==null) {
			System.out.println("WebDriver is not initialized");
			throw new IllegalStateException("WebDriver is not initialized");
			
		}
		return driver.get();
		
	}
	
	//Getter method for Action driver
		public static ActionDriver getActionDriver() {
			
			if(actionDriver.get() == null) {
				System.out.println("ActionDriver is not initialized");
				throw new IllegalStateException("ActionDriver is not initialized");
				
			}
			return actionDriver.get();
			
		}
		
	//Getter Method for prop
		public static Properties getProp() {
			return prop;
		}
	
	//Driver Setter method
	public void setDriver(ThreadLocal<WebDriver> driver) {
		this.driver = driver;
	}
	
	//static wait for pause
	public void staticWait(int seconds) {
		LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
	}
}
