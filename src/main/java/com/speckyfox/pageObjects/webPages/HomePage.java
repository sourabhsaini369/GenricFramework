package com.speckyfox.pageObjects.webPages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.speckyfox.drivermanager.DriverManager;
import com.speckyfox.utilities.commonUtilityFunctions.CommonUtils;

public class HomePage {

	protected WebDriver driver;

	public HomePage(WebDriver driver) {
		// TODO Auto-generated constructor stub
		PageFactory.initElements(driver, this);
		this.driver = driver;
	}

	public WebElement getTopHeaderCompanies(int timeOut) {

		String xpath = "//ul[@id=\"ContentNav\"]//a[text()=\"Companies\"]";
		return CommonUtils.FindElement(driver, xpath, "Top Header Companies", timeOut);

	}
}
