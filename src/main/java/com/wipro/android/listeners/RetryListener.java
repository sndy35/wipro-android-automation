package com.wipro.android.listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryListener implements IRetryAnalyzer{

	int retries = 2;
	int count = 0;
	@Override
	public boolean retry(ITestResult result) {
		if(count < retries) {
			count++;
			return true;
		}
		return false;
	}

}
