package com.speckyfox.cucumberStepDefinitions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.Assert;

import com.speckyfox.constants.ConfigConstants;
import com.speckyfox.constants.PathConstants;
import com.speckyfox.logmanager.LogsManager;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class HighcareAPI {
	
	private String bearerToken="";
	private Response response; 
	
	
	@Given("The API base URL")
	public void baseURL()
	{
		
		Assert.assertEquals(RestAssured.baseURI, ConfigConstants.apiBaseURL, "The base url is not configured");
		LogsManager.getLogger().info("The base url has been configured");
	}
	
	@When("I send a post request")
	public void sendRequest() throws FileNotFoundException
	{
		
		File f=new File(PathConstants.loginCredential);
		FileReader fr=new FileReader(f);
		JSONTokener jt=new JSONTokener(fr);
		JSONObject jsObj=new JSONObject(jt);	
		response= RestAssured.given().contentType("application/json").body(jsObj.toString())
		.when().post("/api/login");	
		
	}
	
	@Then("The status code should be 200")
	public void verifyLoginStatusCode()
	{
		Assert.assertEquals(200, response.statusCode(),"Status code 200 is not verified");
		LogsManager.getLogger().info("Status code 200 ahs been verified");
	}
	
	@And("The message should be Login Successful")
	public void verifyLoginResponseMessage()
	{
		Assert.assertEquals("Login Successful", response.body().jsonPath().getString("message"),"Login Successfull message is not verified");
		LogsManager.getLogger().info("Login Successfull message has been verified");
		bearerToken=response.body().jsonPath().getString("details.token");
		
	}

}
