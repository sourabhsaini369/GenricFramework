package com.speckyfox.driverImplementation;

import com.speckyfox.enums.EnumConstants.WebBrowser;
import org.openqa.selenium.WebDriver;

import com.speckyfox.driver.IWebDriver;
import com.speckyfox.driver.WebDriverData;
import com.speckyfox.driverfactory.LocalWebDriverFactory;

public class LocalWebDriverImpl implements IWebDriver {

	@Override
	public WebDriver getDriver(WebDriverData driverData) {
		return LocalWebDriverFactory.getDriver(WebBrowser.valueOf(driverData.getWebBrowser().toUpperCase()));
	}
}
