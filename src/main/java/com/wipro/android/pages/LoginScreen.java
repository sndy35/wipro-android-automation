package com.wipro.android.pages;

import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wipro.android.utils.AndroidActions;
import com.wipro.reporting.Logger;

import io.appium.java_client.AppiumDriver;

import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class LoginScreen extends AndroidActions{
	private AppiumDriver driver;
	private WebDriverWait wait;
	private Logger logger;

	@AndroidFindBy(xpath = "//android.widget.Button[@text='Skip sign in']")
	private AndroidElement skipSignInButton;

	@AndroidFindBy(xpath = "//android.widget.Button[contains(@text,'customer')]")
	private AndroidElement signInButton;
	
	@AndroidFindBy(xpath= "//android.widget.EditText[@resource-id='ap_email_login']")
	private AndroidElement usrInputBox;
	
	@AndroidFindBy(xpath= "//android.widget.EditText[@resource-id='ap_password']")
	private AndroidElement pwdInputBox;
	
	@AndroidFindBy(xpath= "//android.widget.Button[@resource-id='continue']")
	private AndroidElement continueBtn;
	
	@AndroidFindBy(xpath = "//android.widget.Button[@resource-id='signInSubmit']")
	private AndroidElement submitBtn;
	/*
	 * Constructor to initialize the loginScreen with driver
	 */
	public LoginScreen(AppiumDriver driver, Logger logger) {
		super(driver,logger);
		this.driver = driver;
		this.wait = new WebDriverWait(driver, 30);
		this.logger = logger;
		PageFactory.initElements(new AppiumFieldDecorator(driver), this);
	}

	/*
	 * This function skips the login and takes to the home screen of the app
	 * @return boolean indicating if the login is successful
	 */
	public boolean skipLogin() {
		logger.info("skipLogin: skipping Login");
		if(clickOnElement(skipSignInButton)) {
			logger.pass("skipLogin: Successfully clicked on skip login button");
			return true;
		} else {
			logger.fail("skipLogin: login is not successful");
			return false;
		}
		
	}
	
	/*
	 * This function will login to app using the credentials passed
	 * @param userName username of the user to be logged in
	 * @param passwordEncrypted encrypted password of the user to be logged in
	 * @return boolean indicating if the login is successful
	 */
	public boolean login(String userName, String passwordEncrypted) {
		logger.info("login:  Login to app with user "+userName);
		try {
			logger.info("login: Clicking on sign in button");
			clickOnElement(signInButton);
			logger.info("login: Entering username");
			enterText(usrInputBox, userName);
			logger.info("login: Clicking on Continue button");
			clickOnElement(continueBtn);
			logger.info("login: Entering passwords");
			enterText(pwdInputBox, passwordEncrypted);
			logger.info("login:  Clicking on submit button");
			clickOnElement(submitBtn);
			logger.info("login: login successful");
			return true;
		} catch(Exception e) {
			logger.info("login: Exception thrown in login"+e.getMessage());
		}
		return false;
	}
	
	

}
