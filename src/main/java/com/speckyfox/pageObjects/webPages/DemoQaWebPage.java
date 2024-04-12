package com.speckyfox.pageObjects.webPages;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import com.speckyfox.utilities.commonUtilityFunctions.CommonUtils;

public class DemoQaWebPage {
	
	protected WebDriver driver;

	public DemoQaWebPage(WebDriver driver) {
		// TODO Auto-generated constructor stub
		PageFactory.initElements(driver, this);
		this.driver = driver;
	}
	
	
	public WebElement getAddBtn(int timeOut) {

		String xpath = "//button[@id='addNewRecordButton']";
		return CommonUtils.FindElement(driver, xpath, "Add button", timeOut);
	}

	public WebElement inputBoxRegistrationForm(String labelName, int timeOut) {
		String xpath = "//label[text()='"+labelName+"']/../..//input";
		return CommonUtils.FindElement(driver, xpath, labelName+" input box", timeOut);
	}	
	
	public WebElement getSubmitBtn(int timeOut) {

		String xpath = "//button[@id='submit']";
		return CommonUtils.FindElement(driver, xpath, "submit button", timeOut);
	}
	
	public List<WebElement> getWebTableRow() {

		String xpath = "//div[@class='rt-td' and text()!='']/..";
		return CommonUtils.FindElements(driver, xpath, "webtable rows");
	}
	

	public List<WebElement> getWebTableRowRecord(int rowNum) {

		String xpath = "(//div[@class='rt-tr-group']/div)["+rowNum+"]//div[@class='rt-td' and text()!='']";
		return CommonUtils.FindElements(driver, xpath, "webtable records");
	}

	

	
	
	
}
