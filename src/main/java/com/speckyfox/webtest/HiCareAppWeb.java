package com.speckyfox.webtest;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.speckyfox.base.WebBase;
import com.speckyfox.businessLayer.web.HiCareAppWebPageBusinessLayer;
import com.speckyfox.dataprovider.ExcelDataProvider;
import com.speckyfox.drivermanager.DriverManager;
import com.speckyfox.utilities.commonUtilityFunctions.CommonUtils;

import groovyjarjarasm.asm.commons.Method;
import org.testng.ITestResult;

public class HiCareAppWeb extends WebBase {

	@Test
	public void TC01_Verify_User_Travelled_Location_WEB() {

		HiCareAppWebPageBusinessLayer hiCareBusiness = new HiCareAppWebPageBusinessLayer(DriverManager.getDriver());

		if (!hiCareBusiness.login("akshay@speckyfox.com", "User#2017")) {
			Assert.assertTrue(false, "Logged in Not Successfull");
		}
		List<String> negativeResult = hiCareBusiness.verifyUserTravelLocation("418", "2023-12-01", "2023-12-31",
				"A 42, Block A, Sector 62, Noida, Uttar Pradesh 201309, India");

		if (!negativeResult.isEmpty()) {
			Assert.assertTrue(false, negativeResult.toString());
		}

	}


	@Test
	public void TC03_Verify_AddFrequency_WEB() {

		ArrayList<String> freqDetails=new ArrayList<String>();
		freqDetails.add("Sourabhs");
		freqDetails.add("5");
		freqDetails.add("Active");

		HiCareAppWebPageBusinessLayer hiCareBusiness = new HiCareAppWebPageBusinessLayer(DriverManager.getDriver());

		if (!hiCareBusiness.login("akshay@speckyfox.com", "User#2017")) {
			Assert.assertTrue(false, "Logged in Not Successfull");
		}
		ArrayList<String> result= hiCareBusiness.createAuditFrequency(freqDetails.get(0),freqDetails.get(1),freqDetails.get(2));	
		if(result.isEmpty())
		{
			Assert.assertTrue(true, "Audit frequency has been created");
			try {
				Thread.sleep(30000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			CommonUtils.refresh(DriverManager.getDriver(), Duration.ofSeconds(20));
			ArrayList<String> result1=hiCareBusiness.verifyAuditFrequency(freqDetails.get(0),freqDetails);

			if(result1.isEmpty())
			{
				Assert.assertTrue(true, "Audit frequency has been verified");
			}
			else
			{
				Assert.assertTrue(false, "Audit frequency is not verified. "+result1);
			}		
		}
		else
		{
			Assert.assertTrue(false, "Audit frequency is not created.  "+ result.toString());
		}
	}



	@Test(dataProvider = "dataProviderData")
	public void TC04_Verify_Login_WEB(String S_No,String username, String password, String expected_result,String status){
		System.out.println(username);
		System.out.println(password);
		System.out.println(expected_result);
		HiCareAppWebPageBusinessLayer hiCareBusiness = new HiCareAppWebPageBusinessLayer(DriverManager.getDriver());
		if(expected_result.equals("pass"))
		{
			if (!hiCareBusiness.login(username, password)) {
				Assert.assertTrue(false, "Logged in Not Successfull");
			}}
		else
		{
			if (!hiCareBusiness.login(username, password)) {
				Assert.assertTrue(true, "Logged in Not Successfull");
			}
		}



	}

}
