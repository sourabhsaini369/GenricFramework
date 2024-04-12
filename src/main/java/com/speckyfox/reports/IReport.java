package com.speckyfox.reports;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.speckyfox.drivermanager.DriverManager;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

public interface IReport {

	void createTest(String method);

	ILogger getTestLogger();

	void closeReport();

	void setAppOrBrowserName();

	default String getAppOrBrowserName(WebDriver driver) {

		if (driver instanceof AndroidDriver) {

			return ((AndroidDriver) driver).getCapabilities().getCapability("appPackage").toString();
			// return "Android";
		}

		else if (driver instanceof IOSDriver) {
			return ((IOSDriver) driver).getCapabilities().getCapability("bundleId").toString();
			// return "IOS";
		}

		else if (driver instanceof WebDriver) {
			Capabilities caps = ((RemoteWebDriver) DriverManager.getDriver()).getCapabilities();
			return caps.getBrowserName();
		}

		else {
			return "API";
		}

	}

}
