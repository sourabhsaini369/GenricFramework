package com.speckyfox.reports;

import java.util.Optional;

import org.testng.*;



public interface ILogger {

	void info(String message);

	void warning(String message);

	public void logTestStatus(ITestResult status, Optional<String> message, Optional<Boolean> screenshot);

}
