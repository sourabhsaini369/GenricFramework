package com.speckyfox.cucumberStepDefinitions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.testng.Assert;

import com.speckyfox.businessLayer.web.DemoQaBusinessLayer;
import com.speckyfox.drivermanager.DriverManager;
import com.speckyfox.logmanager.LogsManager;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class DemoqaSteps {

	LinkedHashMap<String, String> map;
	
	@Given("I am on registration form")
	public void onRegistrationPage() {
		DriverManager.getDriver().get("https://demoqa.com/webtables");
		LogsManager.getLogger().info("Go To Google Website");
	}

	@When("I filled the registration form with valid details")
	public void fillRegistrationForm() {
		map =new LinkedHashMap<String,String>();
		map.put("First Name", "Sourabh");
		map.put("Last Name", "saini");
		map.put("Email", "Sourabh@yopmail.com");
		map.put("Age", "20");
		map.put("Salary", "12000");
		map.put("Department", "Testing");
		
		DemoQaBusinessLayer db=new DemoQaBusinessLayer(DriverManager.getDriver());
		ArrayList<String> result=db.createWebTable(map);
		if(result.isEmpty())
		{
			Assert.assertTrue(true, "The record has been created.");
		}
		else
		{
			Assert.assertTrue(false, "The record is not created.");	
		}
	}

	@Then("I should see new records in the Web table")
	public void theResultShouldBe() {
		
		ArrayList<String> expectedData=new ArrayList<String>(map.keySet());
		DemoQaBusinessLayer db=new DemoQaBusinessLayer(DriverManager.getDriver());
		ArrayList<String> result=db.verifyWebtableRecord(expectedData);
		if(result.isEmpty())
		{
			Assert.assertTrue(true, "The data has been verified on webtable");
		}
		else
		{
			Assert.assertTrue(true, "The data is not verified on webtable");
				
		}
	}

}
