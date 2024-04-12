package com.speckyfox.base;


import org.testng.annotations.*;


import com.speckyfox.constants.ConfigConstants;
import com.speckyfox.dataprovider.ExcelDataProvider;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.restassured.RestAssured;

public class APIBase extends ExcelDataProvider {

	@Before("@ApiHook")
	@BeforeMethod
	public void apiSetup() {

		RestAssured.baseURI = ConfigConstants.apiBaseURL;

	}

	@After("@ApiHook")
	@AfterMethod
	public void apiTearDown() {
		RestAssured.reset();
	}

}
