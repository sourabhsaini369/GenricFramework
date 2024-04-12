package com.speckyfox.testinyconfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import com.speckyfox.TestCasesData.TestCasesDataSourceManager;
import com.speckyfox.constants.ConfigConstants;
import com.speckyfox.constants.PathConstants;
import com.speckyfox.drivermanager.DriverManager;
import com.speckyfox.enums.EnumConstants.Framework;
import com.speckyfox.enums.EnumConstants.TestinyResultStatus;
import com.speckyfox.frameworkfactory.FrameworkFactory;
import com.speckyfox.logmanager.LogsManager;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class TestinyConfig {

	private static final Object lock = new Object();
	private static TestinyConfig instance;
	private RequestSpecBuilder reqSpecBuilder;
	private static RequestSpecification reqSpec;
	private static Set<String> methodsNotPresentInTestiny;

	private static int getTestRunID;
	private static HashMap<String, Integer> testAllCasesAndTheirIds;
	private static int prjectID = -1;

	private TestinyConfig() {
		reqSpecBuilder = new RequestSpecBuilder();
		reqSpec = reqSpecBuilder.setBaseUri("https://app.testiny.io/api/v1")
				.addHeader("X-API-KEY", ConfigConstants.testinyAPIKeyValue).build();
	}

	public static TestinyConfig getInstance() {

		synchronized (lock) {

			if (instance == null) {
				instance = new TestinyConfig();
			}
			return instance;

		}

	}

	private static int getProjectID(String expectedProjectName) throws Exception {

		if (prjectID != -1) {
			return prjectID;
		} else {
			Integer index = -1;

			Integer getID = -1;

			Response resp = RestAssured.given().spec(reqSpec).when().get("/project");

			List<Object> getNamesOfProjects = resp.jsonPath().getList("data.name");

			Integer loopCount = 0;
			for (Object projectName : getNamesOfProjects) {

				if (expectedProjectName.equalsIgnoreCase(projectName.toString())) {

					index = loopCount;

				}

				loopCount++;
			}

			if (!index.equals(-1)) {

				getID = resp.jsonPath().getInt("data.id[" + index + "]");
			} else {

				throw new Exception("No Project found named: " + expectedProjectName + " in testiny");
			}

			return getID;
		}
	}

	private static int createTestRun(String expectedProjectName) throws Exception {
		DateFormat dateFormat = new SimpleDateFormat("yy_MM_dd_HH_mm_ss");
		Date date = new Date();
		String testRunName = "SpeckyRun";

		String requestBody = "[{\"id\":0,\"title\":\"" + testRunName + dateFormat.format(date)
				+ "\",\"is_deleted\":false,\"created_at\":\"1970-01-01T00:00:00.000Z\",\"created_by\":0,\"modified_by\":0,\"deleted_by\":0,\"_etag\":\"\",\"project_id\":"
				+ getProjectID(expectedProjectName)
				+ ",\"is_closed\":false,\"description\":\"{\\\"v\\\":1,\\\"t\\\":\\\"slate\\\",\\\"c\\\":[{\\\"t\\\":\\\"p\\\",\\\"children\\\":[{\\\"text\\\":\\\"\\\"}]}]}\",\"testCaseIds\":{}}]";

		Response resp = RestAssured.given().contentType(ContentType.JSON).spec(reqSpec).body(requestBody).when()
				.post("/testrun/bulk");
		resp.then().log().all();
		return getTestRunID = resp.jsonPath().getInt("id[0]");
	}

	private static HashMap<String, Integer> getTestCasesAndTheirIDs() throws Exception {

		HashMap<String, Integer> testCaseAndID = new HashMap<String, Integer>();
		Response resp = RestAssured.given().spec(reqSpec).when().get("/testcase");
		List<String> includeMethods = TestCasesDataSourceManager.getTestCasesDataSource().includeMethods();

		List<String> getMethodsFromTestiny = resp.jsonPath().getList("data.title");
		methodsNotPresentInTestiny = new HashSet<String>();
		JsonPath jsonPath = new JsonPath(resp.getBody().asString());
		testAllCasesAndTheirIds = new HashMap<String, Integer>();
		for (String methodName : getMethodsFromTestiny) {
			testAllCasesAndTheirIds.put(methodName,
					jsonPath.getInt("data.find { it.title == '" + methodName + "' }.id"));
		}

		for (String includeMethod : includeMethods) {
			if (getMethodsFromTestiny.contains(includeMethod)) {
				testCaseAndID.put(includeMethod,
						jsonPath.getInt("data.find { it.title == '" + includeMethod + "' }.id"));

			} else {
				methodsNotPresentInTestiny.add(includeMethod);
			}

		}

		return testCaseAndID;

	}

	public static Set<String> getTestCasesAndTestRunMap() throws Exception {
		String expectedProjectName = ConfigConstants.testinyProjectName;
		String body;
		HashMap<String, Integer> testAndID = getTestCasesAndTheirIDs();
		Integer testRunID = createTestRun(expectedProjectName);
		Thread.sleep(15000);

		if (FrameworkFactory.getInstance().getFrameworkEnum().equals(Framework.DATADRIVEN)) {
			for (Integer id : testAndID.values()) {
				body = "[{\"ids\":{\"testcase_id\":" + id + ",\"testrun_id\":" + testRunID
						+ "},\"mapped\":{\"assigned_to\":\"OWNER\",\"result_status\":\"NOTRUN\"}}]";
				System.out.println(RestAssured.given().header("Content-Type", "application/json").spec(reqSpec)
						.body(body).when().post("/testrun/mapping/bulk/testcase:testrun?op=add").getStatusCode());

			}
		}

		return methodsNotPresentInTestiny;
	}

	public void writeStatusToTestCase(String methodName, TestinyResultStatus status) throws Exception {

		if (testAllCasesAndTheirIds.containsKey(methodName)) {

			String body = "[{\"ids\":{\"testcase_id\":" + testAllCasesAndTheirIds.get(methodName) + ",\"testrun_id\":"
					+ getTestRunID + "},\"mapped\":{\"assigned_to\":\"OWNER\",\"result_status\":\"" + status + "\"}}]";
			System.out.println(RestAssured.given().header("Content-Type", "application/json").spec(reqSpec).body(body)
					.when().post("/testrun/mapping/bulk/testcase:testrun?op=add_or_update").getStatusCode());
		} else {
			LogsManager.getLogger().error("As the Method: " + methodName
					+ " is not present in testiny, So Not Able to update the Test Result in Testiny");
			methodsNotPresentInTestiny.add(methodName);
		}

	}

	public static void getTestCasesWhichAreNotPresentInTestiny() {
		if (!methodsNotPresentInTestiny.isEmpty()) {
			LogsManager.getLogger().error("As the Methods: " + methodsNotPresentInTestiny
					+ " are not present in testiny, So Not Able to update the Test Result in Testiny");
		}
	}

	private static String createBlobBulk(String fileName, String fileToUploadBase64Format, String expectedProjectName)
			throws Exception {

		String requestBody = "[{\"is_deleted\":false,\"project_id\":" + getProjectID(expectedProjectName)
				+ ",\"created_at\":\"1970-01-01T00:00:00.000Z\",\"created_by\":0,\"modified_at\":\"1970-01-01T00:00:00.000Z\",\"modified_by\":0,\"deleted_at\":\"1970-01-01T00:00:00.000Z\",\"deleted_by\":0,\"blob_id\":\"\",\"mime_type\":\"image\\/png\",\"filename\":\""
				+ fileName + "\",\"size\":0,\"data\":\"" + fileToUploadBase64Format + "\"}]";

		Response resp = RestAssured.given().contentType(ContentType.JSON).spec(reqSpec).body(requestBody).when()
				.post("/blob/bulk");
		resp.then().log().all();

		return resp.jsonPath().get("id[0]") + "|" + resp.jsonPath().get("blob_id[0]") + "||"
				+ resp.jsonPath().get("filename[0]") + "|" + resp.jsonPath().get("mime_type[0]") + "|"
				+ resp.jsonPath().get("size[0]");

	}

	private static int createCommentForBlob(String expectedProjectName, String textValue) throws Exception {

		String requestBody = "[{\"is_deleted\":false,\"created_at\":\"1970-01-01T00:00:00.000Z\",\"created_by\":0,\"modified_by\":0,\"deleted_by\":0,\"project_id\":"
				+ getProjectID(expectedProjectName)
				+ ",\"parent_id\":0,\"status\":\"OPEN\",\"type\":\"ATTACHMENT\",\"text\":\"" + textValue + "\"}]";

		Response resp = RestAssured.given().contentType(ContentType.JSON).spec(reqSpec).body(requestBody).when()
				.post("/comment/bulk");
		resp.then().log().all();
		return resp.jsonPath().getInt("id[0]");
	}

	private static int createComment(String expectedProjectName, String textValue) throws Exception {
		textValue = textValue.replaceAll("(?<!\\\\)(\\\\\\\\)*\\\\(?!\\\\)", "\\\\\\\\").replaceAll("[ \\t\\r\\n]+",
				" ");
		;
		System.out.println("Request Body: " + textValue);
		String requestBody = "[{\"is_deleted\":false,\"created_at\":\"1970-01-01T00:00:00.000Z\",\"created_by\":0,\"modified_by\":0,\"deleted_by\":0,\"project_id\":"
				+ getProjectID(expectedProjectName)
				+ ",\"parent_id\":0,\"status\":\"OPEN\",\"type\":\"TEXT\",\"text\":\"" + textValue + "\"}]";

		Response resp = RestAssured.given().contentType(ContentType.JSON).spec(reqSpec).body(requestBody).when()
				.post("/comment/bulk");

		resp.then().log().all();

		return resp.jsonPath().getInt("id[0]");

	}

	private static void createCommentMapping(int commentID, String methodName) throws Exception {

		if (testAllCasesAndTheirIds.containsKey(methodName)) {

			String requestBody = "[{\"ids\":{\"comment_id\":" + commentID + ",\"testcase_id\":"
					+ testAllCasesAndTheirIds.get(methodName) + ",\"testrun_id\":" + getTestRunID + "}}]";

			RestAssured.given().contentType(ContentType.JSON).spec(reqSpec).body(requestBody).when()
					.post("/comment/mapping/bulk/comment:testcase:testrun?op=add").then().statusCode(200);

		}
	}

	private static Path captureScreenshot() {

		{
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
			String timeStamp = dateFormat.format(new Date());
			TakesScreenshot ts = (TakesScreenshot) DriverManager.getDriver();

			if (ts != null) {
				File screenshot = ts.getScreenshotAs(OutputType.FILE);
				Path screenshotPath = Paths.get(PathConstants.screenshotPath, "Capture" + timeStamp + ".png");
				try {
					Files.copy(screenshot.toPath(), screenshotPath);
					return screenshotPath;
				} catch (IOException e) {

					e.printStackTrace();

				}

			}

			return null;

		}
	}

	private static String fileToBase64(String filePath) {
		File file = new File(filePath);

		try (FileInputStream fis = new FileInputStream(file)) {
			byte[] fileBytes = new byte[(int) file.length()];
			fis.read(fileBytes);

			String base64String = Base64.getEncoder().encodeToString(fileBytes);
//			System.out.println("Base64 encoded string: " + base64String);
			return base64String;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;

	}

	public void createCommentInTestCase(String methodName, String failureMessage) {
		try {
			String expectedProjectName = ConfigConstants.testinyProjectName;
			Path path = captureScreenshot();
			if (path != null) {
				String base64Format = fileToBase64(path.toString());
				String textValue = createBlobBulk(path.getFileName().toString(), base64Format, expectedProjectName);
				int commentIDForBlob = createCommentForBlob(expectedProjectName, textValue);

				createCommentMapping(commentIDForBlob, methodName);

				int commentID = createComment(expectedProjectName, failureMessage);
				createCommentMapping(commentID, methodName);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
