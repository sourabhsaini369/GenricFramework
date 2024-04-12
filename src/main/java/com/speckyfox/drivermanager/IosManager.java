package com.speckyfox.drivermanager;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import org.openqa.selenium.WebDriver;

import com.speckyfox.constants.ConfigConstants;
import com.speckyfox.constants.PathConstants;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;

public final class IosManager {

	private IosManager() {
	}

	public static WebDriver getDriver() throws MalformedURLException, URISyntaxException {
		XCUITestOptions options = new XCUITestOptions();
		options.setDeviceName("iPhone 13 Pro Max").setApp(PathConstants.iosAppPath);
		return new IOSDriver(new URI(ConfigConstants.webRemoteModeType).toURL(), options);
	}
}
