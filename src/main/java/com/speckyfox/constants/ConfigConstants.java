package com.speckyfox.constants;

import com.speckyfox.configreader.ConfigReader;

public class ConfigConstants {
	
	
	
	public static final String testingFramework = ConfigReader.getMainPropertyFile().getProperty("testing.Framework");
	public static final String testingTestCasesDataSource = ConfigReader.getMainPropertyFile().getProperty("datadriven.TestCasesDataSource");
	public static final String testingReportType = ConfigReader.getMainPropertyFile().getProperty("testing.ReportType");
	public static final String webBrowser = ConfigReader.getMainPropertyFile().getProperty("webBrowser");
	public static final String webRunMode = ConfigReader.getMainPropertyFile().getProperty("webRunMode");
	public static final String runModeMobile = ConfigReader.getMainPropertyFile().getProperty("runModeMobile");
	public static final String webRemoteModeType = ConfigReader.getMainPropertyFile().getProperty("webRemoteModeType");
	public static final String mobileRemoteMode = ConfigReader.getMainPropertyFile().getProperty("mobileRemoteMode");
	public static final String seleniumGridURL = ConfigReader.getMainPropertyFile().getProperty("seleniumGridURL");
	public static final String selenoidURL = ConfigReader.getMainPropertyFile().getProperty("selenoidURL");
	public static final String appiumServerURL = ConfigReader.getMainPropertyFile().getProperty("appiumServerURL");
	public static final String webURL = ConfigReader.getMainPropertyFile().getProperty("webURL");
	public static final String apiBaseURL = ConfigReader.getMainPropertyFile().getProperty("apiURL");
	public static final String emulatorID = ConfigReader.getMainPropertyFile().getProperty("emulatorID");
	public static final String videoRecordingFlag = ConfigReader.getMainPropertyFile().getProperty("videoRecording");
	public static final String testinyProjectName = ConfigReader.getMainPropertyFile().getProperty("testinyProjectName");
	public static final String testinyAPIKeyValue = ConfigReader.getMainPropertyFile().getProperty("testinyAPIKeyValue");
	



}
