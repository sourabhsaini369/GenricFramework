package com.speckyfox.listners;

import java.beans.ExceptionListener;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.testng.*;

import com.speckyfox.dataprovider.ExcelDataProvider;
import com.speckyfox.drivermanager.DriverManager;
import com.speckyfox.enums.EnumConstants.TestinyResultStatus;
import com.speckyfox.logmanager.LogsConfig;
import com.speckyfox.logmanager.LogsManager;
import com.speckyfox.reports.ReportManager;
import com.speckyfox.testinyconfiguration.TestinyConfig;

public class AppListeners extends ExcelDataProvider
		implements ITestListener, IInvokedMethodListener, ExceptionListener, ITestNGListener, IExecutionListener {

	public static Map<String, String> status = new LinkedHashMap<String, String>();
	public static String currentlyExecutingTC;

	private long time;
	public AppListeners() {

	}

	@Override
	public void onTestStart(ITestResult result) {
		String getTestName = getMethodOrScenarioName(result);
		LogsManager.getLogger().info("********************Test Script: " + getTestName + " is Started At: "
				+ new Timestamp(new Date().getTime()) + "********************");
		ReportManager.getReportType().createTest(getTestName);
		time=System.currentTimeMillis();

//		ReportManager.getReportType().setAppOrBrowserName();

	}

	@Override
	public void onTestSuccess(ITestResult result) {
		String getTestName = getMethodOrScenarioName(result);

		try {
			TestinyConfig.getInstance().writeStatusToTestCase(getTestName, TestinyResultStatus.PASSED);
		} catch (Exception e) {

			e.printStackTrace();
		}
		LogsManager.getLogger().info("***************Test Script: " + getTestName + " is passed***************\n\n");

		ReportManager.getReportType().getTestLogger().logTestStatus(result, Optional.of("Pass"),
				Optional.of(Boolean.TRUE));
		
		time=System.currentTimeMillis()-time;
		System.err.println("Passed Execution time is "+time);

	}

	@Override
	public void onTestFailure(ITestResult result) {

		String getTestName = getMethodOrScenarioName(result);
		try {
			TestinyConfig.getInstance().writeStatusToTestCase(getTestName, TestinyResultStatus.FAILED);
			TestinyConfig.getInstance().createCommentInTestCase(getTestName, result.getThrowable().getMessage());
		} catch (Exception e) {

			e.printStackTrace();
		}
		LogsManager.getLogger().error("***************Test Script: " + getTestName + " is failed***************");
		ReportManager.getReportType().getTestLogger().logTestStatus(result, Optional.of("Fail"),
				Optional.of(Boolean.TRUE));

		LogsManager.getLogger().error("Reason of Failure is: " + result.getThrowable() + "\n\n");
		time=System.currentTimeMillis()-time;
		LogsManager.getLogger().error("Execution : " + result.getThrowable() + "\n\n");
		System.err.println("Failed Execution time is "+time);
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		String getTestName = getMethodOrScenarioName(result);
		try {
			TestinyConfig.getInstance().writeStatusToTestCase(getTestName, TestinyResultStatus.SKIPPED);
		} catch (Exception e) {

			e.printStackTrace();
		}
		LogsManager.getLogger().info("***************Test Script: " + getTestName + " is skipped***************\n\n");
		ReportManager.getReportType().getTestLogger().logTestStatus(result, Optional.of("Skipped"), Optional.empty());
		time=System.currentTimeMillis()-time;
		System.err.println("Skip Execution time is "+time);
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
	}

	@Override
	public void onStart(ITestContext context) {

		LogsConfig.config();

	}

	@Override
	public void onFinish(ITestContext context) {

	}

	@Override
	public void afterInvocation(IInvokedMethod arg0, ITestResult arg1) {

	}

	@Override
	public void beforeInvocation(IInvokedMethod arg0, ITestResult result) {
		System.out.println("Executing Method: " + arg0.getTestMethod().getMethodName());
	}

	@Override
	public void exceptionThrown(Exception e) {

		System.err.println("Excepetion Thrown: " + e.getLocalizedMessage());
//CommonLib.failedMethod(e);

	}

	@Override
	public void onExecutionStart() {

		ReportManager.getReportType();
		try {
			Set<String> testNotInTestiny = TestinyConfig.getInstance().getTestCasesAndTestRunMap();
			System.out.println("Test not in Testiny " + testNotInTestiny);
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	@Override
	public void onExecutionFinish() {

		ReportManager.getReportType().closeReport();
		DriverManager.unload();
		TestinyConfig.getTestCasesWhichAreNotPresentInTestiny();

	}

	private static String getScenarioName(ITestResult result) {
		String scenarioName = "";

		try {
			Object[] parameters = result.getParameters();
			String stringParameters = Arrays.toString(parameters);
			String[] lines = stringParameters.split("\n");

			String[] elements;
			if (lines.length == 1) {
				elements = lines[0].substring(1, lines[0].length() - 1).split(", ");
				scenarioName = elements[0].substring(1, elements[0].length() - 1);
			}

		} catch (Exception e) {
			return scenarioName;

		}

		return scenarioName;
	}

	public static String getMethodOrScenarioName(ITestResult result) {
		currentlyExecutingTC = result.getMethod().getConstructorOrMethod().getName();
		if (currentlyExecutingTC.equals("runScenario")) {
			currentlyExecutingTC = getScenarioName(result);
		}

		return currentlyExecutingTC;

	}

}
