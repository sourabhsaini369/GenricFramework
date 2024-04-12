package com.speckyfox.pageObjects.webPages;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import com.speckyfox.enums.EnumConstants.DTQARegisteredFormSubmitFileds;
import com.speckyfox.utilities.commonUtilityFunctions.CommonUtils;

public class DTQAHomePage {

	protected WebDriver driver;

	public DTQAHomePage(WebDriver driver) {

		PageFactory.initElements(driver, this);
		this.driver = driver;
	}

	public WebElement getElementsCard(String cardName, int timeOut) {

		String xpath = "//h5[text()='" + cardName + "']/ancestor::div[contains(@class,'top-card')]";
		return CommonUtils.FindElement(driver, xpath, "Card: Elements", timeOut);

	}

	public WebElement getSideNavExpand(int timeOut) {

		String xpath = "//div[@class='header-text'][text()='Elements']/ancestor::span/following-sibling::div";
		return CommonUtils.FindElement(driver, xpath, "Card: Elements", timeOut);

	}

	public WebElement getSideNavSubCategory(String category, String subCategoryName, int timeOut) {

		String xpath = "//div[@class='header-text'][text()='" + category
				+ "']/ancestor::span/following-sibling::div//li//span[text()='" + subCategoryName + "']";
		return CommonUtils.FindElement(driver, xpath, "Card: Elements", timeOut);

	}

	public List<WebElement> getEditButtons(int timeOut) {

		String xpath = "//div[@class='rt-tbody']//div[@class='rt-tr-group']//div[@class='rt-td']//span[@title='Edit']";

		return CommonUtils.FindElements(driver, xpath, "Edit Buttons");

	}

	public WebElement getEditButtonOnBasedOfFirstName(String firstName, int timeOut) {

		String xpath = "// div[@class='rt-tbody']//div[@class='rt-tr-group']//div[@class='rt-td'][text()='" + firstName
				+ "']/following-sibling::div//span[@title='Edit']";
		return CommonUtils.FindElement(driver, xpath, "First Name: " + firstName, timeOut);

	}

	public List<WebElement> getFirstNames(int timeOut) {

		String xpath = "//div[@class='rt-tbody']//div[@class='rt-tr-group']//div[@class='rt-td'][1]";

		return CommonUtils.FindElements(driver, xpath, "First Names");

	}

	public List<WebElement> getRow(Integer rowIndex, int timeOut) {

		String xpath = "(//div[@class='rt-tbody']//div[@class='rt-tr-group'])[" + rowIndex
				+ "]//div[@class='rt-td'][not(descendant::span)]";

		return CommonUtils.FindElements(driver, xpath, "Row: " + rowIndex);

	}

	public WebElement getEditFormField(String fieldName, int timeOut) {

		String xpath = "//label[@class='form-label'][text()='" + fieldName + "']/../following-sibling::*//input";
		return CommonUtils.FindElement(driver, xpath, "First Name", timeOut);

	}

	public WebElement getEditFormSubmitButton(int timeOut) {

		String xpath = "//button[@id='submit']";
		return CommonUtils.FindElement(driver, xpath, "Submit Button", timeOut);

	}

	// Form Practice

	public WebElement getPracticeFormFirstName(int timeOut) {

		String xpath = "//input[@id='firstName']";
		return CommonUtils.FindElement(driver, xpath, "Practice Form: First Name", timeOut);

	}

	public WebElement getPracticeFormLastName(int timeOut) {

		String xpath = "//input[@id='lastName']";
		return CommonUtils.FindElement(driver, xpath, "Practice Form: Last Name", timeOut);

	}

	public WebElement getPracticeFormEmailID(int timeOut) {

		String xpath = "//input[@id='userEmail']";
		return CommonUtils.FindElement(driver, xpath, "Practice Form: Email ID", timeOut);

	}

	public WebElement getPracticeFormGenderRadio(String gender, int timeOut) {

		String xpath = "//label[contains(@for,'gender-radio')][text()='" + gender
				+ "']/preceding-sibling::input/parent::div";
		return CommonUtils.FindElement(driver, xpath, "Practice Form: Gender Radio and Value: " + gender, timeOut);

	}

	public WebElement getPracticeFormMobileNumber(int timeOut) {

		String xpath = "//input[@id='userNumber']";
		return CommonUtils.FindElement(driver, xpath, "Practice Form: Mobile Number", timeOut);

	}

