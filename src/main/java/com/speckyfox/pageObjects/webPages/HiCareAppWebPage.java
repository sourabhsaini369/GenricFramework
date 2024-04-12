package com.speckyfox.pageObjects.webPages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import com.speckyfox.utilities.commonUtilityFunctions.CommonUtils;

public class HiCareAppWebPage {

	protected WebDriver driver;

	public HiCareAppWebPage(WebDriver driver) {

		PageFactory.initElements(driver, this);
		this.driver = driver;
	}

	public WebElement getEmailInputBox(int timeOut) {

		String xpath = "//input[@name=\"username\"]";
		return CommonUtils.FindElement(driver, xpath, "Email Input Box", timeOut);

	}

	public WebElement getPasswordInputBox(int timeOut) {

		String xpath = "//input[@name=\"password\"]";
		return CommonUtils.FindElement(driver, xpath, "Password Input Box", timeOut);

	}

	public WebElement getSignInButton(int timeOut) {

		String xpath = "//button[text()=\"Sign In\"]";
		return CommonUtils.FindElement(driver, xpath, "Sign In Button", timeOut);

	}

	public WebElement getHicareLogo(int timeOut) {

		String xpath = "//img[@alt=\"AdminLTE Logo\"]";
		return CommonUtils.FindElement(driver, xpath, "Hi Care Logo", timeOut);

	}

	public WebElement getSideMenuLink(String linkName, int timeOut) {

		String xpath = "//p[text()[normalize-space()=\"" + linkName + "\"]]/ancestor::a";
		return CommonUtils.FindElement(driver, xpath, "Side Menu Link: " + linkName, timeOut);

	}

	public WebElement getUserListSelectElement(int timeOut) {

		String id = "userDropdown";
		return CommonUtils.FindElement(driver, By.id(id), "User List Select", timeOut);

	}

	public WebElement getViewLocationButton(int timeOut) {

		String id = "submitButton";
		return CommonUtils.FindElement(driver, By.id(id), "View Location Button", timeOut);

	}

	public WebElement getUserOnMap(int timeOut) {

		String xpath = "//h3[text()=\"User on Map\"]";
		return CommonUtils.FindElement(driver, By.xpath(xpath), "User on Map", timeOut);

	}

	public WebElement getStartDate(int timeOut) {

		String xpath = "//input[@name=\"startDate\"]";
		return CommonUtils.FindElement(driver, By.xpath(xpath), "Start Date", timeOut);

	}

	public WebElement getEndDate(int timeOut) {

		String xpath = "//input[@name=\"endDate\"]";
		return CommonUtils.FindElement(driver, By.xpath(xpath), "End Date", timeOut);

	}

	public WebElement getResultButton(int timeOut) {

		String xpath = "//button[text()[normalize-space()=\"Get Result\"]]";
		return CommonUtils.FindElement(driver, By.xpath(xpath), "Get Result Button", timeOut);

	}

	public WebElement getMapIFrame(int timeOut) {

		String xpath = "//div/following-sibling::iframe";
		return CommonUtils.FindElement(driver, By.xpath(xpath), "getMapIFrame", timeOut);

	}

	public WebElement get1ImageInMap(int timeOut) {

		String xpath = "//img[@src=\"https://maps.gstatic.com/mapfiles/transparent.png\"]/..";
		return CommonUtils.FindElement(driver, By.xpath(xpath), "Get Image Show 1", timeOut);

	}

	public WebElement getAddressInMap(int timeOut) {

		String xpath = "//p[text()=\"Additional info\"]/preceding-sibling::h6";
		return CommonUtils.FindElement(driver, By.xpath(xpath), "Get Address In Map", timeOut);

	}

	public WebElement getAddressInTable(int timeOut) {

		String xpath = "//tbody[@id=\"tableBody\"]//td[2]";
		return CommonUtils.FindElement(driver, By.xpath(xpath), "Get Address In Table", timeOut);

	}
	
	public WebElement getAddAuditFrequency(int timeOut) {

		String xpath = "//a[@href='/portal/addauditfrequency']";
		return CommonUtils.FindElement(driver, By.xpath(xpath), "Audit frequency button", timeOut);

	}
	
	public WebElement getSideMenuLink(int timeOut, String linkName) {

		String xpath = "//p[contains(text(),'"+linkName+"')]/ancestor::a";
		return CommonUtils.FindElement(driver, xpath, "Side Menu Link: " + linkName, timeOut);

	}
	
	public WebElement getAuditFrequencyTextField(String labelName, int timeOut) {

		String xpath = "//label[text()='"+labelName+"']/..//input";
		return CommonUtils.FindElement(driver, xpath, "label Name" +labelName , timeOut);

	}
	
	public WebElement getAuditFrequencyTextField(int timeOut) {

		String xpath = "//select[@name='status']";
		return CommonUtils.FindElement(driver, xpath, "status"  , timeOut);
	}
	
	public WebElement getSaveBtn(String btnName, int timeOut) {

		String xpath="//button[text()='"+btnName+"']";
		return CommonUtils.FindElement(driver, xpath,"button name" , timeOut);

	}
	
	public List<WebElement> getFrequencyRecord(String name) {

		String xpath = "//table[@id='example']/tbody//td[text()='"+name+"']/..//td";
		return CommonUtils.FindElements(driver, xpath, "button name" );

	}
	
	
	
	
	



	
	

	
	
}
