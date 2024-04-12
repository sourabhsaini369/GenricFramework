package com.speckyfox.businessLayer.web;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.speckyfox.drivermanager.DriverManager;
import com.speckyfox.enums.EnumConstants.DTQACardSelect;
import com.speckyfox.enums.EnumConstants.DTQACardSubCategorySelect;
import com.speckyfox.enums.EnumConstants.DTQARegisteredFormSubmitFileds;
import com.speckyfox.enums.EnumConstants.DTQAWebTablesFields;
import com.speckyfox.enums.EnumConstants.DTQAWebTablesGender;
import com.speckyfox.logmanager.LogsManager;
import com.speckyfox.pageObjects.webPages.DTQAHomePage;
import com.speckyfox.utilities.commonUtilityFunctions.CommonUtils;

public class DTQAHomePageBusinessLayer extends DTQAHomePage {

	public DTQAHomePageBusinessLayer(WebDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}

	private boolean clickOnCard(DTQACardSelect card, int timeout) {

		return CommonUtils.click(DriverManager.getDriver(), getElementsCard(card.toString().replace("_", " "), timeout),
				"Card: " + card.toString().replace("_", " "), Duration.ofSeconds(timeout));

	}

	private boolean clickOnSubCategoryOfCard(DTQACardSelect card, DTQACardSubCategorySelect subCategory, int timeout) {

		return CommonUtils.click(
				DriverManager.getDriver(), getSideNavSubCategory(card.toString().replace("_", " "),
						subCategory.toString().replace("_", " "), timeout),
				"Card: " + card.toString().replace("_", " "), Duration.ofSeconds(timeout));

	}

	private boolean clickOnPencilIconForEdit(String firstName, int timeout) {

		return CommonUtils.click(DriverManager.getDriver(), getEditButtonOnBasedOfFirstName(firstName, timeout),
				"FirstName: " + firstName, Duration.ofSeconds(timeout));

	}

	private boolean sendTextToField(DTQAWebTablesFields fields, String value, int timeout) {

		if (value != null) {
			if (!"".equals(value)) {

				return CommonUtils.sendKeys(DriverManager.getDriver(),
						getEditFormField(fields.toString().replace("_", " "), timeout), value,
						"Field: " + fields.toString().replace("_", " ") + " and Value: " + value,
						Duration.ofSeconds(timeout));
			}
		}
		return true;

	}

	private boolean clickOnSubmitButton(int timeout) {

		return CommonUtils.click(DriverManager.getDriver(), getEditFormSubmitButton(timeout), "Submit Button",
				Duration.ofSeconds(timeout));

	}

////////////////////Practice Form/////////////////////////////////

	private boolean sendTextFirstName(String value, int timeout) {

		return CommonUtils.sendKeys(DriverManager.getDriver(), getPracticeFormFirstName(timeout), value,
				"Field: First Name and Value: " + value, Duration.ofSeconds(timeout));
	}

	private boolean sendTextLastName(String value, int timeout) {

		return CommonUtils.sendKeys(DriverManager.getDriver(), getPracticeFormLastName(timeout), value,
				"Field: Last Name and Value: " + value, Duration.ofSeconds(timeout));
	}

	private boolean sendTextEmailId(String value, int timeout) {

		return CommonUtils.sendKeys(DriverManager.getDriver(), getPracticeFormEmailID(timeout), value,
				"Field: Email and Value: " + value, Duration.ofSeconds(timeout));
	}

	private boolean clickonGenderRadioButton(String gender, int timeout) {

		return CommonUtils.click(DriverManager.getDriver(), getPracticeFormGenderRadio(gender, timeout),
				"Gender Radio : " + gender, Duration.ofSeconds(timeout));

	}

	private boolean sendTextMobileNumber(String value, int timeout) {

		return CommonUtils.sendKeys(DriverManager.getDriver(), getPracticeFormMobileNumber(timeout), value,
				"Field: Mobile and Value: " + value, Duration.ofSeconds(timeout));
	}

	private boolean sendTextDOB(String value, int timeout) {

		CommonUtils.click(DriverManager.getDriver(), getPracticeFormDOB(timeout), "DOB Element ",
				Duration.ofSeconds(timeout));

		String date = "";
		String month = "";
		String year = "";
		if (value != null) {
			String[] dateMonthYear = value.split("/");
			if (dateMonthYear.length == 3) {
				date = dateMonthYear[0];
				month = dateMonthYear[1];
				year = dateMonthYear[2];
			}

		}
		return CommonUtils.datePickerHandle(DriverManager.getDriver(), getMonthOfCalender(timeout),
				getYearSelectorOfCalender(timeout), getDatesOfCalender(timeout), "Calender Selector", year, month, date,
				Duration.ofSeconds(20));

	}

