package com.speckyfox.driverfactory;


import org.openqa.selenium.WebDriver;

import com.speckyfox.enums.EnumConstants.MobileRemoteModeType;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;

public final class RemoteMobileDriverFactory {

  private RemoteMobileDriverFactory() {
  }

//  private static final Map<MobileRemoteModeType, Function<MobilePlatformType, WebDriver>> MAP =
//    new EnumMap<>(MobileRemoteModeType.class);
//
//  static {
//    MAP.put(MobileRemoteModeType.SAUCE_LABS, SauceLabsMobileFactory::getDriver);
//    MAP.put(MobileRemoteModeType.BROWSER_STACK, BrowserStackMobileFactory::getDriver);
//  }
//
//  public static WebDriver getDriver(MobileRemoteModeType browserRemoteModeType,
//                                    MobilePlatformType platformType) {
//    return MAP.get(browserRemoteModeType).apply(platformType);
//  }
}
