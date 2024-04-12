package com.speckyfox.driver;

import org.openqa.selenium.WebDriver;



public interface IMobileDriver {
  WebDriver getDriver(MobileDriverData driverData);
}
