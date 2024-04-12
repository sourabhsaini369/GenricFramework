package com.speckyfox.apitest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.speckyfox.base.APIBase;
import com.speckyfox.constants.PathConstants;
import com.speckyfox.listners.AppListeners;
import com.speckyfox.logmanager.LogsManager;
import com.speckyfox.utilities.commonUtilityFunctions.APIUtils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class HighCareAuditAPI extends APIBase {

	String bearerToken = "";
	String user_id = "";
	String audit_id = "";

	@Test(priority = 1)
	public void HicareLogin_API() throws FileNotFoundException {

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

	}

	@Test(priority = 2, dependsOnMethods = { "HicareLogin_API" })
	public void VerifyProfile_API() {

		String loginMessage = "Login Successful";
		String email = "aytse@hicare.in";
		Integer statusCode = 201;
		HashMap<String, Integer> bodyMap = new HashMap<>();
		bodyMap.put("user_id", Integer.parseInt(user_id));

		RequestSpecification reqSpec = APIUtils.setOAuth2Authentication(RestAssured.given(), bearerToken);
		Response response = APIUtils.sendPostRequest("/api/profile", reqSpec.contentType(ContentType.JSON), bodyMap);

		boolean flag = APIUtils.isResponseStatusInRange(response, statusCode, statusCode);
		Assert.assertEquals(flag, true, "Status Code Not Matched: Expected: " + statusCode + " But getting: "
				+ APIUtils.getResponseStatus(response));

		boolean loginFlag = APIUtils.doesResponseBodyContainString(response, loginMessage);
		Assert.assertEquals(loginFlag, true, loginMessage + " message not Confirmed");
		String actualEmail = APIUtils.extractValueFromResponseBody(response, "details.email");
		Assert.assertEquals(actualEmail, email,
				"Email Not Confirmed, Expected: " + email + " but Actual: " + actualEmail);
		LogsManager.getLogger().info("--------OAuthentication has been successful---------");
	}

	@Test(priority = 3, dependsOnMethods = { "HicareLogin_API" })
	public void CreateAudit_API() throws FileNotFoundException {
		File f = new File(PathConstants.apiAuditCreationDataFile);
		FileReader fr = new FileReader(f);
		JSONTokener jt = new JSONTokener(fr);
		JSONObject jsObj = new JSONObject(jt);

		String message = "Audit successfully created";
		String messageAlready = "Audit already created for input Audit date & time";
		Integer statusCode = 200;
		String getFutureDate = APIUtils.getFutureDateAccToTimeZone("GMT+5:30", "yyyy-MM-dd", 2);
		jsObj.put("audit_date", getFutureDate);
		jsObj.put("user_id", Integer.parseInt(user_id));

		RequestSpecification reqSpec = APIUtils.setOAuth2Authentication(RestAssured.given(), bearerToken);
		reqSpec.contentType(ContentType.JSON);
		Response response = APIUtils.sendPostRequest("/api/createaudit", reqSpec, jsObj.toString());
		boolean flag = APIUtils.isResponseStatusInRange(response, statusCode, statusCode);
		Assert.assertEquals(flag, true, "Status Code Not Matched: Expected: " + statusCode + " But getting: "
				+ APIUtils.getResponseStatus(response));

		boolean createAuditFlag = APIUtils.doesResponseBodyContainString(response, message);

		boolean createAlreadyAuditFlag = APIUtils.doesResponseBodyContainString(response, messageAlready);

		if (createAuditFlag) {
			Assert.assertEquals(createAuditFlag, true, message + " message not Confirmed");
		} else {
			Assert.assertEquals(createAlreadyAuditFlag, true, messageAlready + " message not Confirmed");
		}

		LogsManager.getLogger().info("--------Audit Created Successfully---------");

	}

	@Test(priority = 4, dependsOnMethods = { "HicareLogin_API" })
	public void LogOutVerification_API() {

		String logoutMessage = "Logged Out";
		Integer statusCode = 200;

		RequestSpecification reqSpec = APIUtils.setOAuth2Authentication(RestAssured.given(), bearerToken);
		Response response = APIUtils.sendPostRequest("/api/logout", reqSpec.contentType(ContentType.JSON), null);

		boolean flag = APIUtils.isResponseStatusInRange(response, statusCode, statusCode);
		Assert.assertEquals(flag, true, "Status Code Not Matched: Expected: " + statusCode + " But getting: "
				+ APIUtils.getResponseStatus(response));

		boolean logouFlag = APIUtils.doesResponseBodyContainString(response, logoutMessage);
		Assert.assertEquals(logouFlag, true, logoutMessage + " message not Confirmed");
		LogsManager.getLogger().info("--------Logged Out Successfully---------");
	}

//	@Test
//	public void apiDummy() {
//
//		HashMap<String, Integer> map = new HashMap<>();
//
//		map.put("page", 1);
//
//		RequestSpecification reqSpec = RestAssured.given();
//
////		Response resp = reqSpec.contentType("Application/Json").param("page", 1).when().get("/users");
//
//		// Response resp = reqSpec.contentType("Application/Json").queryParam("page",
//		// 2).get("/users");
//
//		Response resp = reqSpec.contentType("Application/Json").queryParam("page", 2).get("/users");
//		// Response resp =
//		// reqSpec.contentType("Application/Json").body(map).when().get(/users");
//
////		resp.then().log().all();
//
////		assertThat(resp.statusCode(), equalTo(201));
//		assertThat(resp.statusCode(), equalTo(200));
//
////		Integer id = resp.getBody().jsonPath().get("data[0].id");
//		Integer id = resp.getBody().jsonPath().get("data.id[0]");
////		List<Integer> id = resp.getBody().jsonPath().get("data.id");
//		System.out.println("Id is: " + id);
//
//		LogsManager.getLogger().info("Response is: " + resp.getBody().asString());
//
//		String ids = resp.jsonPath().getString("data.id");
//		System.out.println("Id's are " + ids);
//
//		Assert.assertTrue(false);
//
//	}

}