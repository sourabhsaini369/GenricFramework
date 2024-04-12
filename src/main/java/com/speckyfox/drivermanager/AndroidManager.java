package com.speckyfox.drivermanager;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import org.openqa.selenium.WebDriver;

import com.speckyfox.constants.ConfigConstants;
import com.speckyfox.constants.PathConstants;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

public final class AndroidManager {

	private AndroidManager() {
	}

	public static WebDriver getDriver() throws MalformedURLException, URISyntaxException {
		UiAutomator2Options options = new UiAutomator2Options();
		options.setApp(PathConstants.androidAppPath);

		return new AndroidDriver(new URI(ConfigConstants.appiumServerURL).toURL(), options);
	}
}
