package com.wipro.android.search;


import org.testng.Assert;
import org.testng.annotations.Test;

import com.wipro.android.BaseTest;
import com.wipro.android.pages.CheckoutProduct;
import com.wipro.android.pages.HomeScreen;
import com.wipro.android.pages.LoginScreen;
import com.wipro.android.pages.SearchResultsScreen;
import com.wipro.android.testdata.TestParameters;

public class SearchProductTest extends BaseTest {
	
	@Test(dataProvider = "testData",retryAnalyzer=com.wipro.android.listeners.RetryListener.class)
	public void searchProduct(String testName, TestParameters test) throws InterruptedException {
		
		LoginScreen loginScreen = new LoginScreen(driver, logger);
		HomeScreen homeScreen = new HomeScreen(driver, logger);
		CheckoutProduct checkoutProduct = new CheckoutProduct(driver, logger);
		SearchResultsScreen searchScreen = new SearchResultsScreen(driver, logger);
		boolean result = true;
		String searchText = test.getSearchText();
		logger.info("searchProduct: Running Test to search for "+searchText);
		loginScreen.skipLogin();
		String actual = null;
		String expected = null;		
		int initialCartValue = Integer.parseInt(homeScreen.getCartValue());
		if(initialCartValue != 0) {
			logger.fail("searchProduct: product returned is empty");
			Assert.fail("Initial Cart Value is Empty");
		}
		result = result && homeScreen.searchItem(searchText);	
		expected = searchScreen.selectRandomItemFromList(searchText);
		logger.info("searchProduct: Expected product name is "+expected);
		if(expected == null) {
			logger.fail("searchProduct: product returned is empty");
			Assert.fail("searchProduct: product returned is empty");
		}			
		result = result && checkoutProduct.addItemToCart(initialCartValue);
		result = result && checkoutProduct.clickOnBasket();
		actual = checkoutProduct.getProductNameFromCheckoutScreen();
		logger.info("searchProduct: Actual product name in checkout screen is "+actual);
		if(!expected.contains(actual)) {
			logger.fail("searchProduct: product name in checkout screen is not matching with product selected");
			Assert.fail("Expected product value does not match actual product value");
		}
		Assert.assertTrue(result, "Test has failed");
		
		

	}

}
