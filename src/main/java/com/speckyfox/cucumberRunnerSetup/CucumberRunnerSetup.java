package com.speckyfox.cucumberRunnerSetup;
//

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.testng.annotations.*;

import com.speckyfox.constants.PathConstants;
import com.speckyfox.enums.EnumConstants.cucumberExcelReference;
import com.speckyfox.enums.EnumConstants.cucumberOptions;
import com.speckyfox.listners.AppListeners;
import com.speckyfox.utilities.excelUtilityFunctions.ExcelUtils;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(plugin = { "pretty", "html:target/cucumber-reports" })
@Listeners({ AppListeners.class })
public class CucumberRunnerSetup extends AbstractTestNGCucumberTests {

	static {
		String pathOfExcel = PathConstants.testCasesExcelPath;
		String sheetName = "CucumberScenarios";
		CucumberRunnerSetup.cucumberOptionsReadFromExcel(pathOfExcel, sheetName);
	}

	private static void cucumberOptionsReadFromExcel(String pathOfExcel, String sheetName) {

		try (FileInputStream fis = new FileInputStream(new File(pathOfExcel));
				XSSFWorkbook workbook = new XSSFWorkbook(fis)) {
			Sheet sheet = workbook.getSheet(sheetName);

			// Get the header row
			Row headerRow = sheet.getRow(0);

			for (int columnIndex = 1; columnIndex < headerRow.getLastCellNum(); columnIndex++) {
				Cell headerCell = headerRow.getCell(columnIndex);

				String cellValue = ExcelUtils.readData(workbook, pathOfExcel, sheetName,
						cucumberExcelReference.Reference.toString(), "CucumberReference",
						headerCell.getStringCellValue());

				// Check if the header cell contains "Ignore"
				if (cellValue != null && ("Ignore".equalsIgnoreCase(cellValue) || "".equalsIgnoreCase(cellValue))) {
					// If "Ignore" or "" is found, skip setting the system property for this option
					continue;
				}

				// Set system properties for the corresponding options
				String optionValue = cellValue;
				if (!optionName(cucumberOptions.valueOf(headerCell.getStringCellValue())).isEmpty()) {
					System.setProperty(
							"cucumber." + optionName(cucumberOptions.valueOf(headerCell.getStringCellValue())),
							optionValue);

//					System.out.println(System.getProperty(
//							"cucumber." + optionName(cucumberOptions.valueOf(headerCell.getStringCellValue()))));

					System.out.println("Property: " + "cucumber."
							+ optionName(cucumberOptions.valueOf(headerCell.getStringCellValue())) + " and value: "
							+ optionValue);

				}

				// Now, you can run your Cucumber tests
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String optionName(cucumberOptions cucumberOption) {
		switch (cucumberOption) {
		case FeaturePath:
			return "features";
		case Tags:
			return "filter.tags";
		case StepDefinitionPath:
			return "glue";
		case PluginOptions:
			return "plugin";
		case DryRun:
			return "dryRun";
		case StrictMode:
			return "strict";
		case MonoChrome:
			return "monochrome";
		case OutputFormat:
			return "format";
		default:
			return "";
		}
	}

}