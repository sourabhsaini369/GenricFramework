package com.speckyfox.driverfactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import com.speckyfox.enums.EnumConstants.WebRunMode;
import com.speckyfox.driver.IMobileDriver;
import com.speckyfox.driver.IWebDriver;
import com.speckyfox.driverImplementation.LocalMobileDriverImpl;
import com.speckyfox.driverImplementation.LocalWebDriverImpl;

import com.speckyfox.enums.EnumConstants.MobileRunMode;;

public final class DriverFactory {

	private DriverFactory() {
	}

	private static final Map<WebRunMode, Class<? extends IWebDriver>> WEB = new HashMap<>();
	private static final Map<MobileRunMode, Class<? extends IMobileDriver>> MOBILE = new HashMap<>();
	// private static final Map<RunModeType, IMobileDriver> MOBILE = new
	// EnumMap<>(RunModeType.class);

	static {
		WEB.put(WebRunMode.LOCAL, LocalWebDriverImpl.class);
//		WEB.put(WebRunMode.REMOTE, RemoteWebDriverImpl.class);
		MOBILE.put(MobileRunMode.LOCAL, LocalMobileDriverImpl.class);
//		MOBILE.put(MobileRunMode.REMOTE, RemoteMobileDriverImpl.class);
	}

	public static IWebDriver createObjectForWeb(WebRunMode key)
			throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
		Class<? extends IWebDriver> clazz = WEB.get(key);
		if (clazz != null) {
			try {
				// Find and call the default (no-argument) constructor
				Constructor<? extends IWebDriver> constructor = clazz.getDeclaredConstructor();
				return constructor.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static IMobileDriver createObjectForMobile(MobileRunMode key)
			throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
		Class<? extends IMobileDriver> clazz = MOBILE.get(key);
		if (clazz != null) {
			try {
				// Find and call the default (no-argument) constructor
				Constructor<? extends IMobileDriver> constructor = clazz.getDeclaredConstructor();
				return constructor.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static IWebDriver getDriverForWeb(WebRunMode runModeType)
			throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
		return createObjectForWeb(runModeType);

	}

	public static IMobileDriver getDriverForMobile(MobileRunMode runModeType)
			throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
		return createObjectForMobile(runModeType);
	}

}
