package com.speckyfox.drivermanager;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public final class FirefoxManager implements ILocalBrowserManager {

	public FirefoxManager() {
	}

	public WebDriver getDriver() {

		WebDriver firefoxDriver = new FirefoxDriver();
		return firefoxDriver;
	}
}
