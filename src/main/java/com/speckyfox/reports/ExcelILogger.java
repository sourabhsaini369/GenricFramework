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



import com.speckyfox.constants.PathConstants;
import com.speckyfox.drivermanager.DriverManager;
import com.speckyfox.enums.EnumConstants.excelLabel;
import com.speckyfox.utilities.excelUtilityFunctions.ExcelUtils;

public class ExcelILogger implements ILogger {

	private static final Object lock = new Object();
	static final String path = PathConstants.testCasesExcelPath;
	static final String sheetName = PathConstants.testCasesExcelSheetName;

	private final ExcelIReport getExcelIReport = (ExcelIReport) ReportManager.getReportType();
	String LogInfoOfTest = "";
	String LogWarningOfTest = "";
	private static ExcelILogger instance = null;

	private ExcelILogger() {

	}

	@Override
	public void info(String message) {
//		synchronized (lock) {
//
//			try {
//				LogInfoOfTest = ExcelUtils.readData(getExcelIReport.getWorkbook(), path, sheetName,
//						excelLabel.Test_Cases, getExcelIReport.getTest(), excelLabel.Logs);
//
//				if (LogInfoOfTest != null) {
//					LogInfoOfTest = (LogInfoOfTest + "INFO: " + message) + "\n";
//
//					ExcelUtils.writeData(getExcelIReport.getWorkbook(), path, LogInfoOfTest, sheetName,
//							excelLabel.Test_Cases, getExcelIReport.getTest(), excelLabel.Logs);
//				} else {
//
//					System.out.println("Either No Test Case Named: " + getExcelIReport.getTest()
//							+ " found in Excel or No Column Found Named: " + excelLabel.Logs);
//				}
//
//			} catch (Exception e) {
//
//				e.printStackTrace();
//
//			}
//		}
	}

	@Override
	public void warning(String message) {
//		synchronized (lock) {
//
//			try {
//				LogWarningOfTest = ExcelUtils.readData(getExcelIReport.getWorkbook(), path, sheetName,
//						excelLabel.Test_Cases, getExcelIReport.getTest(), excelLabel.Logs);
//
//				if (LogWarningOfTest != null) {
//					LogWarningOfTest = (LogWarningOfTest + "WARNING: " + message) + "\n";
//
//					ExcelUtils.writeData(getExcelIReport.getWorkbook(), path, LogWarningOfTest, sheetName,
//							excelLabel.Test_Cases, getExcelIReport.getTest(), excelLabel.Logs);
//				} else {
//
//					System.out.println("Either No Test Case Named: " + getExcelIReport.getTest()
//							+ " found in Excel or No Column Found Named: " + excelLabel.Logs);
//				}
//
//			} catch (Exception e) {
//
//				e.printStackTrace();
//
//			}
//		}

	}

	@Override
	public void logTestStatus(ITestResult status, Optional<String> message, Optional<Boolean> screenshot) {

		synchronized (lock) {
			String messageValue = message.orElse("");
			Boolean screenshotFlag = screenshot.orElse(false);

			try {
				String statusOfTest = ExcelUtils.readData(getExcelIReport.getWorkbook(), path, sheetName,
						excelLabel.Test_Cases, getExcelIReport.getTest(), excelLabel.Status);

				if (statusOfTest != null) {

					if (ITestResult.SUCCESS == status.getStatus()) {

						if (screenshotFlag) {
							captureScreenshot();
						}

						String dataToWrite = "Pass: " + messageValue;

						dataToWrite = statusOfTest + "\n" + dataToWrite;

						ExcelUtils.writeData(getExcelIReport.getWorkbook(), path, dataToWrite, sheetName,
								excelLabel.Test_Cases, getExcelIReport.getTest(), excelLabel.Status);
					} else if (ITestResult.FAILURE == status.getStatus()) {
						Throwable throwable = status.getThrowable();

						if (throwable != null) {

							if (screenshotFlag) {
								captureScreenshot();
							}
							String dataToWrite = "Fail: " + messageValue + " and Issue Found: "
									+ throwable.getMessage();

							dataToWrite = statusOfTest + "\n" + dataToWrite;

							ExcelUtils.writeData(getExcelIReport.getWorkbook(), path, dataToWrite, sheetName,
									excelLabel.Test_Cases, getExcelIReport.getTest(), excelLabel.Status);

						}
					}

					else if (ITestResult.SKIP == status.getStatus()) {

						Throwable throwable = status.getThrowable();

						if (throwable != null) {
							String dataToWrite = "Skip: " + messageValue + " and Reason Occured: "
									+ throwable.getMessage();

							dataToWrite = statusOfTest + "\n" + dataToWrite;

							ExcelUtils.writeData(getExcelIReport.getWorkbook(), path, dataToWrite, sheetName,
									excelLabel.Test_Cases, getExcelIReport.getTest(), excelLabel.Status);
						}

					}

				} else {

					System.out.println("Either No Test Case Named: " + getExcelIReport.getTest()
							+ " found in Excel or No Column Found Named: " + excelLabel.Status);
				}

			} catch (Exception e) {

				e.printStackTrace();

			}

		}

	}

	public static ILogger getInstance() {

		synchronized (lock) {

			if (instance == null) {
				instance = new ExcelILogger();
			}
			return instance;

		}

	}

	private void captureScreenshot() {

		{
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
			String timeStamp = dateFormat.format(new Date());
			TakesScreenshot ts = (TakesScreenshot) DriverManager.getDriver();

			if (ts != null) {

				File screenshot = ts.getScreenshotAs(OutputType.FILE);
				String capturedScreenshotName = "Capture" + timeStamp + ".png";
				Path screenshotPath = Paths.get(PathConstants.screenshotPath, capturedScreenshotName);
				try {
					Files.copy(screenshot.toPath(), screenshotPath);

//				int columnNumber = ExcelUtils.getColumnNumberBasedOnLabel(getExcelIReport.getWorkbook(), path,
//						sheetName, excelLabel.Screenshot);
//				int rowNumber = ExcelUtils.getRowNumberBasedOnLabelAndValue(getExcelIReport.getWorkbook(), path,
//						sheetName, excelLabel.Test_Cases, getExcelIReport.getTest());

//				ExcelUtils.addImageReferenceToCell(sheetName, columnNumber, rowNumber, screenshotPath.toString(),
//						getExcelIReport.getWorkbook(), path);

				} catch (IOException e) {
					
					
				}

			}

		}
	}

}
