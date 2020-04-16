package com.wipro.android.utils;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wipro.reporting.Logger;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.touch.offset.PointOption;

/*
 * Constructor to initialize the AndroidActions class with driver object
 */
public abstract class AndroidActions {
	private AppiumDriver driver;
	private TouchAction action;
	private WebDriverWait wait;
	private Logger logger;

	public AndroidActions(AppiumDriver driver, Logger logger) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, 30);
		this.action = new TouchAction(driver);
		this.logger = logger;

	}

	/*
	 * Returns the centre point co-ordinates of the screen irrespective of screen
	 * size
	 */
	private PointOption getCentrePoint() {
		Dimension dimensions = driver.manage().window().getSize();
		int StartX = (int) (dimensions.getWidth() * 0.5);
		int startY = (int) (dimensions.getHeight() * 0.5);
		PointOption centre = new PointOption().withCoordinates(StartX, startY);
		return centre;

	}

	/*
	 * Returns the top point co-ordinates of the screen irrespective of screen size
	 */
	private PointOption getTopPoint() {
		Dimension dimensions = driver.manage().window().getSize();
		int startX = (int) (dimensions.getWidth() * 0.5);
		int startY = (int) (dimensions.getHeight() * 0.1);
		PointOption top = new PointOption().withCoordinates(startX, startY);
		return top;
	}

	/*
	 * Returns the bottom point co-ordinates of the screen irrespective of screen
	 * size
	 */
	private PointOption getBottomPoint() {
		Dimension dimensions = driver.manage().window().getSize();
		int startX = (int) (dimensions.getWidth() * 0.5);
		int startY = (int) (dimensions.getHeight() * 0.9);
		PointOption bottom = new PointOption().withCoordinates(startX, startY);
		return bottom;
	}

	/*
	 * utility function to scroll up the screen
	 */
	public void scrollUp() {
		PointOption centre = getCentrePoint();
		PointOption top = getTopPoint();
		action.longPress(centre).moveTo(top).release().perform();
	}

	/*
	 * utility function to scroll down the screen
	 */
	public void scrollDown() {
		PointOption centre = getCentrePoint();
		PointOption bottom = getBottomPoint();
		action.longPress(centre).moveTo(bottom).release().perform();
	}

	/*
	 * utility function to scroll to top of  the screen
	 */
	public boolean scrollToTopOfScreen(MobileElement el) {
		logger.info("scrollToTopOfScreen: Scrolling to top of the screen");
		boolean visible = false;
		int attempts = 0;
		while (!visible && attempts < 5) {
			try {
				wait.until(ExpectedConditions.visibilityOf(el));
				logger.info("scrollToTopOfScreen: element is visible");
				visible = true;
			} catch (ElementNotVisibleException e) {
				logger.info("scrollToTopOfScreen: Scrolling down a little to find element");
				scrollDown();
			} catch(Exception e) {
				logger.info("scrollToTopOfScreen: Exception thrown "+e.getMessage());
				scrollDown();
			}
			attempts++;
		}
		if(!visible) {
			logger.info("scrollToTopOfScreen: Element not found");
		}
		return visible;
				
	}
	
	/*
	 * utility function to scroll to the element passed as param
	 * 
	 * @param - Element to be scrolled to
	 */
	public boolean scrollToElement(MobileElement el) {
		logger.info("scrollToElement: Scroll to element"+el);
		boolean visible = false;
		int attempts = 0;
		while (!visible && attempts < 5) {
			try {
				wait.until(ExpectedConditions.visibilityOf(el));
				logger.info("scrollToElement: element is visible");
				visible = true;
			} catch (ElementNotVisibleException e) {
				logger.info("scrollToElement: Scrolling up a little to find element");
				scrollUp();
			} catch(Exception e) {
				logger.info("scrollToElement: Exception thrown "+e.getMessage());
				scrollUp();
			}
			attempts++;
		}
		if(!visible) {
			logger.info("scrollToElement: Element not found");
		}
		return visible;

	}

	/*
	 * utility function to click on an element
	 */
	public boolean clickOnElement(MobileElement el) {
		logger.info("clickOnElement: Clicking on element");
		if(verifyElementExists(el)) {
			el.click();
			return true;
		}
		else {
			logger.info("clickOnElement: Element not found to click");
			return false;
		}
		
	}
	
	/*
	 * utility function to check visibility on an element
	 */
	
	public boolean verifyElementExists(MobileElement el) {
		
		logger.info("verifyElementExists: checking if element is visible");
		try {
			wait.until(ExpectedConditions.visibilityOf(el));
			logger.pass("verifyElementExists: element is visible");
			return true;
		} catch(ElementNotVisibleException e) {
			
			logger.info("verifyElementExists: Element is not visible on page");
			return false;
		} catch(Exception e) {
			
			logger.info("verifyElementExists: Exception thrown checking for visiblity of element");
			return false;
		}

	}
	
	/*
	 * utility function to click on an element
	 */
	public boolean enterText(MobileElement el, String value) {
		logger.info("enterText: Entering text data");
		if(verifyElementExists(el)) {
			el.sendKeys(value);
			((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.ENTER));
			return true;
		}
		else {
			logger.info("enterText: Element not found to send data");
			return false;
		}
		
	}
}
