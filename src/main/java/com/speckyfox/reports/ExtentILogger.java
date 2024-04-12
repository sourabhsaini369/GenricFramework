package com.speckyfox.reports;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.*;



import com.aventstack.extentreports.MediaEntityBuilder;
import com.speckyfox.constants.PathConstants;
import com.speckyfox.drivermanager.DriverManager;

public class ExtentILogger implements ILogger {

	private static final Object lock = new Object();

	private ExtentIReport getExtentIReport = (ExtentIReport) ReportManager.getReportType();
	private static ExtentILogger instance = null;

	private ExtentILogger() {
	}

	@Override
	public void info(String message) {
		getExtentIReport.getTest().info(message);

	}

	@Override
	public void warning(String message) {
		getExtentIReport.getTest().warning(message);

	}

	@Override
	public void logTestStatus(ITestResult status, Optional<String> message, Optional<Boolean> screenshot) {

		synchronized (lock) {
			String messageValue = message.orElse("");
			Boolean screenshotFlag = screenshot.orElse(false);
			if (ITestResult.SUCCESS == status.getStatus()) {
				getExtentIReport.getTest().pass(messageValue);
			} else if (ITestResult.FAILURE == status.getStatus()) {
				Throwable throwable = status.getThrowable();

				if (throwable != null) {

					if (screenshotFlag) {
						captureScreenshot();
					}
					getExtentIReport.getTest().fail(throwable.getMessage());

				}
			}

			else if (ITestResult.SKIP == status.getStatus()) {

				Throwable throwable = status.getThrowable();

				if (throwable != null) {
					getExtentIReport.getTest().skip(throwable.getMessage());
				}

			}

		}

	}

	private void captureScreenshot() {

		{
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
			String timeStamp = dateFormat.format(new Date());
			TakesScreenshot ts = (TakesScreenshot) DriverManager.getDriver();

			if (ts != null) {
				File screenshot = ts.getScreenshotAs(OutputType.FILE);
				Path screenshotPath = Paths.get(PathConstants.screenshotPath, "Capture" + timeStamp + ".png");
				try {
					Files.copy(screenshot.toPath(), screenshotPath);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				getExtentIReport.getTest()
						.fail(MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath.toString()).build());
			}

		}
	}

	public static ILogger getInstance() {

		synchronized (lock) {

			if (instance == null) {
				instance = new ExtentILogger();
			}
			return instance;

		}

	}

}