	private boolean sendTextSubjectInput(String value, int timeout) {

		return CommonUtils.sendKeys(DriverManager.getDriver(), getPracticeFormSubjectInput(timeout), value,
				"Field: Subject and Value: " + value, Duration.ofSeconds(timeout));
	}

	private boolean clickOnSubjectDropdown(String subjectName, int timeout) {

		return CommonUtils.click(DriverManager.getDriver(), getPracticeFormSubjectInputSuggestion(subjectName, timeout),
				"Subject Drop Down", Duration.ofSeconds(timeout));

	}

	private boolean clickOnHobbies(String[] hobbies, int timeout) {

		boolean flag = false;
		for (String hobby : hobbies) {

			flag = false;
			flag = CommonUtils.click(DriverManager.getDriver(), getPracticeFormHobbiesCheckbox(hobby, timeout),
					"Subject Drop Down", Duration.ofSeconds(timeout));
			if (!flag) {
				return flag;
			}
		}
		return flag;

	}

	private boolean selectPicture(String path, int timeout) {

		return CommonUtils.sendKeys(DriverManager.getDriver(), getPracticeFormUploadPicture(timeout), path,
				"Field: Picture and Value: " + path, Duration.ofSeconds(timeout));
	}

	private boolean sendTextCurrentAddress(String value, int timeout) {

		return CommonUtils.sendKeys(DriverManager.getDriver(), getPracticeFormCurrentAddress(timeout), value,
				"Field: Current Address and Value: " + value, Duration.ofSeconds(timeout));
	}

	private boolean clickOnState(int timeout) {

		return CommonUtils.click(DriverManager.getDriver(), getPracticeFormStateDropDownButton(timeout),
				"Subject Drop Down", Duration.ofSeconds(timeout));

	}

	private boolean clickOnStateDropdownValue(String value, int timeout) {

		return CommonUtils.click(DriverManager.getDriver(), getPracticeFormStateSelectFromDropdown(value, timeout),
				"Subject Drop Down", Duration.ofSeconds(timeout));

	}

	private boolean clickOnCity(int timeout) {

		return CommonUtils.click(DriverManager.getDriver(), getPracticeFormCityDropDownButton(timeout),
				"Subject Drop Down", Duration.ofSeconds(timeout));

	}

	private boolean clickOnCityDropdownValue(String value, int timeout) {

		return CommonUtils.click(DriverManager.getDriver(), getPracticeFormCitySelectFromDropdown(value, timeout),
				"Subject Drop Down", Duration.ofSeconds(timeout));

	}

	private boolean clickOnSubmitButtonOnRegistrationForm(int timeout) {

		return CommonUtils.click(DriverManager.getDriver(), getPracticeFormSubmitButton(timeout), "Submit Button",
				Duration.ofSeconds(timeout));

	}

