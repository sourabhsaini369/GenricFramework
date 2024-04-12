package com.speckyfox.drivermanager;

import static com.speckyfox.enums.EnumConstants.PlatformType;
import static java.lang.ThreadLocal.withInitial;

import java.util.EnumMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

public class DriverManager {

	private DriverManager() {
	}

	private static final ThreadLocal<WebDriver> WEB_DRIVER_THREAD_LOCAL = new ThreadLocal<>();
	private static final ThreadLocal<WebDriver> MOBILE_DRIVER_THREAD_LOCAL = new ThreadLocal<>();
	private static final ThreadLocal<PlatformType> CONTEXT = withInitial(() -> PlatformType.WEB);
	private static final Map<PlatformType, ThreadLocal<WebDriver>> DRIVER_MAP = new EnumMap<>(PlatformType.class);

	public static WebDriver getDriver() {
		return CONTEXT.get() == PlatformType.WEB ? WEB_DRIVER_THREAD_LOCAL.get() : MOBILE_DRIVER_THREAD_LOCAL.get();
	}

	public static void setDriver(WebDriver driver) {
		if (isMobileDriver(driver)) {
			MOBILE_DRIVER_THREAD_LOCAL.set(driver);
			DRIVER_MAP.put(PlatformType.MOBILE, MOBILE_DRIVER_THREAD_LOCAL);
			CONTEXT.set(PlatformType.MOBILE);
		} else {
			WEB_DRIVER_THREAD_LOCAL.set(driver);
			DRIVER_MAP.put(PlatformType.WEB, WEB_DRIVER_THREAD_LOCAL);
			CONTEXT.set(PlatformType.WEB);
		}
	}

	private static boolean isMobileDriver(WebDriver driver) {
		return driver instanceof AndroidDriver || driver instanceof IOSDriver;
	}

	public static void unload() {
		WEB_DRIVER_THREAD_LOCAL.remove();
		MOBILE_DRIVER_THREAD_LOCAL.remove();
		CONTEXT.remove();
	}
}
