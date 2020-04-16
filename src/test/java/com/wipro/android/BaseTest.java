package com.wipro.android;


import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;

import com.wipro.android.testdata.Test;
import com.wipro.android.testdata.TestParameters;
import com.wipro.android.testdata.Testdata;
import com.wipro.reporting.Logger;
import com.wipro.utils.CommandLineParameters;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

public class BaseTest {

	protected AppiumDriver<AndroidElement> driver;
	protected Logger logger;
	protected Testdata testData;

	@BeforeClass(alwaysRun = true)
	@Parameters({ "xmlFile" })
	public void setUp(String xmlFile) throws Exception {
		logger = new Logger();
		parseXmlFileData(xmlFile);
		initializeDriver();

	}

	private void parseXmlFileData(String xmlFile) {
		try {
			if (xmlFile == null || xmlFile.isEmpty()) {
				logger.info("parseXmlFileData: XML test data file name is " + xmlFile);
			}
			File file = new File("src/main/resources/testdata/" + xmlFile);
			JAXBContext jaxbContext = JAXBContext.newInstance(Testdata.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			testData = (Testdata) jaxbUnmarshaller.unmarshal(file);
		} catch (Exception e) {
			 logger.fail("parseXmlFileData: error in parsing xml test data");
		}
	}

	private void initializeDriver() throws MalformedURLException {
		try {
			String ip = System.getProperty(CommandLineParameters.APPIUM_SERVER) != null
					? System.getProperty(CommandLineParameters.APPIUM_SERVER)
					: "127.0.0.1";
			String port = System.getProperty(CommandLineParameters.APPIUM_PORT) != null
					? System.getProperty(CommandLineParameters.APPIUM_PORT)
					: "4723";
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability("platformName", System.getProperty(CommandLineParameters.PLATFORM));
			capabilities.setCapability(CapabilityType.VERSION,
					System.getProperty(CommandLineParameters.PLATFORM_VERSION));
			capabilities.setCapability("deviceName", System.getProperty(CommandLineParameters.DEVICE_NAME));
			capabilities.setCapability("app", System.getProperty(CommandLineParameters.APP));
			capabilities.setCapability("appPackage", System.getProperty(CommandLineParameters.APP_PACKAGE));
			capabilities.setCapability("appActivity", System.getProperty(CommandLineParameters.APP_ACTIVITY));
			capabilities.setCapability("newCommandTimeout",
					System.getProperty(CommandLineParameters.NEW_COMMAND_TIMEOUT));
			driver = new AndroidDriver<>(new URL("http://" + ip + ":" + port + "/wd/hub"), capabilities);
		
		} catch(Exception e) {
			logger.fail("initializeDriver: Error launching driver "+e.getMessage());
		}
	}

	@DataProvider(name = "testData")
	public Object[][] getTestData() {
		Map<String, TestParameters> tests = parseXmlDataProvider(testData);
		Object[][] output = new Object[tests.size()][2];
		Iterator<Map.Entry<String, TestParameters>> entries = tests.entrySet().iterator();
		int i = 0;
		while (entries.hasNext()) {
			Map.Entry<String, TestParameters> entry = entries.next();
			output[i][0] = entry.getKey();
			output[i][1] = entry.getValue();
			i++;
		}

		return output;
	}

	private Map<String, TestParameters> parseXmlDataProvider(Testdata testData) {
		Map<String, TestParameters> testsGenerated = new HashMap<String, TestParameters>();
		for (Test data : testData.getTest()) {
			TestParameters testParams = new TestParameters();
			testParams.setCategory(data.getCategory().toString());
			testParams.setSearchText(data.getSearchText().toString());
			testsGenerated.put(data.getName(), testParams);
		}
		return testsGenerated;
	}

	@AfterMethod
	public void closeApp(ITestResult result, ITestContext ctx) {
		if(result.getStatus() == ITestResult.SUCCESS) {
			logger.pass("Test has run successfully");			
		}
		logger.endTest();
		((AppiumDriver)driver).resetApp();		
	}

	@BeforeMethod(alwaysRun = true)
	public void handleTestMethodName(ITestContext ctx, Object[] data) {
		logger.startTest(ctx.getCurrentXmlTest().getName()+data[0].toString());
		try {
			int attempts = 0;
			while (driver == null || driver.getSessionId() == null) {
				if (attempts < 10) {
					Thread.sleep(5000);
					initializeDriver();
				} else {
					break;
				}
				attempts++;
			}
		} catch (Exception e) {
			logger.info("BeforeMethod: Error launching driver");
		}
	}

	@AfterSuite
	public void afterSuite() {
		 driver.quit();

	}

}
