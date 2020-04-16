package com.wipro.android.pages;

import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wipro.android.utils.AndroidActions;
import com.wipro.reporting.Logger;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class CheckoutProduct extends AndroidActions{
	private AppiumDriver driver;
	private WebDriverWait wait;
	private Logger logger;
	
	@AndroidFindBy(xpath = "//android.widget.Button[contains(@text,'Add to Cart')]")
	private AndroidElement addTocartButton;
	
	@AndroidFindBy(xpath = "//android.widget.Button[contains(@text,'See All Buying Options')]")
	private AndroidElement buyingOptionsButton;

	@AndroidFindBy(id = "com.amazon.mShop.android.shopping:id/action_bar_cart_count")
	private AndroidElement cartIcon;
	
	@AndroidFindBy(id = "glow_subnav_label")
	private AndroidElement addressSelection;

	@AndroidFindBy(xpath = "//android.widget.Button[@text='Proceed to Checkout']")
	private AndroidElement checkoutButton;
	
	
	@AndroidFindBy(xpath = "//android.widget.Image[@class='android.widget.Image']")
	private AndroidElement ItemDetailsImage;
	
	@AndroidFindBy(xpath = "//android.widget.Button[@text='Proceed to Checkout']")
	private AndroidElement deliverButton;

	/*
	 * Constructor to initialize the CheckoutProduct class with driver object
	 */
	public CheckoutProduct(AppiumDriver driver, Logger logger) {
		super(driver,logger);
		this.driver = driver;
		this.wait = new WebDriverWait(driver, 30);
		this.logger = logger;
		PageFactory.initElements(new AppiumFieldDecorator(driver), this);
	}

	/*
	 * Function add Item to the cart
	 */
	public boolean addItemToCart(int currentCartValue) {
		logger.info("addItemToCart: Adding item to cart");
		if(!checkBuyingOptions()) {
			scrollToTopOfScreen(addressSelection);
		}
		scrollToElement(addTocartButton);
		if(clickOnElement(addTocartButton)) {
			logger.info("addItemToCart: Clicked on Add to Cart Button");
			logger.info("addItemToCart: Verifying if the cart value is increased by 1");
			if(verifyElementExists(cartIcon)) {
				if(cartIcon.getText().equalsIgnoreCase(Integer.toString(currentCartValue+1))) {
					logger.info("addItemToCart: Item added to cart successfully");
					return true;
				} else {
					logger.fail("addItemToCart: Item not added to cart");
					return false;
				}
			} else {
				logger.fail("addItemToCart: cartIcon is not visible on screen");
				return false;
			}
		}
		
		return false;
	}
	
	
	
	/*
	 * Function to check buying options is visible
	 */
	private boolean checkBuyingOptions() {
		logger.info("checkBuyingOptions: checking for buying options button");
		if(scrollToElement(buyingOptionsButton)) {
			logger.info("checkBuyingOptions: buying options button is visible");
			if(clickOnElement(buyingOptionsButton)) {
				logger.info("checkBuyingOptions: clicked on buying options button successfully");
				return true;
			} else {
				logger.info("checkBuyingOptions: click on buying options failed");
				return false;
			}
		}
		
		return false;
	}

	/*
	 * Function to click on basket once the item is added to cart
	 */
	public boolean clickOnBasket() {
		logger.info("clickOnBasket: Clicking on the basket icon");
		if(clickOnElement(cartIcon)) {
			logger.info("clickOnBasket: successfully clicked on basket icon");
			return true;
		} else {
			logger.info("clickOnBasket: click action failed on basket icon");
			return false;
		}	
	}
	
	/*
	 * Function to get the name of the product added in the cart
	 */
	public String getProductNameFromCheckoutScreen() {
		logger.info("getProductNameFromCheckoutScreen: Getting product name from checkout screen");
		if(verifyElementExists(ItemDetailsImage)) {
			logger.info("getProductNameFromCheckoutScreen: Product details found on page");
			return ItemDetailsImage.getAttribute("text");	
		}
		logger.info("getProductNameFromCheckoutScreen: Product details image not found on page");
		return null;
		
	}
	
	/*
	 * Function to checkout the cart
	 */
	public boolean checkout() {
		logger.info("checkout: Checking out cart");
		if(clickOnElement(checkoutButton)) {
			logger.info("checkout: Successfully checkedout cart");
			return true;
		}
		logger.info("checkout: unable to checkout cart");
		return false;
	
	}
}
