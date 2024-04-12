package com.speckyfox.mobiletest;

import org.junit.Assert;
import org.testng.annotations.Test;

import com.speckyfox.base.MobileBase;
import com.speckyfox.drivermanager.DriverManager;
import com.speckyfox.logmanager.LogsManager;

public class DemoTestMobile extends MobileBase {

	@Test
	public void OpenMobileInstance_MOBILE() {
		DriverManager.getDriver();
		LogsManager.getLogger().info("First Mobile Test Case");
	}

	@Test
	public void OpenMobileInstance_MOBILE2() {
		DriverManager.getDriver();
		LogsManager.getLogger().info("2nd Mobile Test Case");
		Assert.assertTrue("Issue in Mobile 2", false);
	}

}