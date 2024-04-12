package com.speckyfox.utilities.commonUtilityFunctions;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

import com.speckyfox.logmanager.LogsManager;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class APIUtils {

	private APIUtils() {

	}

	public static Response sendGetRequest(String endpoint) {
		logRequest("GET", endpoint);
		return RestAssured.get(endpoint).then().extract().response();
	}

	public static Response sendDeleteRequest(String endpoint) {
		logRequest("DELETE", endpoint);
		return RestAssured.delete(endpoint).then().extract().response();
	}

	public static Response sendHeadRequest(String endpoint) {
		logRequest("HEAD", endpoint);
		return RestAssured.head(endpoint).then().extract().response();
	}

	private static void logRequest(String method, String endpoint) {
		LogsManager.getLogger().info("Sending " + method + " request to: " + endpoint);
	}

	public static String getResponseBody(Response response) {
		logResponse("Response Body: " + response.getBody().asString());
		return response.getBody().asString();
	}

	public static String extractValueFromResponseBody(Response response, String jsonPath) {
		String value = response.getBody().jsonPath().getString(jsonPath);
		logResponse("Extracted value from response body with JSONPath \"" + jsonPath + "\": " + value);
		return value;
	}

	public static boolean doesValueExistInResponseBody(Response response, String jsonPath) {
		boolean exists = response.getBody().jsonPath().get(jsonPath) != null;
		logResponse("Value exists in response body with JSONPath \"" + jsonPath + "\": " + exists);
		return exists;
	}

	public static boolean isResponseContentTypeJson(Response response) {
		boolean isJson = response.contentType().equalsIgnoreCase("application/json");
		logResponse("Response Content Type is JSON: " + isJson);
		return isJson;
	}

	public static int getResponseStatus(Response response) {
		int statusCode = response.getStatusCode();
		logResponse("Response Status Code: " + statusCode);
		return statusCode;
	}

	public static boolean isResponseStatusInRange(Response response, int minStatusCode, int maxStatusCode) {
		int statusCode = response.getStatusCode();
		boolean inRange = statusCode >= minStatusCode && statusCode <= maxStatusCode;
		logResponse("Response Status Code is in range [" + minStatusCode + ", " + maxStatusCode + "]: " + inRange);
		logResponse("Response Status Code is " + statusCode);
		return inRange;
	}

	public static boolean isResponseSuccessful(Response response) {
		int statusCode = response.getStatusCode();
		boolean isSuccess = statusCode >= 200 && statusCode < 300;
		logResponse("Response Status Code indicates success: " + isSuccess);
		return isSuccess;
	}

	public static boolean doesResponseBodyContainString(Response response, String searchString) {
		String responseBody = response.getBody().asString();
		boolean containsString = responseBody.contains(searchString);
		logResponse("Response Body contains \"" + searchString + "\": " + containsString);
		return containsString;
	}

	public static boolean doesResponseBodyMatchRegex(Response response, String regex) {
		String responseBody = response.getBody().asString();
		boolean matchesRegex = responseBody.matches(regex);
		logResponse("Response Body matches regex \"" + regex + "\": " + matchesRegex);
		return matchesRegex;
	}

	public static boolean doesResponseHeaderExist(Response response, String headerName) {
		boolean headerExists = response.getHeader(headerName) != null;
		logResponse("Response Header \"" + headerName + "\" exists: " + headerExists);
		return headerExists;
	}

	public static boolean isResponseHeaderValueEqualTo(Response response, String headerName, String expectedValue) {
		String actualValue = response.getHeader(headerName);
		boolean valueMatches = actualValue != null && actualValue.equals(expectedValue);
		logResponse("Response Header \"" + headerName + "\" value is \"" + expectedValue + "\": " + valueMatches);
		return valueMatches;
	}

	public static boolean isResponseTimeWithinRange(Response response, long minResponseTime, long maxResponseTime) {
		long responseTime = response.getTime();
		boolean withinRange = responseTime >= minResponseTime && responseTime <= maxResponseTime;
		logResponse(
				"Response Time is within range [" + minResponseTime + "ms, " + maxResponseTime + "ms]: " + withinRange);
		return withinRange;
	}

	// Method to log the response details
	private static void logResponse(String message) {
		LogsManager.getLogger().info(message);
	}

	public static Response sendPostRequest(String endpoint, RequestSpecification reqSpec, Object requestBody) {
		logRequest("POST", endpoint, reqSpec, requestBody);
		if (requestBody instanceof String) {
			return RestAssured.given().spec(reqSpec).body((String) requestBody).post(endpoint).then().extract()
					.response();
		} else if (requestBody instanceof File) {
			return RestAssured.given().spec(reqSpec).multiPart((File) requestBody).post(endpoint).then().extract()
					.response();
		} else if (requestBody instanceof Map) {
			return RestAssured.given().spec(reqSpec).body(requestBody).post(endpoint).then().extract().response();
		}

		else {
			return RestAssured.given().spec(reqSpec).post(endpoint).then().extract().response();
		}
	}

	public static Response sendPutRequest(String endpoint, RequestSpecification reqSpec, Object requestBody) {
		logRequest("PUT", endpoint, reqSpec, requestBody);
		if (requestBody instanceof String) {
			return RestAssured.given().spec(reqSpec).body((String) requestBody).put(endpoint).then().extract()
					.response();
		} else if (requestBody instanceof File) {
			return RestAssured.given().spec(reqSpec).multiPart((File) requestBody).put(endpoint).then().extract()
					.response();
		} else {
			return RestAssured.given().spec(reqSpec).put(endpoint).then().extract().response();
		}
	}

	public static Response sendPatchRequest(String endpoint, RequestSpecification reqSpec, Object requestBody) {
		logRequest("PATCH", endpoint, reqSpec, requestBody);
		if (requestBody instanceof String) {
			return RestAssured.given().spec(reqSpec).body((String) requestBody).patch(endpoint).then().extract()
					.response();
		} else if (requestBody instanceof File) {
			return RestAssured.given().spec(reqSpec).multiPart((File) requestBody).patch(endpoint).then().extract()
					.response();
		} else {
			return RestAssured.given().spec(reqSpec).patch(endpoint).then().extract().response();
		}
	}

	private static void logRequest(String method, String endpoint, RequestSpecification reqSpec, Object requestBody) {
		LogsManager.getLogger().info("Sending " + method + " request to: " + endpoint);
		if (reqSpec != null) {
			LogsManager.getLogger().info("Request Specification: " + reqSpec.log().all().toString());
		}
		if (requestBody instanceof String) {
			LogsManager.getLogger().info("Request Body: " + requestBody);
		} else if (requestBody instanceof File) {
			LogsManager.getLogger().info("Request Body: File - " + ((File) requestBody).getPath());
		}
	}

	public static RequestSpecification setBasicAuthentication(RequestSpecification reqSpec, String username,
			String password) {
		return reqSpec.auth().basic(username, password);
	}

	public static RequestSpecification setOAuth2Authentication(RequestSpecification reqSpec, String accessToken) {
		return reqSpec.auth().oauth2(accessToken);
	}

	/**
	 * 
	 * @author Ankur Huria
	 * @description- This method is used to get Future date according to the time
	 *               zone.
	 */
	public static String getFutureDateAccToTimeZone(String timeZone, String format, int daysToAdd) {
		try {
			DateFormat formatter = new SimpleDateFormat(format);
			formatter.setTimeZone(TimeZone.getTimeZone(timeZone));

			Calendar cal = Calendar.getInstance();
			try {
				cal.setTime(formatter.parse((formatter.format(new Date()))));
			} catch (ParseException e) {
				e.printStackTrace();
			}

			// use add() method to add the days to the given date
			cal.add(Calendar.DAY_OF_MONTH, daysToAdd);
			return formatter.format(cal.getTime());

		} catch (Exception e) {
			return null;
		}
	}

}
