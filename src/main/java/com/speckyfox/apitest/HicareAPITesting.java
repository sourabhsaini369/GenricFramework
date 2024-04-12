package com.speckyfox.apitest;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.speckyfox.base.APIBase;
import com.speckyfox.constants.PathConstants;
import com.speckyfox.logmanager.LogsManager;
import com.speckyfox.utilities.commonUtilityFunctions.APIUtils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class HicareAPITesting extends APIBase  {

	String bearerToken = "";
	String user_id = "";
	String audit_id = "";
	
	@Test(priority = 1)
	public void TC001_HicareLogin_API() throws FileNotFoundException {

		File f = new File(PathConstants.apiLoginCredentialFile);
		FileReader fr = new FileReader(f);
		JSONTokener jt = new JSONTokener(fr);
		JSONObject jsObj = new JSONObject(jt);
		Integer statusCode = 200;

		Response res = APIUtils.sendPostRequest("/api/login", RestAssured.given().contentType(ContentType.JSON),
				jsObj.toString());
		boolean flag = APIUtils.isResponseStatusInRange(res, statusCode, statusCode);
		Assert.assertEquals(flag, true, "Status Code Not Matched: Expected: " + statusCode + " But getting: "
				+ APIUtils.getResponseStatus(res));

		user_id = APIUtils.extractValueFromResponseBody(res, "details.user_id");
		bearerToken = APIUtils.extractValueFromResponseBody(res, "details.token");

		boolean stringContains = APIUtils.doesResponseBodyContainString(res, "Login Successful");
		Assert.assertEquals(stringContains, true, "Login Successful message is not verified");
		LogsManager.getLogger().info("The test has been created");

	}

	@Test (priority = 2)
	public void TC002_createAudit() throws FileNotFoundException
	{
		
		File f=new File(PathConstants.auditCreationData);
		FileReader fr=new FileReader(f);
		JSONTokener jt=new JSONTokener(fr);
		JSONObject jsObj=new JSONObject(jt);
		
		Response response=RestAssured.given().auth().oauth2(bearerToken).contentType("application/json").body(jsObj.toString())
				.when().post("/api/createaudit");
		System.err.println("create list "+response.then().log().all());
		
		Assert.assertEquals(200, response.statusCode(),"status code 200 is not verified");
		String message=APIUtils.extractValueFromResponseBody(response, "message");
		
		Assert.assertEquals(message,"Audit successfully created","Audit is not created");
		
	}
	
	
	@Test (priority = 3)
	public void TC003_unconfirmAudit() throws FileNotFoundException
	{
		File f=new File(PathConstants.auditCreationData);
		FileReader fr=new FileReader(f);
		JSONTokener jt=new JSONTokener(fr);
		JSONObject jsObj=new JSONObject(jt);
		JSONObject json=new JSONObject();
		json.put("meeting_date", jsObj.get("audit_date"));
		json.put("user_id", jsObj.get("user_id"));
		
		Response response=RestAssured.given().auth().oauth2(bearerToken).contentType("application/json").body(json.toString())
				.when().post("/api/unconfirmedaudits");
	//	System.err.println("Unconfirm list "+response.then().log().all());
		Assert.assertEquals(200, response.statusCode(),"status code 200 is not verified");
		String message=APIUtils.extractValueFromResponseBody(response, "message");
		
		Assert.assertEquals(message,"List Of Unconfirmed Audits.","Audit is not created");
		
	
	}
}
