package com.speckyfox.driverImplementation;

import org.openqa.selenium.WebDriver;
import com.speckyfox.enums.EnumConstants.MobilePlatformType;

import com.speckyfox.driver.IMobileDriver;
import com.speckyfox.driver.MobileDriverData;
import com.speckyfox.driverfactory.LocalMobileDriverFactory;

public class LocalMobileDriverImpl implements IMobileDriver {

	@Override
	public WebDriver getDriver(MobileDriverData driverData) {
		return LocalMobileDriverFactory.getDriver(MobilePlatformType.valueOf(driverData.getMobilePlatformMode()));
	}
}
