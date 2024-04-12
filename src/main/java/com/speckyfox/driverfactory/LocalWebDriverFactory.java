package com.speckyfox.driverfactory;



import org.openqa.selenium.WebDriver;
import java.util.HashMap;
import java.util.Map;

import com.speckyfox.drivermanager.ChromeManager;
import com.speckyfox.drivermanager.EdgeManager;
import com.speckyfox.drivermanager.FirefoxManager;
import com.speckyfox.drivermanager.ILocalBrowserManager;
import com.speckyfox.enums.EnumConstants.WebBrowser;;


public final class LocalWebDriverFactory {

	private LocalWebDriverFactory() {
	}

	private static final Map<WebBrowser, Class<? extends ILocalBrowserManager>> MAP = new HashMap<>();

	static {
		MAP.put(WebBrowser.CHROME, ChromeManager.class);
		MAP.put(WebBrowser.FIREFOX, FirefoxManager.class);
		MAP.put(WebBrowser.EDGE, EdgeManager.class);
	}

	public static WebDriver createObject(WebBrowser WebBrowser) {
		try {
			Class<? extends ILocalBrowserManager> managerClass = MAP.get(WebBrowser);
			if (managerClass != null) {
				ILocalBrowserManager manager = managerClass.getDeclaredConstructor().newInstance();
				return manager.getDriver();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static WebDriver getDriver(WebBrowser WebBrowser) {
		return createObject(WebBrowser);

	}

}
