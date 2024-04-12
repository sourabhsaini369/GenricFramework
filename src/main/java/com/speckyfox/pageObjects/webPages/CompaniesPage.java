package com.speckyfox.pageObjects.webPages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import com.speckyfox.utilities.commonUtilityFunctions.CommonUtils;

public class CompaniesPage {

	protected WebDriver driver;

	public CompaniesPage(WebDriver driver) {
		// TODO Auto-generated constructor stub
		PageFactory.initElements(driver, this);
		this.driver = driver;
	}

	public WebElement getInputSuggestion(String optionToSelect, int timeOut) {
		String xpath = "//ul[contains(@class,\"suggestions\")]//span/span[@class=\"suggestionLabel\"]";
		try {
			return CommonUtils.getElementVisibility(driver, CommonUtils.FindElements(driver, xpath, ""), optionToSelect,
					Duration.ofSeconds(timeOut), "Input: " + optionToSelect, true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public WebElement getIndustriesInput(int timeOut) {

		String xpath = "//input[@name=\"Industries\"]";
		return CommonUtils.FindElement(driver, xpath, "Industries", timeOut);
	}

	public WebElement getLocationInput(int timeOut) {

		String xpath = "//input[@name=\"Location\"]";
		return CommonUtils.FindElement(driver, xpath, "Location", timeOut);
	}

	public WebElement getJobTitleInput(int timeOut) {

		String xpath = "//input[@name=\"Job Title\"]";
		return CommonUtils.FindElement(driver, xpath, "Job Title", timeOut);
	}

	public WebElement getJobFunctionsInputButton(int timeOut) {

		String xpath = "//div[contains(@class, \"block\")]/h3[text()=\"Job Functions \"]/following-sibling::div[@role=\"button\"]";
		return CommonUtils.FindElement(driver, xpath, "Job Functions", timeOut);
	}

	public List<WebElement> getJobFunctionsNames(int timeOut) {

		String xpath = "//div[@class=\"pt-xl\"]//span";
		return CommonUtils.FindElements(driver, xpath, "");
		
	}

	public List<WebElement> getJobFunctionsCheckBoxes(int timeOut) {

		String xpath = "//div[@class=\"pt-xl\"]//ancestor::div[contains(@data-test, \"checkbox\")]";
		
			return CommonUtils.FindElements(driver, xpath, "");
		
			// TODO Auto-generated catch block
	
		

	}

	public WebElement employerRatingDropDownButton(int timeOut) {

		String xpath = "//div[@aria-label=\"Select Employer Ratings\"]/div/div";
		return CommonUtils.FindElement(driver, xpath, "Employer Rating Drop Down Button", timeOut);
	}

	public WebElement getRatingSelectFromDropDown(String rating, int timeOut) {

		String xpath = "//div[@class=\"dropDownOptionsContainer\"]//li[@id=\"option_" + rating + "\"]";
		return CommonUtils.FindElement(driver, xpath, "Rating: " + rating, timeOut);
	}

	public WebElement getCompanySize(String companySize, int timeOut) {

		String xpath = "//input[@value=\"" + companySize + "\"]/ancestor::div[contains(@data-test,\"radioButton\")]";
		return CommonUtils.FindElement(driver, xpath, "Comapny Size: " + companySize, timeOut);
	}

//	public WebElement getEmployeeCard(String companySize, int timeOut) {
//
//		String xpath = "//input[@value=\"" + companySize + "\"]";
//		return CommonUtils.FindElement(driver, xpath, "Comapny Size: " + companySize, timeOut);
//	}

	public List<WebElement> getCompanyCards(int timeOut) {

		String xpath = "//div[@data-test=\"employer-card-single\"]";
		
			return CommonUtils.FindElements(driver, xpath, "");
		

	}

	public List<WebElement> getPaginationLinks(int timeOut) {

		String xpath = "//div[@class=\"pageContainer\"]//button[contains(@data-test,\"pagination-link\")]";
		
			return CommonUtils.FindElements(driver, xpath, "");
		

	}

	// After New Page

	public WebElement getEmployerHeading(int timeOut) {

		String xpath = "//div[@data-test=\"EmpInfo\"]//h1";
		return CommonUtils.FindElement(driver, xpath, "Employee Heading", timeOut);

	}

	public WebElement getEmployerWebsite(int timeOut) {

		String xpath = "//div[@id=\"MainContent\"]//ul[@data-test=\"companyDetails\"]//a[@data-test=\"employer-website\"]";
		return CommonUtils.FindElement(driver, xpath, "Employer Website", timeOut);

	}

	public WebElement getEmployerLocation(int timeOut) {

		String xpath = "//div[@id=\"MainContent\"]//ul[@data-test=\"companyDetails\"]//li[2]";
		return CommonUtils.FindElement(driver, xpath, "Employer Location", timeOut);

	}

	public WebElement getNotHumanCheckbox(int timeOut) {

		String xpath = "//span[text()=\"Verify you are human\"]/preceding-sibling::input/ancestor::div[@role=\"alert\"]";
		return CommonUtils.FindElement(driver, xpath, "Not Human Checkbox", timeOut);

	}
	
	
	public WebElement getNotHumanCheckboxIframe(int timeOut) {

		String xpath = "//div[@id=\"turnstile-wrapper\"]//iframe";
		return CommonUtils.FindElement(driver, xpath, "Not Human Checkbox Iframe", timeOut);

	}

	
	
	
	// div[@data-test="employer-card-single"]

	// div[@class="pageContainer"]//button[contains(@data-test,"pagination-link")]
	// Attribute class contains page selected

	// After New Page

	// div[@data-test="EmpInfo"]//h1

	// div[@id="MainContent"]//ul[@data-test="companyDetails"]//a[@data-test="employer-website"]

	// Location
	// div[@id="MainContent"]//ul[@data-test="companyDetails"]//li[2]

}
