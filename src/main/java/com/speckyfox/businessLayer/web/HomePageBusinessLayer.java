package com.speckyfox.businessLayer.web;

import java.time.Duration;

import org.openqa.selenium.WebDriver;

import com.speckyfox.drivermanager.DriverManager;
import com.speckyfox.pageObjects.webPages.HomePage;
import com.speckyfox.utilities.commonUtilityFunctions.CommonUtils;

public class HomePageBusinessLayer extends HomePage {

	
	public HomePageBusinessLayer(WebDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}
	public void clickOnTopPageHeaderCompany() {

		CommonUtils.click(DriverManager.getDriver(), getTopHeaderCompanies(10), "Company Header",
				Duration.ofSeconds(10));

	}
}