	private List<String> registrationFormVerification(String firstName, String lastName, String email,
			String genderRadio, String mobileNumber, String dateOfBirth, String subject, String[] hobbies,
			String picturePath, String currentAddress, int timeout) {

		List<String> negativeResult = new ArrayList<String>();

		String actualStudentName = getPracticeFormDataVerify(DTQARegisteredFormSubmitFileds.Student_Name, timeout)
				.getText();
		String actualGender = getPracticeFormDataVerify(DTQARegisteredFormSubmitFileds.Gender, timeout).getText();
		String actualEmail = getPracticeFormDataVerify(DTQARegisteredFormSubmitFileds.Student_Email, timeout).getText();
		String actualMobile = getPracticeFormDataVerify(DTQARegisteredFormSubmitFileds.Mobile, timeout).getText();
		String actualDOB = getPracticeFormDataVerify(DTQARegisteredFormSubmitFileds.Date_of_Birth, timeout).getText();
		String actualSubject = getPracticeFormDataVerify(DTQARegisteredFormSubmitFileds.Subjects, timeout).getText();
		String actualHobies = getPracticeFormDataVerify(DTQARegisteredFormSubmitFileds.Hobbies, timeout).getText();
		String actualPicturePath = getPracticeFormDataVerify(DTQARegisteredFormSubmitFileds.Picture, timeout).getText();
		String actualCurrentAddress = getPracticeFormDataVerify(DTQARegisteredFormSubmitFileds.Address, timeout)
				.getText();

		if (actualStudentName.equals(firstName + " " + lastName)) {
			LogsManager.getLogger().info("Student Name Verified: " + actualStudentName);

		} else {
			LogsManager.getLogger().error("Student Name not Verified, Actual: " + actualStudentName + " but Expected: "
					+ firstName + " " + lastName);

			negativeResult.add("Student Name not Verified, Actual: " + actualStudentName + " but Expected: " + firstName
					+ " " + lastName);
		}

		if (actualGender.equals(genderRadio)) {
			LogsManager.getLogger().info("Student Gender Verified: " + genderRadio);

		} else {

			LogsManager.getLogger()
					.error("Student Gender not Verified, Actual: " + actualGender + " but Expected: " + genderRadio);

			negativeResult
					.add("Student Gender not Verified, Actual: " + actualGender + " but Expected: " + genderRadio);
		}

		if (actualEmail.equals(email)) {
			LogsManager.getLogger().info("Student Email Verified: " + email);

		} else {

			LogsManager.getLogger()
					.error("Student Email not Verified, Actual: " + actualEmail + " but Expected: " + email);
			negativeResult.add("Student Email not Verified, Actual: " + actualEmail + " but Expected: " + email);
		}

		if (actualMobile.equals(mobileNumber)) {
			LogsManager.getLogger().info("Student Mobile Verified: " + mobileNumber);

		} else {

			LogsManager.getLogger()
					.error("Student Mobile not Verified, Actual: " + actualMobile + " but Expected: " + mobileNumber);

			negativeResult
					.add("Student Mobile not Verified, Actual: " + actualMobile + " but Expected: " + mobileNumber);
		}

		if (dateOfBirth != null) {
			String[] dateMonthYear = dateOfBirth.split("/");
			if (dateMonthYear.length == 3) {
				dateOfBirth = dateMonthYear[0] + " " + dateMonthYear[1] + "," + dateMonthYear[2];
			} else {
				LogsManager.getLogger().error("Student DOB expected format is not correct " + dateOfBirth);

				negativeResult.add("Student DOB expected format is not correct " + dateOfBirth);
			}
		}
		if (actualDOB.equals(dateOfBirth)) {
			LogsManager.getLogger().info("Student DOB Verified: " + dateOfBirth);

		} else {

			LogsManager.getLogger()
					.error("Student DOB not Verified, Actual: " + actualDOB + " but Expected: " + dateOfBirth);
			negativeResult.add("Student DOB not Verified, Actual: " + actualDOB + " but Expected: " + dateOfBirth);
		}

		if (actualSubject.equals(subject)) {

			LogsManager.getLogger().info("Student Subject Verified: " + subject);

		} else {

			LogsManager.getLogger()
					.error("Student Subject not Verified, Actual: " + actualSubject + " but Expected: " + subject);
			negativeResult.add("Student Subject not Verified, Actual: " + actualSubject + " but Expected: " + subject);
		}

		for (String hobby : hobbies) {
			if (actualHobies.contains(hobby)) {
				LogsManager.getLogger().info("Student Hobies Verified: " + hobby);

			} else {
				LogsManager.getLogger()
						.error("Student Hobies not Verified, Actual: " + actualHobies + " but Expected: " + hobby);
				negativeResult.add("Student Hobies not Verified, Actual: " + actualHobies + " but Expected: " + hobby);
			}
		}

		if (picturePath.contains(actualPicturePath)) {

			LogsManager.getLogger().info("Student Picture Verified: " + picturePath);

		} else {

			LogsManager.getLogger().error(
					"Student Picture not Verified, Actual: " + actualPicturePath + " but Expected: " + picturePath);

			negativeResult.add(
					"Student Picture not Verified, Actual: " + actualPicturePath + " but Expected: " + picturePath);
		}

		if (actualCurrentAddress.equals(currentAddress)) {

			LogsManager.getLogger().info("Student Current Address Verified: " + currentAddress);
		} else {

			LogsManager.getLogger().error("Student Current Address not Verified, Actual: " + actualCurrentAddress
					+ " but Expected: " + currentAddress);
			negativeResult.add("Student Current Address not Verified, Actual: " + actualCurrentAddress
					+ " but Expected: " + currentAddress);
		}

		return negativeResult;

	}

