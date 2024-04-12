package com.speckyfox.reports;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.speckyfox.constants.PathConstants;
import com.speckyfox.drivermanager.DriverManager;
import com.speckyfox.enums.EnumConstants.excelLabel;
import com.speckyfox.utilities.excelUtilityFunctions.ExcelUtils;

public class ExcelIReport implements IReport {

	private static ExcelIReport instance;
	private static final Object lock = new Object();

	static String path = PathConstants.testCasesExcelPath;
	static String sheetName = PathConstants.testCasesExcelSheetName;
	static FileInputStream fis;

	public static Workbook wb;

	static Map<Integer, String> excelTestMap = new HashMap<>();

	private ExcelIReport() {

		synchronized (lock) {

			try {

				if (wb == null) {
					fis = new FileInputStream(new File(path));
					wb = WorkbookFactory.create(fis);
					fis.close();

					ExcelUtils.clearColumn(wb, sheetName, path, excelLabel.Status);
//					ExcelUtils.clearColumn(wb, sheetName, path, excelLabel.Logs);
					ExcelUtils.clearColumn(wb, sheetName, path, excelLabel.Screenshot);
//					ExcelUtils.clearColumn(wb, sheetName, path, excelLabel.BrowserOrAppName);
					wb.close();
					fis = new FileInputStream(new File(path));
					wb = WorkbookFactory.create(fis);
					fis.close();

				}

			} catch (Exception e) {
				e.printStackTrace();

			}
		}

	}

	@Override
	public void createTest(String method) {

		synchronized (lock) {

			excelTestMap.put((int) Thread.currentThread().getId(), method);
			System.out.println("Create Test Excel Report Current Thread: " + (int) Thread.currentThread().getId()
					+ " and Test Correspond: " + excelTestMap.get((int) Thread.currentThread().getId()));

		}

	}

	@Override
	public void closeReport() {

		try {
			wb.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static IReport getInstance() {

		if (instance == null) {
			instance = new ExcelIReport();
		}
		System.out.println("IReport Thread ID: " + instance.hashCode());
		return instance;

	}

	@Override
	public ILogger getTestLogger() {

		return ExcelILogger.getInstance();

	}

	@SuppressWarnings("deprecation")

	public String getTest() {

		synchronized (lock) {
			return excelTestMap.get((int) Thread.currentThread().getId());
		}

	}

	public Workbook getWorkbook() {

		synchronized (lock) {
			return wb;
		}

	}

	@Override
	public void setAppOrBrowserName() {

		synchronized (lock) {
			String appNameOrBrowserName = getAppOrBrowserName(DriverManager.getDriver());

			try {
				String getBrowserOrAppNameFromExcel = ExcelUtils.readData(getWorkbook(), path, sheetName,
						excelLabel.Test_Cases, getTest(), excelLabel.BrowserOrAppName);

				if (getBrowserOrAppNameFromExcel != null) {
					getBrowserOrAppNameFromExcel = appNameOrBrowserName;

					ExcelUtils.writeData(getWorkbook(), path, getBrowserOrAppNameFromExcel, sheetName,
							excelLabel.Test_Cases, getTest(), excelLabel.BrowserOrAppName);
				} else {

					System.out.println("Either No Test Case Named: " + getTest()
							+ " found in Excel or No Column Found Named: " + excelLabel.BrowserOrAppName);
				}

			} catch (Exception e) {

				e.printStackTrace();

			}
		}

	}

}
