package com.speckyfox.dataprovider;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.DataProvider;

import com.speckyfox.constants.PathConstants;
import com.speckyfox.enums.EnumConstants.excelLabel;
import com.speckyfox.utilities.excelUtilityFunctions.ExcelUtils;

import net.bytebuddy.asm.Advice.Exit;

public class ExcelDataProvider {

	private static final ExcelDataProvider lock = new ExcelDataProvider();

	private Map<Integer, List<String>> dataProviderFileAndSheetMap = new HashMap<Integer, List<String>>();

	@DataProvider(name = "WebTableData")
	public Object[][] readDataFromExcelForWebTable() throws IOException {

		String filePath = PathConstants.testDataExcelPath;
		String sheetName = PathConstants.testDataSheetWebTable;

		synchronized (lock) {

			mapper(filePath, sheetName);
			return ExcelUtils.getAllDataOfSheet(filePath, sheetName, 1);

		}

	}

	@DataProvider(name = "RegistartionForm")
	public Object[][] readDataFromExcelForRegistartionForm() throws IOException {

		String filePath = PathConstants.testDataExcelPath;
		String sheetName = PathConstants.testDataSheetRegistration;

		synchronized (lock) {

			mapper(filePath, sheetName);
			return ExcelUtils.getAllDataOfSheet(filePath, sheetName, 1);

		}

	}
	
	
	@DataProvider(name = "dataProviderData")
	public Object[][] readDataFromExcelForDataProvider(Method method) throws IOException {

		String filePath = PathConstants.testCasesExcelPath;
		String sheetName = PathConstants.testCasesExcelSheetName;
		String fileDataSheetPath= PathConstants.testDataExcelPath;

		
		synchronized (lock) {
			mapper(filePath, sheetName);
			int testsciptColumn=ExcelUtils.getColumnNumberBasedOnLabel(filePath, sheetName,excelLabel.Test_Cases);
			int dataDrivenSheetName=ExcelUtils.getColumnNumberBasedOnLabel(filePath, sheetName,excelLabel.DataDrivenTestingSheetName);
			
			Object[][] data=ExcelUtils.getAllRowDataOfSheetColumnwise(filePath, sheetName, testsciptColumn,dataDrivenSheetName);			
		int k=0;
		String finSheetName=null;
			for(int i=0; i<data.length; i++)
			{
				if(data[i][0].toString().equals(method.getName()))
						{
					k++;
					finSheetName=data[i][1].toString();
					break;
						}
			}
			
			Object[][] val=ExcelUtils.getAllDataOfSheet(fileDataSheetPath, finSheetName, 1);
			
			return ExcelUtils.getAllDataOfSheet(fileDataSheetPath, finSheetName, 1);

		}

	}


	private void mapper(String filePath, String sheetName) {

		synchronized (lock) {

			ArrayList<String> fileAndSheet = new ArrayList<String>();
			fileAndSheet.add(filePath);
			fileAndSheet.add(sheetName);
			dataProviderFileAndSheetMap.put((int) Thread.currentThread().getId(), fileAndSheet);

		}
	}

	public Map<Integer, List<String>> getDataProviderFileAndSheetMap() {
		return dataProviderFileAndSheetMap;

	}
}
