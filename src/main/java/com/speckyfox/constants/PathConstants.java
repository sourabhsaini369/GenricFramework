package com.speckyfox.constants;

public class PathConstants {

	public static final String testCasesExcelPath = System.getProperty("user.dir")
			+ "\\src\\test\\resources\\TestCaseExcelFile\\TestCases.xlsx";

	public static final String configPropertyFilePath = System.getProperty("user.dir")
			+ "\\src\\test\\resources\\ConfigurationFiles\\config.properties";

	public static final String androidAppPath = System.getProperty("user.dir") + "/android-app.apk";
	public static final String iosAppPath = System.getProperty("user.dir") + "/ios-app.zip";
	public static final String testCasesExcelSheetName = "TestCases";
	public static final String screenshotPath = System.getProperty("user.dir") + "\\Screenshots";
	public static final String extentReportPath = System.getProperty("user.dir") + "\\Reports\\ExtentReports";
	public static final String jsonTestCasesFilePath = System.getProperty("user.dir")
			+ "/src/test/resources/TestCaseJsonFile/TestCases.json";

	public static final String jsonTestModulesFilePath = System.getProperty("user.dir")
			+ "/src/test/resources/TestCaseJsonFile/TestCasesModules.json";
	
	public static final String testDataExcelPath = System.getProperty("user.dir")
			+ "\\src\\test\\resources\\TestData\\TestData.xlsx";
	
	public static final String testDataSheetWebTable = "WebTable";
	public static final String testDataSheetRegistration = "Registration";
	
	public static final String logFileName = System.getProperty("user.dir").replace("\\", "/") + "/logs/"
			+ "SpeckyLogs_";
	
	public static final String log4jConfigFileName = "log4j.properties";
	
	public static final String apiLoginCredentialFile = ".\\src\\test\\resources\\HicareData\\loginCredential.json";
	public static final String apiAuditCreationDataFile = ".\\src\\test\\resources\\HicareData\\auditCreationData.json";
	
	public static final String nodeJSMainFilePath = "\\AppData\\Roaming\\npm\\node_modules\\appium\\build\\lib\\main.js";
	public static final String scriptVideosFolderPath = "\\ScriptVideos\\";
	
	
	
	// JSON file path
	public static final String loginCredential=System.getProperty("user.dir")
	+ "\\src\\test\\resources\\TestCaseJsonFile\\loginCredential.json";
	
	public static final String auditCreationData=System.getProperty("user.dir")
			+ "\\src\\test\\resources\\TestCaseJsonFile\\auditCreationData.json";
	
	
	

}