	public WebElement getPracticeFormDOB(int timeOut) {

		String xpath = "//input[@id='dateOfBirthInput']";
		return CommonUtils.FindElement(driver, xpath, "Practice Form: DOB", timeOut);

	}

	public WebElement getPracticeFormSubjectInput(int timeOut) {

		String xpath = "//input[@id='subjectsInput']";
		return CommonUtils.FindElement(driver, xpath, "Practice Form: Subject Input", timeOut);

	}

	public WebElement getPracticeFormSubjectInputSuggestion(String subjectName, int timeOut) {

		String xpath = "//div[contains(@class,'subjects-auto-complete__option')][text()='" + subjectName + "']";
		return CommonUtils.FindElement(driver, xpath, "Practice Form: Subject Input Suggestion: " + subjectName,
				timeOut);

	}

	public WebElement getPracticeFormHobbiesCheckbox(String hobby, int timeOut) {

		String xpath = "//label[text()='" + hobby + "']/preceding-sibling::input/parent::div";
		return CommonUtils.FindElement(driver, xpath, "Practice Form: Hobies Checkbox", timeOut);

	}

	public WebElement getPracticeFormUploadPicture(int timeOut) {

		String xpath = "//input[@id='uploadPicture']";
		return CommonUtils.FindElement(driver, xpath, "Practice Form: Picture Upload", timeOut);

	}

	public WebElement getPracticeFormCurrentAddress(int timeOut) {

		String xpath = "//textarea[@id='currentAddress']";
		return CommonUtils.FindElement(driver, xpath, "Practice Form: Current Address", timeOut);

	}

	public WebElement getPracticeFormStateDropDownButton(int timeOut) {

		String xpath = "//div[@id='state']/div/div";
		return CommonUtils.FindElement(driver, xpath, "Practice Form: State Dropdown Button", timeOut);

	}

	public WebElement getPracticeFormStateSelectFromDropdown(String stateName, int timeOut) {

		String xpath = "//div[contains(@class,'menu')]//div[contains(@class,'option')][text()='" + stateName + "']";
		return CommonUtils.FindElement(driver, xpath, "Practice Form: State Select From Dropdown", timeOut);

	}

	public WebElement getPracticeFormCityDropDownButton(int timeOut) {

		String xpath = "//div[text()='Select City']/ancestor::div[contains(@class,'control')]";
		return CommonUtils.FindElement(driver, xpath, "Practice Form: City Dropdown Button", timeOut);

	}

	public WebElement getPracticeFormCitySelectFromDropdown(String cityName, int timeOut) {

		String xpath = "//div[contains(@class,'menu')]//div[contains(@class,'option')][text()='" + cityName + "']";
		return CommonUtils.FindElement(driver, xpath, "Practice Form: City Select From Dropdown", timeOut);

	}

	public WebElement getPracticeFormSubmitButton(int timeOut) {

		String xpath = "//button[@id='submit']";
		return CommonUtils.FindElement(driver, xpath, "Practice Form: Submit Button", timeOut);

	}

	public WebElement succesFormSubmitMsg(int timeOut) {

		String xpath = "//div[text()='Thanks for submitting the form']";
		return CommonUtils.FindElement(driver, xpath, "Practice Form: Success Msg", timeOut);

	}

	public WebElement getPracticeFormDataVerify(DTQARegisteredFormSubmitFileds field, int timeOut) {

		String fieldTemp = field.toString().replace("_", " ");
		String xpath = "//td[text()='" + fieldTemp + "']/following-sibling::td";
		return CommonUtils.FindElement(driver, xpath, "Practice Form Verify Field: " + fieldTemp, timeOut);

	}
	
	public WebElement getMonthOfCalender( int timeOut) {

		
		String xpath = "//select[@class='react-datepicker__month-select']";
		return CommonUtils.FindElement(driver, xpath, "Month of Calender " , timeOut);

	}
	public WebElement getYearSelectorOfCalender( int timeOut) {


		String xpath =  "//select[contains(@class,'year-select')]";
		return CommonUtils.FindElement(driver, xpath, "Year of Calender ", timeOut);

	}
	
	public String getDatesOfCalender(int timeOut) {

		String xpath = "//div[contains(@class,'react-datepicker__day--')][not(contains(@class,'outside-month'))]";

		return xpath;

	}

	

	// td[text()='Student Name']/following-sibling::td

	// td[text()='Student Email']/following-sibling::td

}