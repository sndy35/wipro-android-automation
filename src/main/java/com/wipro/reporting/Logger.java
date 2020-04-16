package com.wipro.reporting;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class Logger {
	private static ExtentReports report = new ExtentReports("./ExtentReport.html",true);
	private ExtentTest test;
	
	public void startTest(String testname) {
		test=report.startTest(testname);
	}
	public void endTest() {
		report.endTest(test);
		report.flush();
	}
	
	public void pass(String message) {
		test.log(LogStatus.PASS, message);
	}
	
	public void fail(String message) {
		test.log(LogStatus.FAIL, message);
	}
	
	public void info(String message) {
		test.log(LogStatus.INFO, message);
	}
	
	public void fatal(String message) {
		test.log(LogStatus.FATAL, message);
	}
	
	
	

}