	public boolean webTablesEdit(String firstNameToWhichEdit, String firstName, String lastName, String email,
			String age, String salary, String department, int timeout) {

		List<String> dataToVerify = new ArrayList<String>();
		dataToVerify.add(lastName);
		dataToVerify.add(email);
		dataToVerify.add(age);
		dataToVerify.add(salary);
		dataToVerify.add(department);

		clickOnCard(DTQACardSelect.Elements, timeout);
		clickOnSubCategoryOfCard(DTQACardSelect.Elements, DTQACardSubCategorySelect.Web_Tables, timeout);

		List<WebElement> firstNames = getFirstNames(timeout);
		List<String> firstNamesText = firstNames.stream().map(x -> x.getText()).collect(Collectors.toList());

		if (clickOnPencilIconForEdit(firstNameToWhichEdit, timeout)) {
			sendTextToField(DTQAWebTablesFields.First_Name, firstName, timeout);
			sendTextToField(DTQAWebTablesFields.Last_Name, lastName, timeout);
			sendTextToField(DTQAWebTablesFields.Email, email, timeout);
			sendTextToField(DTQAWebTablesFields.Age, age, timeout);
			sendTextToField(DTQAWebTablesFields.Salary, salary, timeout);
			sendTextToField(DTQAWebTablesFields.Department, department, timeout);
			clickOnSubmitButton(timeout);

			if (!firstNamesText.isEmpty()) {
				String getNameToFindRow;
				if (!"".equals(firstName) && firstName != null) {

					getNameToFindRow = firstName;

				} else {
					getNameToFindRow = firstNameToWhichEdit;

				}

				Integer rowOfFirstName = firstNamesText.indexOf(firstNameToWhichEdit) + 1;

				List<String> actualDataAfterEdit = getRow(rowOfFirstName, timeout).stream().map(x -> x.getText())
						.collect(Collectors.toList());

				dataToVerify.removeIf(str -> str == null || str.trim().isEmpty());

				if (actualDataAfterEdit.contains(getNameToFindRow)) {

					LogsManager.getLogger().info("Verify first Name is: " + getNameToFindRow);

				} else {

					LogsManager.getLogger()
							.error("Not Able to find the name: " + firstName + ", So Not able to Edit it");

				}

				for (String expectedChanges : dataToVerify)

				{

					if (actualDataAfterEdit.contains(expectedChanges)) {

						LogsManager.getLogger().info("Verify changed data: " + expectedChanges
								+ " of row whose first Name is: " + getNameToFindRow);

					} else {

						LogsManager.getLogger().error("Not Able to find data: " + expectedChanges
								+ " of row whose first Name is: " + getNameToFindRow);

					}

				}

			}

			return true;

		} else {

			LogsManager.getLogger().error("Not Able to find the name: " + firstName + ", So Not able to Edit it");

			return false;

		}

	}

	public List<String> studentRegistration(String firstName, String lastName, String email, String genderRadio,
			String mobileNumber, String dateOfBirth, String subject, String[] hobbies, String picturePath,
			String currentAddress, String state, String city, int timeout) {

		List<String> negativeResult = new ArrayList<String>();
		clickOnCard(DTQACardSelect.Forms, timeout);
		clickOnSubCategoryOfCard(DTQACardSelect.Forms, DTQACardSubCategorySelect.Practice_Form, timeout);
		sendTextFirstName(firstName, timeout);
		sendTextLastName(lastName, timeout);
		sendTextEmailId(email, timeout);
		clickonGenderRadioButton(genderRadio, timeout);
		sendTextMobileNumber(mobileNumber, timeout);

		sendTextDOB(dateOfBirth, timeout);
		sendTextSubjectInput(subject, timeout);
		clickOnSubjectDropdown(subject, timeout);
		clickOnHobbies(hobbies, timeout);
		selectPicture(picturePath, timeout);
		sendTextCurrentAddress(currentAddress, timeout);
//		clickOnState(timeout);
//
//		clickOnStateDropdownValue(state, timeout);
//		clickOnCity(timeout);
//
//		clickOnCityDropdownValue(city, timeout);
		clickOnSubmitButtonOnRegistrationForm(timeout);

		if (succesFormSubmitMsg(timeout) != null) {
			LogsManager.getLogger().info("Success Msg Verified: " + firstName + " " + lastName);

			List<String> negativeResultTemp = registrationFormVerification(firstName, lastName, email, genderRadio,
					mobileNumber, dateOfBirth, subject, hobbies, picturePath, currentAddress, timeout);

			negativeResult.addAll(negativeResultTemp);
		} else {
			LogsManager.getLogger().error("Success Msg Verified Not Verified: " + firstName + " " + lastName);
			negativeResult.add("Success Msg Verified Not Verified: " + firstName + " " + lastName);
		}

		return negativeResult;
	}

}
