package com.wipro.android.pages;

import java.util.ArrayList;
import java.util.List;


import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wipro.android.utils.AndroidActions;
import com.wipro.reporting.Logger;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class SearchResultsScreen extends AndroidActions {
	private AppiumDriver driver;
	private WebDriverWait wait;
	private Logger logger;

	@AndroidFindBy(id = "add-to-cart-button")
	private AndroidElement addToCartButton;

	@AndroidFindBy(xpath = "//android.view.View[1]/android.view.View")
	private List<AndroidElement> searchResults;
	
	private String xPathValidResult = "//android.view.View/android.view.View/android.view.View[@text='%s']";

	/*
	 * Constructor to initialize the SearchResultsScreen with driver
	 */
	public SearchResultsScreen(AppiumDriver driver, Logger logger) {
		super(driver,logger);
		this.driver = driver;
		this.wait = new WebDriverWait(driver, 30);
		this.logger = logger;
		PageFactory.initElements(new AppiumFieldDecorator(driver), this);
	}

	/*
	 * This function selects the items randomly from list of items in search screen
	 * @param searchText item to be searched in the application
	 * @return String name of the product selected from results
	 */
	public String selectRandomItemFromList(String searchText) {
		logger.info("selectRandomItemFromList: selecting random item from list of results");
		verifyElementExists(searchResults.get(0));
		int results = searchResults.size();
		logger.info("selectRandomItemFromList: Total number of search results "+results);
		if(results > 0) {
			List<String> validItems = new ArrayList<String>();
			for (MobileElement el : searchResults) {
				String txtAttribute = el.getAttribute("text");
				if(txtAttribute.contains("TV")) {
					validItems.add(String.format(xPathValidResult, txtAttribute));
				}
			}
			int validResults = validItems.size();
			logger.info("selectRandomItemFromList: Valid search results "+validResults);
			logger.info("selectRandomItemFromList: Selecting random item from search list");
			int randomIndex = (int)(Math.random()*validResults);
			MobileElement searchItem = (MobileElement) driver.findElementByXPath(validItems.get(randomIndex));
			scrollToElement(searchItem);
			String productName = searchItem.getAttribute("text");
			driver.findElementByXPath(validItems.get((int)(Math.random()*validItems.size()))).click();
			logger.info("selectRandomItemFromList: Selected Item "+searchText);
			return productName;
		} else {
			logger.info("selectRandomItemFromList: No search results found for "+searchText);
			return null;
		}
		
		
	}

}
