package com.speckyfox.driverfactory;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

import org.openqa.selenium.WebDriver;

import com.speckyfox.drivermanager.AndroidManager;
import com.speckyfox.drivermanager.IosManager;
import com.speckyfox.enums.EnumConstants.MobilePlatformType;

public final class LocalMobileDriverFactory {

	private LocalMobileDriverFactory() {
	}

	private static final Map<MobilePlatformType, Supplier<WebDriver>> MAP = new EnumMap<>(MobilePlatformType.class);

	static {
		MAP.put(MobilePlatformType.ANDROID, () -> {
			try {
				return AndroidManager.getDriver();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		});
		MAP.put(MobilePlatformType.IOS, () -> {
			try {
				return IosManager.getDriver();
			} catch (MalformedURLException | URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		});
	}

	public static WebDriver getDriver(MobilePlatformType platformType) {
		return MAP.get(platformType).get();
	}

}
