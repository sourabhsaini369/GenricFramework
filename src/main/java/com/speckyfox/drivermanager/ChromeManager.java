package com.speckyfox.drivermanager;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public final class ChromeManager implements ILocalBrowserManager {

	public ChromeManager() {
	}

	public WebDriver getDriver() {

		WebDriver chromeDriver = new ChromeDriver();
		return chromeDriver;
	}
}