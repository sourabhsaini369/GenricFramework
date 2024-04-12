package com.speckyfox.enums;

public class EnumConstants {

	public static enum WebBrowser {
		CHROME, FIREFOX, EDGE
	}

	public static enum WebRunMode {
		LOCAL, REMOTE
	}

	public static enum WebRemoteModeType {

		SELENIUM, SELENOID, BROWSER_STACK

	}

	public static enum MobileRunMode {
		LOCAL, REMOTE
	}

	public static enum MobileRemoteModeType {

		BROWSER_STACK, SAUCE_LABS
	}

	public static enum PlatformType {

		WEB, MOBILE
	}

	public static enum MobilePlatformType {

		ANDROID, IOS
	}

	public static enum YesNo {
		YES, NO;
	}

	public static enum excelLabel {
		Module_Name, Execute, Test_Cases {
			@Override
			public String toString() {
				return "Test Cases";
			}
		},
		Logs, Status, Screenshot, BrowserOrAppName,DataDrivenTestingSheetName;
	}

	public static enum cucumberExcelReference {
		Reference
	}

	public static enum cucumberOptions {
		FeaturePath, Tags, StepDefinitionPath, PluginOptions, DryRun, StrictMode, MonoChrome, OutputFormat
	}

	public static enum Framework {
		CUCUMBER, DATADRIVEN;
	}

	public static enum DataSource {
		EXCEL, DATABASE, JSON;
	}

	public static enum ReportType {
		EXCEL, EXTENT;
	}

	public static enum TestResult {
		Pass, Fail, Skip;
	}

	public static enum DTQACardSelect {
		Elements, Forms, Interactions, Widgets;
	}

	public static enum DTQACardSubCategorySelect {
		Text_Box, Check_Box, Web_Tables, Buttons, Links, Practice_Form;
	}

	public static enum DTQAWebTablesFields {
		First_Name, Last_Name, Email, Age, Salary, Department;
	}

	public static enum DTQAWebTablesGender {
		Male, Female, Other;
	}

	public static enum DTQARegisteredFormSubmitFileds {
		Student_Name, Student_Email, Gender, Mobile, Date_of_Birth, Subjects, Hobbies, Picture, Address, State_and_City;
	}

	public static enum TestinyResultStatus {
		NOTRUN, PASSED, FAILED, BLOCKED, SKIPPED;
	}

	public static enum AlertAction {
		ACCEPT, DECLINE;
	}

}