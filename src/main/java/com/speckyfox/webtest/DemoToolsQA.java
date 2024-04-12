package com.speckyfox.webtest;

import java.util.List;

import org.testng.*;
import org.testng.annotations.*;

import com.speckyfox.base.WebBase;
import com.speckyfox.businessLayer.web.DTQAHomePageBusinessLayer;
import com.speckyfox.drivermanager.DriverManager;
import com.speckyfox.logmanager.LogsManager;
import com.speckyfox.reports.ReportManager;

public class DemoToolsQA extends WebBase {

	@Test(dataProvider = "WebTableData")
	public void UpdateWebTableData_WEB(String firstNameToWhichEdit, String firstName, String lastName, String email,
			String age, String salary, String department) {

		DTQAHomePageBusinessLayer dtqHP = new DTQAHomePageBusinessLayer(DriverManager.getDriver());

		boolean flag = dtqHP.webTablesEdit(firstNameToWhichEdit, firstName, lastName, email, age, salary, department,
				15);

		if (flag) {
			LogsManager.getLogger().info("WebTable Edit Pass for " + firstNameToWhichEdit);
			ReportManager.getReportType().getTestLogger().info("WebTable Edit Pass for " + firstNameToWhichEdit);
		} else {
			LogsManager.getLogger().error("WebTable Edit Fail for " + firstNameToWhichEdit);
			ReportManager.getReportType().getTestLogger().warning("WebTable Edit Fail for " + firstNameToWhichEdit);
			Assert.assertTrue(flag);
		}

		if (firstNameToWhichEdit.equals("Kierra")) {
			LogsManager.getLogger().error("WebTable Edit Fail for " + firstNameToWhichEdit);
			ReportManager.getReportType().getTestLogger().warning("WebTable Edit Fail for " + firstNameToWhichEdit);

			Assert.assertTrue(false, "Failed Due to Kierra occured");
		}

	}

	@Test(dataProvider = "RegistartionForm")
	public void FillStudentRegistrationForm_WEB(String firstName, String lastName, String email, String genderRadio,
			String mobileNumber, String dateOfBirth, String subject, String hobbiesWithBreaks, String picturePath,
			String currentAddress, String state, String city) {

		DTQAHomePageBusinessLayer dtqHP = new DTQAHomePageBusinessLayer(DriverManager.getDriver());

		String[] hobbies = hobbiesWithBreaks.split("<break>");

		List<String> negativeResults = dtqHP.studentRegistration(firstName, lastName, email, genderRadio, mobileNumber,
				dateOfBirth, subject, hobbies, picturePath, currentAddress, state, city, 10);

		if (negativeResults.isEmpty()) {
			LogsManager.getLogger().info("Registartion Form Verified");

		} else {
			LogsManager.getLogger().error("Registartion Form Not Verified, Reason: " + negativeResults);

		}

	}

}
