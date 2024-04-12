package com.speckyfox.reports;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.speckyfox.constants.PathConstants;
import com.speckyfox.drivermanager.DriverManager;

public class ExtentIReport implements IReport {

	private static ExtentIReport instance;
	private static final Object lock = new Object();
	private static ExtentReports extent;

	static Map<Integer, ExtentTest> extentTestMap = new HashMap<>();

	private ExtentIReport() {

		synchronized (lock) {

			if (extent == null)

			{
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
				String timeStamp = dateFormat.format(new Date());

				ExtentSparkReporter spark = new ExtentSparkReporter(
						PathConstants.extentReportPath + "\\Report_" + timeStamp + ".html");
				extent = new ExtentReports();
				extent.attachReporter(spark);
				extent.setSystemInfo("Environment", "QA");
				extent.setSystemInfo("OS", "Windows 10");
			}

		}

	};

	@Override
	public void createTest(String method) {

		synchronized (lock) {
			ExtentTest extentTest = extent.createTest(method);
			extentTestMap.put((int) Thread.currentThread().getId(), extentTest);
		}

	}

	public ExtentTest getTest() {

		synchronized (lock) {
			return extentTestMap.get((int) Thread.currentThread().getId());
		}

	}

	@Override
	public void closeReport() {

		if (extent != null) {
			extent.flush();
		}

	}

	public static IReport getInstance() {

		synchronized (lock) {

			if (instance == null) {
				instance = new ExtentIReport();
			}
			return instance;

		}

	}

	@Override
	public ILogger getTestLogger() {

		synchronized (lock) {
			return ExtentILogger.getInstance();
		}
	}

	@Override
	public void setAppOrBrowserName() {

		synchronized (lock) {
			String appNameOrBrowserName = getAppOrBrowserName(DriverManager.getDriver());
			getTest().assignDevice(appNameOrBrowserName);
		}

	}

}
