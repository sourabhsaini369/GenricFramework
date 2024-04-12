package com.speckyfox.utilities.excelUtilityFunctions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import com.speckyfox.constants.PathConstants;
import com.speckyfox.enums.EnumConstants.excelLabel;
import com.speckyfox.listners.AppListeners;
import com.speckyfox.logmanager.LogsManager;

public class ExcelUtils {
	static String path = PathConstants.testCasesExcelPath;
	static FileInputStream fis;
	static FileOutputStream fos;
	public static Workbook wb;
	static int lastRow = 0;

	public static List<String> getTcsToRun(String sheetName, String yesOrNo) {

		List<String> tcs = new ArrayList<String>();
		try {
			fis = new FileInputStream(new File(path));
			wb = WorkbookFactory.create(fis);
			lastRow = wb.getSheet(sheetName).getLastRowNum();
			for (int i = 1; i <= lastRow; i++) {
				if (wb.getSheet(sheetName).getRow(i).getCell(2).getStringCellValue().equalsIgnoreCase(yesOrNo)) {
					tcs.add(wb.getSheet(sheetName).getRow(i).getCell(0).getStringCellValue());
				}
			}
			fis.close();
		} catch (IOException | EncryptedDocumentException | InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tcs;

	}



	public static Object[][] getAllDataOfSheet(String filePath, String sheetName, int fromWhichRow) {

		Object[][] data = null;

		try {

			FileInputStream fis = new FileInputStream(new File(filePath));
			Workbook workbook = WorkbookFactory.create(fis);
			Sheet sheet = workbook.getSheet(sheetName); // Assuming data is in the first sheet

			int rowCount = sheet.getLastRowNum();
			int colCount = sheet.getRow(0).getLastCellNum();
			data = new Object[rowCount][colCount];

			for (int i = 0; i < rowCount; i++) {
				Row row = sheet.getRow(i + fromWhichRow); // Start from the second row (0-based index)
				for (int j = 0; j < colCount; j++) {
					Cell cell = row.getCell(j);
					if(cell!=null)
					{
						data[i][j] = cell.getStringCellValue(); // Assuming all data is string type
					}
				}
			}
			workbook.close();
		} catch (Exception e) {
			e.printStackTrace();
			return data;
		}
		return data;

	}


	public static Object[][] getAllRowDataOfSheetColumnwise(String filePath, String sheetName, int testScriptCol, int dataProviderCol) {

		Object[][] data = null;

		try {

			FileInputStream fis = new FileInputStream(new File(filePath));
			Workbook workbook = WorkbookFactory.create(fis);
			Sheet sheet = workbook.getSheet(sheetName); // Assuming data is in the first sheet

			int rowCount = sheet.getLastRowNum();
			data = new Object[rowCount][2];

			int updatedRow=0;
			for (int i = 1; i <= rowCount; i++) {
				Row row = sheet.getRow(i); // Start from the second row (0-based index)

				Cell cell = row.getCell(dataProviderCol);
				if(cell!=null)
				{
					String dataProviderSheetName=cell.getStringCellValue();
					if(!dataProviderSheetName.isEmpty())
					{
						data[updatedRow][1]=dataProviderSheetName;
						cell= row.getCell(testScriptCol);
						data[updatedRow][0]=cell.getStringCellValue();
						updatedRow++;
					}

					/*
				for (int j = 0; j < colCount; j++) {
					Cell cell = row.getCell(j);
					data[i][j] = cell.getStringCellValue(); // Assuming all data is string type

				}*/
				}
			}
			workbook.close();
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
			return data;
		}
		return data;

	}



	public static void writeStatusInExcel(String sheetName) {
		Set<String> keys = AppListeners.status.keySet();
		Iterator<String> itr = keys.iterator();
		try {
			fis = new FileInputStream(new File(path));
			wb = WorkbookFactory.create(fis);
			fis.close();
			fos = new FileOutputStream(new File(path));
			Sheet sheet = wb.getSheet(sheetName);
			List<String> key = new ArrayList<String>();
			while (itr.hasNext()) {
				key.add(itr.next());
			}
			for (int i = 0; i < key.size(); i++) {
				for (int z = 1; z <= lastRow; z++) {
					if (key.get(i).equalsIgnoreCase(sheet.getRow(z).getCell(0).getStringCellValue())) {
						Row row = sheet.getRow(z);
						row.createCell(3).setCellValue(AppListeners.status.get(key.get(i)));
					}
				}
			}
			wb.write(fos);
			wb.close();
			fos.close();
		} catch (IOException | EncryptedDocumentException | InvalidFormatException e) {
			e.printStackTrace();
		}

	}

	public static void writeDataInExcel(Object msg, String sheetName, int rowNum, int cellNum) {

		try {
			fis = new FileInputStream(new File(path));
			wb = WorkbookFactory.create(fis);
			fis.close();
			fos = new FileOutputStream(new File(path));
			wb.getSheet(sheetName).getRow(rowNum).createCell(cellNum).setCellValue(msg.toString());
			wb.write(fos);
			fos.close();
		} catch (IOException | EncryptedDocumentException | InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String readData(String sheetName, int rowNum, int cellNum) {
		String value = null;
		try {
			fis = new FileInputStream(new File(path));
			wb = WorkbookFactory.create(fis);
			//			wb.getSheet(sheetName).getRow(rowNum).getLastCellNum();
			Cell cell = wb.getSheet(sheetName).getRow(rowNum).getCell(cellNum);
			value = getValueBasedOnCellType(cell);
			fis.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block

			return null;
		}
		return value;

	}

	//	public static String readDataFromPropertyFile(String filePath, String key) {
	//
	//		String value = "";
	//		Properties prop = new Properties();
	//
	//		try {
	//			prop.load(new FileInputStream(new File(filePath)));
	//			value = prop.getProperty(key);
	//		} catch (Exception e) {
	//			LogsManager.getLogger().error("property file not found");
	//			e.printStackTrace();
	//		}
	//		return value;
	//	}

	public static int getLastColumn(String sheetName, int rowNum) {

		return wb.getSheet(sheetName).getRow(rowNum).getLastCellNum();
	}

	public static String readData(String sheetName, String tcName, int cellNum) {
		String value = "";
		String testCaseName;
		try {
			fis = new FileInputStream(new File(path));
			wb = WorkbookFactory.create(fis);
			DataFormatter df = new DataFormatter();
			int lastRow = wb.getSheet(sheetName).getLastRowNum();
			LogsManager.getLogger().info(lastRow);

			int j = 0;
			for (int i = 0; i <= lastRow; i++) {
				try {
					Cell cell = wb.getSheet(sheetName).getRow(i).getCell(0);
					testCaseName = getValueBasedOnCellType(cell);
					if (!testCaseName.isEmpty() && tcName.contains(testCaseName)) {
						cell = wb.getSheet(sheetName).getRow(i).getCell(cellNum);
						value = getValueBasedOnCellType(cell);
						break;
					} else {
						continue;
					}
				} catch (Exception e) {

					LogsManager.getLogger()
					.error("Row number '" + i + "' is blank. So will not be able to read data from row number '"
							+ i + "'. Kindly Delete the row or enter some value.");

				}
			}
			fis.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LogsManager.getLogger()
			.error("File Path '" + path + "' or Sheet Name '" + sheetName + "' is wrong kindly re-check.");

			return null;
		}
		return value;

	}

	public static String readData(String sheetName, String columnName) {
		String value = "";
		String testCaseName;
		String tcName = AppListeners.currentlyExecutingTC;
		String colName;
		int cellNum = 0;
		try {
			fis = new FileInputStream(new File(path));
			wb = WorkbookFactory.create(fis);
			DataFormatter df = new DataFormatter();
			int lastRow = wb.getSheet(sheetName).getLastRowNum();
			LogsManager.getLogger().info(lastRow);

			for (int i = 0; i <= lastRow; i++) {
				try {
					Cell cell = wb.getSheet(sheetName).getRow(i).getCell(0);
					testCaseName = getValueBasedOnCellType(cell);
					if (!testCaseName.isEmpty() && tcName.contains(testCaseName)) {
						int lastColumnNum = wb.getSheet(sheetName).getRow(0).getLastCellNum();
						LogsManager.getLogger().info(lastColumnNum);

						for (int j = 0; j <= lastColumnNum; j++) {
							try {
								cell = wb.getSheet(sheetName).getRow(0).getCell(j);
								colName = getValueBasedOnCellType(cell);
								if (!colName.isEmpty() && colName.equalsIgnoreCase(columnName)) {
									cellNum = j;
									break;
								} else {
									if (j == lastColumnNum) {
										LogsManager.getLogger().error(columnName + " is not found.");

										return "";
									}
									continue;
								}
							} catch (Exception e) {
								LogsManager.getLogger()
								.error("Column number '" + j
										+ "' is blank. So will not be able to read data from column number '"
										+ j + "'. Kindly Delete the column or enter some value.");
							}
						}
						cell = wb.getSheet(sheetName).getRow(i).getCell(cellNum);
						value = getValueBasedOnCellType(cell);
						break;
					} else {
						if (i == lastRow) {
							LogsManager.getLogger().error(tcName + " is not found.");
							break;
						}
						continue;
					}
				} catch (Exception e) {
					LogsManager.getLogger()
					.error("Row number '" + i + "' is blank. So will not be able to read data from row number '"
							+ i + "'. Kindly Delete the row or enter some value.");
				}
			}
			fis.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LogsManager.getLogger()
			.error("File Path '" + path + "' or Sheet Name '" + sheetName + "' is wrong kindly re-check.");
			return null;
		}
		return value;

	}

	// start from here replacing the df.format with getValueBasedOnCellType
	public static String readData(String path, String sheetName, int rowNum, int cellNum) {
		String value = "";
		try {
			fis = new FileInputStream(new File(path));
			wb = WorkbookFactory.create(fis);
			//			wb.getSheet(sheetName).getRow(rowNum).getLastCellNum();
			Cell cell = wb.getSheet(sheetName).getRow(rowNum).getCell(cellNum);
			value = getValueBasedOnCellType(cell);
			fis.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LogsManager.getLogger().error("There is no value at the location you passed.");
			return null;
		}
		return value;

	}

	public static String readData(String sheetName, int columnNum, int cellNum, String value) {
		String excelValue = null;
		String testCaseName;
		try {
			fis = new FileInputStream(new File(path));
			wb = WorkbookFactory.create(fis);
			DataFormatter df = new DataFormatter();
			int lastRow = wb.getSheet(sheetName).getLastRowNum();
			//			LogsManager.getLogger().info(lastRow);
			for (int i = 0; i <= lastRow; i++) {
				try {
					Cell cell = wb.getSheet(sheetName).getRow(i).getCell(columnNum);
					testCaseName = getValueBasedOnCellType(cell);
					if (!testCaseName.isEmpty() && testCaseName.equalsIgnoreCase(value)) {
						cell = wb.getSheet(sheetName).getRow(i).getCell(cellNum);
						excelValue = getValueBasedOnCellType(cell);
						break;
					} else {
						continue;
					}
				} catch (Exception e) {
					LogsManager.getLogger()
					.error("Row number '" + i + "' is blank. So will not be able to read data from row number '"
							+ i + "'. Kindly Delete the row or enter some value.");
				}
			}
			fis.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LogsManager.getLogger()
			.error("File Path '" + path + "' or Sheet Name '" + sheetName + "' is wrong kindly re-check.");
			return null;
		}
		return excelValue;

	}

	public static boolean WriteDataBasedOnColumnName(String sheetName, int columnNum, int cellNum, String value,
			String writeValue) {
		String excelValue = null;
		boolean flag = false;
		try {
			fis = new FileInputStream(new File(path));
			wb = WorkbookFactory.create(fis);
			fis.close();
			int lastRow = wb.getSheet(sheetName).getLastRowNum();
			for (int i = 0; i <= lastRow; i++) {
				try {
					Cell cell = wb.getSheet(sheetName).getRow(i).getCell(columnNum);
					excelValue = getValueBasedOnCellType(cell).trim();
					if (!excelValue.isEmpty() && excelValue.equalsIgnoreCase(value)) {
						fos = new FileOutputStream(new File(path));
						wb.getSheet(sheetName).getRow(i).createCell(cellNum).setCellValue(writeValue);
						wb.write(fos);
						flag = true;
						fos.close();
						break;
					} else {
						continue;
					}
				} catch (Exception e) {
					LogsManager.getLogger()
					.error("Row number '" + i + "' is blank. So will not be able to read data from row number '"
							+ i + "'. Kindly Delete the row or enter some value.");
				}
			}
			//			fis.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LogsManager.getLogger()
			.error("File Path '" + path + "' or Sheet Name '" + sheetName + "' is wrong kindly re-check.");
			return false;
		}
		if (!flag) {
			LogsManager.getLogger().error(
					value + " :Value is not present in excel sheet " + sheetName + " in column number " + columnNum);
		}
		return flag;
	}

	public static int getLastRow(String sheetName) {
		//		return wb.getSheet(sheetName).getPhysicalNumberOfRows();
		try {
			fis = new FileInputStream(new File(path));
			wb = WorkbookFactory.create(fis);
			return wb.getSheet(sheetName).getLastRowNum();
		} catch (EncryptedDocumentException | InvalidFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;

	}

	public static int getColumnNumberBasedOnLabel(String sheetName, excelLabel label) {

		try {
			fis = new FileInputStream(new File(path));
			wb = WorkbookFactory.create(fis);
			Sheet sheet = wb.getSheet(sheetName);
			int lastColumnNumber = sheet.getRow(0).getLastCellNum();
			for (int i = 0; i < lastColumnNumber; i++) {
				if (getValueBasedOnCellType(sheet.getRow(0).getCell(i)).equalsIgnoreCase(label.toString())) {
					return i;
				} else {
					if (i == lastColumnNumber - 1) {
						LogsManager.getLogger().error(label.toString() + " is not present in the excel.");
						LogsManager.getLogger().error(label.toString() + " is not present in the excel.");
					}
				}
			}
		} catch (EncryptedDocumentException | InvalidFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@SuppressWarnings("deprecation")
	public static List<String> readAllDataFromCSVFileIntoList(String filePath, boolean isColumnIncluded) {
		String line = "";
		List<String> csvRecords = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			int i = 0;
			int k = 0;
			while ((line = br.readLine()) != null) {
				// System.err.println("Line: "+line);
				if (isColumnIncluded && k == 0) {
					csvRecords.add(line);
				} else {
					if (k == 0) {
						k++;
					} else {
						csvRecords.add(line.replace("\"", ""));
					}
				}

			}

		} catch (Exception e) {
			// TODO: handle exception
			LogsManager.getLogger()
			.error(filePath + "<<<<<<<<<<<<<Exception Error :while reading/parsing csv file >>>>>>>>>> :" + e);

		}
		return csvRecords;

	}

	public static boolean writeDataOnCSVFile(String path, Object dataToWrite, String basedOnLabel, String basedOnValue,
			String writeUnderLabel) {
		try {
			writeDataInCSVFile(path, dataToWrite,
					getRowNumberFromCSVFileBasedOnLabelAndValue(path, basedOnLabel, basedOnValue),
					getColumnNumberFromCSVFileBasedOnLabel(path, writeUnderLabel));
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//			e.printStackTrace();
		}

		return false;
	}

	public static int getRowNumberBasedOnLabelAndValue(String sheetName, excelLabel basedOnLabel, String basedOnValue) {

		try {
			fis = new FileInputStream(new File(path));
			wb = WorkbookFactory.create(fis);
			String currentlyItreatingValue = "";
			Sheet sheet = wb.getSheet(sheetName);
			int j = 0;
			int searchInColumnNumber = getColumnNumberBasedOnLabel(sheetName, basedOnLabel);
			for (int i = 0; i >= 0; i++) {
				try {
					currentlyItreatingValue = getValueBasedOnCellType(sheet.getRow(i).getCell(searchInColumnNumber));
				} catch (NullPointerException e) {
					j++;
					if (j == 100) {
						break;
					}
					continue;
				}
				if (currentlyItreatingValue.equalsIgnoreCase(basedOnValue.toString())) {
					return i;
				}
			}
		} catch (EncryptedDocumentException | InvalidFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LogsManager.getLogger().error(basedOnValue + " value is not found under label " + basedOnLabel.toString());
		return 0;

	}

	public static String readData(String sheetName, excelLabel basedOnLabel, String basedOnValue,
			excelLabel searchUnderLabel) {
		try {
			fis = new FileInputStream(new File(path));
			wb = WorkbookFactory.create(fis);
			Sheet sheet = wb.getSheet(sheetName);
			Cell cell = sheet.getRow(getRowNumberBasedOnLabelAndValue(sheetName, basedOnLabel, basedOnValue))
					.getCell(getColumnNumberBasedOnLabel(sheetName, searchUnderLabel));
			return getValueBasedOnCellType(cell);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} finally {
			try {
				fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return null;
	}

	public static boolean writeData(Object dataToWrite, String sheetName, excelLabel basedOnLabel, String basedOnValue,
			excelLabel writeUnderLabel) {
		try {
			if (dataToWrite != null) {
				//			System.err.println("Row: "+getRowNumberBasedOnLabelAndValue(sheetName, basedOnLabel, basedOnValue)+" Cell: "+getColumnNumberBasedOnLabel(sheetName, writeUnderLabel));
				writeDataInExcel(dataToWrite, sheetName,
						getRowNumberBasedOnLabelAndValue(sheetName, basedOnLabel, basedOnValue),
						getColumnNumberBasedOnLabel(sheetName, writeUnderLabel));
				return true;
			} else {

				LogsManager.getLogger()
				.error("Not Able to Write Data: " + dataToWrite + " in Sheet: " + sheetName + " of Column: "
						+ writeUnderLabel + " correspond to Column: " + basedOnLabel + " of Workbook: ");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	public static void writeDataInExcel(String path, Object msg, String sheetName, int rowNum, int cellNum) {

		try {
			fis = new FileInputStream(new File(path));
			wb = WorkbookFactory.create(fis);
			fis.close();
			fos = new FileOutputStream(new File(path));
			wb.getSheet(sheetName).getRow(rowNum).createCell(cellNum).setCellValue(msg.toString());
			wb.write(fos);
			wb.close();
			fos.close();
		} catch (IOException | EncryptedDocumentException | InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static boolean writeData(String path, Object dataToWrite, String sheetName, excelLabel basedOnLabel,
			String basedOnValue, excelLabel writeUnderLabel) {
		try {

			if (dataToWrite != null) {
				writeDataInExcel(path, dataToWrite, sheetName,
						getRowNumberBasedOnLabelAndValue(path, sheetName, basedOnLabel, basedOnValue),
						getColumnNumberBasedOnLabel(path, sheetName, writeUnderLabel));
				return true;
			} else {

				LogsManager.getLogger()
				.error("Not Able to Write Data: " + dataToWrite + " in Sheet: " + sheetName + " of Column: "
						+ writeUnderLabel + " correspond to Column: " + basedOnLabel + " of Workbook: " + path);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//			e.printStackTrace();
		}

		return false;
	}

	public static int getRowNumberBasedOnLabelAndValue(String path, String sheetName, excelLabel basedOnLabel,
			String basedOnValue) {
		int k = 0;
		try {
			fis = new FileInputStream(new File(path));
			wb = WorkbookFactory.create(fis);
			String currentlyItreatingValue = "";
			Sheet sheet = wb.getSheet(sheetName);
			int j = 0;
			int searchInColumnNumber = getColumnNumberBasedOnLabel(path, sheetName, basedOnLabel);
			for (int i = 0; i >= 0; i++) {
				try {
					currentlyItreatingValue = getValueBasedOnCellType(sheet.getRow(i).getCell(searchInColumnNumber));
				} catch (NullPointerException e) {
					j++;
					if (j == 100) {
						break;
					}
					continue;
				}
				if (currentlyItreatingValue.equalsIgnoreCase(basedOnValue.toString())) {
					k = i;
					break;
				} else {
					//					LogsManager.getLogger().error(basedOnValue + " value is not found under label " + basedOnLabel.toString());
					//					LogsManager.getLogger().error().error(basedOnValue + " value is not found under label " + basedOnLabel.toString());
				}
			}
		} catch (EncryptedDocumentException | InvalidFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				wb.close();
				fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//				e.printStackTrace();
			}
		}

		return k;

	}

	public static int getColumnNumberBasedOnLabel(String path, String sheetName, excelLabel label) {
		int k = 0;
		try {
			fis = new FileInputStream(new File(path));
			wb = WorkbookFactory.create(fis);
			Sheet sheet = wb.getSheet(sheetName);
			int lastColumnNumber = sheet.getRow(0).getLastCellNum();
			for (int i = 0; i < lastColumnNumber; i++) {
				if (getValueBasedOnCellType(sheet.getRow(0).getCell(i)).equalsIgnoreCase(label.toString())) {
					k = i;
					break;
				} else {
					if (i == lastColumnNumber - 1) {
						LogsManager.getLogger().error(label.toString() + " is not present in the excel.");
						LogsManager.getLogger().error(label.toString() + " is not present in the excel.");
					}
				}
			}
		} catch (EncryptedDocumentException | InvalidFormatException | IOException e) {
			// TODO Auto-generated catch block
			//			e.printStackTrace();
		} finally {
			try {
				wb.close();
				fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//				e.printStackTrace();
			}
		}
		return k;
	}

	public static int getColumnNumberBasedOnLabel(String path, String sheetName, String label) {
		int k = 0;
		try {
			fis = new FileInputStream(new File(path));
			wb = WorkbookFactory.create(fis);
			Sheet sheet = wb.getSheet(sheetName);
			int lastColumnNumber = sheet.getRow(0).getLastCellNum();
			for (int i = 0; i < lastColumnNumber; i++) {
				if (getValueBasedOnCellType(sheet.getRow(0).getCell(i)).equalsIgnoreCase(label.toString())) {
					k = i;
					break;
				} else {
					if (i == lastColumnNumber - 1) {
						LogsManager.getLogger().error(label.toString() + " is not present in the excel.");
						LogsManager.getLogger().error(label.toString() + " is not present in the excel.");
					}
				}
			}
		} catch (EncryptedDocumentException | InvalidFormatException | IOException e) {
			// TODO Auto-generated catch block
			//			e.printStackTrace();
		} finally {
			try {
				wb.close();
				fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//				e.printStackTrace();
			}
		}
		return k;
	}

	public static int getLastColumn(String filePath, String sheetName, int rowNum) {
		//		return wb.getSheet(sheetName).getPhysicalNumberOfRows();
		int lastColumn = 0;
		try {
			fis = new FileInputStream(new File(filePath));
			wb = WorkbookFactory.create(fis);
			lastColumn = wb.getSheet(sheetName).getRow(rowNum).getLastCellNum();
		} catch (EncryptedDocumentException | InvalidFormatException | IOException e) {
			// TODO Auto-generated catch block
			//			e.printStackTrace();
		} finally {
			try {
				wb.close();
				fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//				e.printStackTrace();
			}
		}
		return lastColumn;
	}

	public static int getRowNumberFromCSVFileBasedOnLabelAndValue(String path, String basedOnLabel,
			String basedOnValue) {
		int k = 0;
		basedOnLabel = basedOnLabel.replace("_", " ");
		CSVReader reader = null;
		try {
			File inputFile = new File(path);
			reader = new CSVReader(new FileReader(inputFile));
			List<String[]> csvBody = reader.readAll();
			// System.err.println("csvBody: "+csvBody.size());
			String currentlyItreatingValue = "";
			int j = 0;
			int searchInColumnNumber = getColumnNumberFromCSVFileBasedOnLabel(path, basedOnLabel);
			for (int i = 1; i < csvBody.size() - 1; i++) {
				try {
					currentlyItreatingValue = csvBody.get(i)[searchInColumnNumber];
				} catch (NullPointerException e) {
					j++;
					if (j == 100) {
						break;
					}
					continue;
				}
				if (currentlyItreatingValue.equalsIgnoreCase(basedOnValue.toString())) {
					k = i;
					break;
				} else {
					//					LogsManager.getLogger().error(basedOnValue + " value is not found under label " + basedOnLabel.toString());
					//					LogsManager.getLogger().error().error(basedOnValue + " value is not found under label " + basedOnLabel.toString());
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//				e.printStackTrace();
			}
		}

		return k;

	}

	public static void writeDataInCSVFile(String path, Object msg, int rowNum, int cellNum) {

		try {
			File inputFile = new File(path);

			// Read existing file
			CSVReader reader = new CSVReader(new FileReader(inputFile));
			List<String[]> csvBody = reader.readAll();
			System.err.println("csvBody: " + csvBody.size());
			csvBody.get(rowNum)[cellNum] = msg.toString();
			reader.close();

			// Write to CSV file which is open
			CSVWriter writer = new CSVWriter(new FileWriter(inputFile));
			writer.writeAll(csvBody);
			writer.flush();
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CsvException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static int getColumnNumberFromCSVFileBasedOnLabel(String path, String label) {
		int k = 0;
		label = label.replace("_", " ");
		CSVReader reader = null;
		try {
			File inputFile = new File(path);
			reader = new CSVReader(new FileReader(inputFile));
			List<String[]> csvBody = reader.readAll();
			// System.err.println("csvBody: "+csvBody.size());
			for (int i = 0; i < 1; i++) {
				String[] strArray = csvBody.get(i);
				// System.err.println("strArray: "+strArray.length);
				for (int j = 0; j < strArray.length; j++) {
					if (strArray[j].equalsIgnoreCase(label)) {
						k = j;
						break;
					} else {
						if (j == strArray.length - 1) {
							LogsManager.getLogger().error(label.toString() + " is not present in the excel.");
							LogsManager.getLogger().error(label.toString() + " is not present in the excel.");
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}
		}
		return k;
	}

	public static String readData(String filePath, String sheetName, excelLabel basedOnLabel, String basedOnValue,
			excelLabel searchUnderLabel) {
		try {
			fis = new FileInputStream(new File(filePath));
			wb = WorkbookFactory.create(fis);
			Sheet sheet = wb.getSheet(sheetName);
			return getValueBasedOnCellType(
					sheet.getRow(getRowNumberBasedOnLabelAndValue(filePath, sheetName, basedOnLabel, basedOnValue))
					.getCell(getColumnNumberBasedOnLabel(filePath, sheetName, searchUnderLabel)));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//			e.printStackTrace();
		} finally {
			try {
				wb.close();
				fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//				e.printStackTrace();
			}
		}
		return null;
	}

	public static String getValueBasedOnCellType(Cell cell) {
		String value = "";
		DataFormatter df = new DataFormatter();
		try {
			switch (cell.getCellTypeEnum()) {
			case FORMULA:
				switch (cell.getCachedFormulaResultTypeEnum()) {
				case NUMERIC:
					value = String.valueOf((int) cell.getNumericCellValue());
					break;
				default:
					value = cell.getStringCellValue();
					break;
				}
				break;
			default:
				value = df.formatCellValue(cell);
				break;
			}

		} catch (Exception e) {
		}
		return value;
	}

	/**
	 * @author Ankur Huria
	 * @param sheetName
	 * @param basedOnLabel:     Label under which primary key is present
	 * @param basedOnValue:     Primary Key based on which value is required.
	 * @param searchUnderLabel: Label under which required value is present
	 * @param Workbook:         Opened Workbook should be there
	 * @return Value based on the parameters.
	 */
	public static String readData(Workbook wb, String filePath, String sheetName, excelLabel basedOnLabel,
			String basedOnValue, excelLabel searchUnderLabel) {
		try {
			Sheet sheet = wb.getSheet(sheetName);
			return getValueBasedOnCellType(
					sheet.getRow(getRowNumberBasedOnLabelAndValue(wb, filePath, sheetName, basedOnLabel, basedOnValue))
					.getCell(getColumnNumberBasedOnLabel(wb, filePath, sheetName, searchUnderLabel)));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String readData(Workbook wb, String filePath, String sheetName, String basedOnLabel,
			String basedOnValue, String searchUnderLabel) {
		try {
			Sheet sheet = wb.getSheet(sheetName);
			return getValueBasedOnCellType(
					sheet.getRow(getRowNumberBasedOnLabelAndValue(wb, filePath, sheetName, basedOnLabel, basedOnValue))
					.getCell(getColumnNumberBasedOnLabel(wb, filePath, sheetName, searchUnderLabel)));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @author Ankur Huria
	 * @param wb
	 * @param sheetName
	 * @param label:
	 * @param Value:
	 * @return Row No. based on Label.
	 */

	public static int getRowNumberBasedOnLabelAndValue(Workbook wb, String path, String sheetName,
			excelLabel basedOnLabel, String basedOnValue) {
		int k = 0;
		try {

			String currentlyItreatingValue = "";
			Sheet sheet = wb.getSheet(sheetName);
			int j = 0;
			int searchInColumnNumber = getColumnNumberBasedOnLabel(wb, path, sheetName, basedOnLabel);
			for (int i = 0; i >= 0; i++) {
				try {
					currentlyItreatingValue = getValueBasedOnCellType(sheet.getRow(i).getCell(searchInColumnNumber));
				} catch (NullPointerException e) {
					j++;
					if (j == 100) {
						break;
					}
					continue;
				}
				if (currentlyItreatingValue.equalsIgnoreCase(basedOnValue.toString())) {
					k = i;
					break;
				} else {
					//				LogsManager.getLogger().error(basedOnValue + " value is not found under label " + basedOnLabel.toString());
					//				LogsManager.getLogger().error().error(basedOnValue + " value is not found under label " + basedOnLabel.toString());
				}
			}
		} catch (EncryptedDocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return k;

	}

	/**
	 * @author Ankur Huria
	 * @param wb
	 * @param sheetName
	 * @param label:
	 * @param Value:
	 * @return Row No. based on Label.
	 */

	public static int getRowNumberBasedOnLabelAndValue(Workbook wb, String path, String sheetName, String basedOnLabel,
			String basedOnValue) {
		int k = 0;
		try {

			String currentlyItreatingValue = "";
			Sheet sheet = wb.getSheet(sheetName);
			int j = 0;
			int searchInColumnNumber = getColumnNumberBasedOnLabel(wb, path, sheetName, basedOnLabel);
			for (int i = 0; i >= 0; i++) {
				try {
					currentlyItreatingValue = getValueBasedOnCellType(sheet.getRow(i).getCell(searchInColumnNumber));
				} catch (NullPointerException e) {
					j++;
					if (j == 100) {
						break;
					}
					continue;
				}
				if (currentlyItreatingValue.equalsIgnoreCase(basedOnValue.toString())) {
					k = i;
					break;
				} else {
					//				LogsManager.getLogger().error(basedOnValue + " value is not found under label " + basedOnLabel.toString());
					//				LogsManager.getLogger().error().error(basedOnValue + " value is not found under label " + basedOnLabel.toString());
				}
			}
		} catch (EncryptedDocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return k;

	}

	/**
	 * @author Ankur Huria
	 * @param wb
	 * @param sheetName
	 * @param label:
	 * @return Column No. based on Label.
	 */
	public static int getColumnNumberBasedOnLabel(Workbook wb, String path, String sheetName, excelLabel label) {
		int k = 0;
		try {

			Sheet sheet = wb.getSheet(sheetName);
			int lastColumnNumber = sheet.getRow(0).getLastCellNum();
			for (int i = 0; i < lastColumnNumber; i++) {
				if (getValueBasedOnCellType(sheet.getRow(0).getCell(i)).equalsIgnoreCase(label.toString())) {
					k = i;
					break;
				} else {
					if (i == lastColumnNumber - 1) {
						LogsManager.getLogger().error(label.toString() + " is not present in the excel.");
						LogsManager.getLogger().error(label.toString() + " is not present in the excel.");
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//		e.printStackTrace();
		}
		return k;
	}

	/**
	 * @author Ankur Huria
	 * @param wb
	 * @param sheetName
	 * @param label:
	 * @return Column No. based on Label.
	 */
	public static int getColumnNumberBasedOnLabel(Workbook wb, String path, String sheetName, String label) {
		int k = 0;
		try {

			Sheet sheet = wb.getSheet(sheetName);
			int lastColumnNumber = sheet.getRow(0).getLastCellNum();
			for (int i = 0; i < lastColumnNumber; i++) {
				if (getValueBasedOnCellType(sheet.getRow(0).getCell(i)).equalsIgnoreCase(label.toString())) {
					k = i;
					break;
				} else {
					if (i == lastColumnNumber - 1) {
						LogsManager.getLogger().error(label.toString() + " is not present in the excel.");
						LogsManager.getLogger().error(label.toString() + " is not present in the excel.");
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//		e.printStackTrace();
		}
		return k;
	}

	public static HashMap<String, ArrayList<String>> dataRead(String filePath, String sheetName,
			excelLabel basedOnLabel, String basedOnValue) {
		HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
		ArrayList<String> headerList = new ArrayList<>();
		ArrayList<String> headervalue = new ArrayList<>();
		try {
			fis = new FileInputStream(new File(filePath));
			wb = WorkbookFactory.create(fis);
			Sheet sheet = wb.getSheet(sheetName);
			int row = ExcelUtils.getRowNumberBasedOnLabelAndValue(filePath, sheetName, basedOnLabel, basedOnValue);
			int lastColumnNumber = sheet.getRow(0).getLastCellNum();
			for (int i = 3; i < lastColumnNumber; i++) {

				headerList.add(ExcelUtils.getValueBasedOnCellType(sheet.getRow(0).getCell(i)).replaceAll("_", " "));
			}

			for (String header : headerList) {
				headervalue.add(ExcelUtils.getValueBasedOnCellType(sheet.getRow(row)
						.getCell(ExcelUtils.getColumnNumberBasedOnLabel(filePath, sheetName, header))));

			}

			LogsManager.getLogger().info("List1:" + String.valueOf(headerList));
			LogsManager.getLogger().info("List2:" + headervalue);
			map.put("headers", headerList);
			map.put("value", headervalue);
			// return getValueBasedOnCellType();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//			e.printStackTrace();
		} finally {
			try {
				wb.close();
				fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//				e.printStackTrace();
			}
		}
		return map;
	}

	/*
	 * @author Ravi kumar
	 * 
	 * @param filepath
	 * 
	 * @param sheetName
	 * 
	 * @param columnName
	 * 
	 * @return String
	 * 
	 * @description This method read the cell data according to column Name passed.
	 */
	public static String readDataForResearch(String filepath, String sheetName, String columnName) {
		String value = "";
		String testCaseName;
		String tcName = AppListeners.currentlyExecutingTC;
		String colName;
		int cellNum = 0;
		try {
			fis = new FileInputStream(new File(filepath));
			wb = WorkbookFactory.create(fis);
			int lastRow = wb.getSheet(sheetName).getLastRowNum();
			LogsManager.getLogger().info(lastRow);
			for (int i = 1; i <= lastRow; i++) {// changed value of i from 1 to 2
				try {
					Cell cell = wb.getSheet(sheetName).getRow(i).getCell(0);
					testCaseName = getValueBasedOnCellType(cell);
					if (!testCaseName.isEmpty() && tcName.contains(testCaseName)) {
						int lastColumnNum = wb.getSheet(sheetName).getRow(0).getLastCellNum();
						LogsManager.getLogger().info(lastColumnNum);
						for (int j = 0; j <= lastColumnNum; j++) {
							try {
								cell = wb.getSheet(sheetName).getRow(0).getCell(j);
								colName = getValueBasedOnCellType(cell);
								if (!colName.isEmpty() && colName.equalsIgnoreCase(columnName)) {
									cellNum = j;
									break;
								} else {
									if (j == lastColumnNum) {
										LogsManager.getLogger().error(columnName + " is not found.");
										return "";
									}
									continue;
								}
							} catch (Exception e) {
								LogsManager.getLogger()
								.error("Column number '" + j
										+ "' is blank. So will not be able to read data from column number '"
										+ j + "'. Kindly Delete the column or enter some value.");
							}
						}
						cell = wb.getSheet(sheetName).getRow(i).getCell(cellNum);
						value = getValueBasedOnCellType(cell);
						break;
					} else {
						if (i == lastRow) {
							LogsManager.getLogger().error(tcName + " is not found.");
							break;
						}
						continue;
					}
				} catch (Exception e) {
					LogsManager.getLogger()
					.error("Row number '" + i + "' is blank. So will not be able to read data from row number '"
							+ i + "'. Kindly Delete the row or enter some value.");
				}
			}
			fis.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LogsManager.getLogger()
			.error("File Path '" + path + "' or Sheet Name '" + sheetName + "' is wrong kindly re-check.");
			return null;
		}
		return value;

	}

	@SuppressWarnings("deprecation")
	public static String readAllDataForAColumn(String filePath, String sheetName, int cellNum,
			Boolean numericTypeValue) {
		String value = "";
		try {
			File src = new File(filePath);
			FileInputStream fis = new FileInputStream(src);
			XSSFWorkbook wb = new XSSFWorkbook(fis);
			XSSFSheet sh1 = wb.getSheet(sheetName);
			int rowCount = sh1.getLastRowNum();
			System.err.println("rowCount : " + rowCount);
			String a = "";
			DataFormatter df = new DataFormatter();
			for (int row = 1; row <= rowCount; row++) {
				if (numericTypeValue) {
					XSSFRow row1 = sh1.getRow(row);
					XSSFCell cell = row1.getCell(cellNum);
					cell.setCellType(CellType.NUMERIC);
					a = String.valueOf(cell.getNumericCellValue());
					//					LogsManager.getLogger().info().info("Id : "+a);
					String[] ss = a.split("E");
					//					LogsManager.getLogger().info().info("ss0 >> "+ss[0]);
					//					LogsManager.getLogger().info().info("ss1 >> "+ss[1]);
					if (ss.length > 1) {
						Double d = Double.valueOf(ss[0]) * Math.pow(10, Integer.parseInt(ss[1]));
						//						System.err.println(d);
						a = String.valueOf(new BigDecimal(Math.round(d)).toBigInteger());
						//						LogsManager.getLogger().info().info(" Final Id : "+a);
						value = a + "<break>" + value;
					} else {
						//						LogsManager.getLogger().info().info(" Final Id : "+a);
						value = a + "<break>" + value;
					}
				} else {
					a = df.formatCellValue((sh1.getRow(row).getCell(cellNum)));
					if (row < rowCount) {
						value = value + a + "<break>";
					} else {
						value = value + a;
					}

				}
			}
			fis.close();
			wb.close();
		} catch (Exception e) {

			value = null;
		}
		return value;

	}

	public static HashMap<String, ArrayList<String>> dataReadForCurrentRecord(String filePath, String sheetName,
			excelLabel basedOnLabel, String basedOnValue) {
		HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
		ArrayList<String> headerList = new ArrayList<>();
		ArrayList<String> headervalue = new ArrayList<>();
		int k = 0;
		try {
			fis = new FileInputStream(new File(filePath));
			wb = WorkbookFactory.create(fis);
			Sheet sheet = wb.getSheet(sheetName);
			int row = ExcelUtils.getRowNumberBasedOnLabelAndValue(filePath, sheetName, basedOnLabel, basedOnValue);
			int lastColumnNumber = sheet.getRow(0).getLastCellNum();
			if (sheetName == "CurrentRecord") {
				k = 4;
			} else if (sheetName == "AdvancedSearch") {
				k = 7;
			} else {
				k = 3;
			}
			for (int i = k; i < lastColumnNumber; i++) {

				headerList.add(ExcelUtils.getValueBasedOnCellType(sheet.getRow(0).getCell(i)).replaceAll("_", " "));
			}

			for (String header : headerList) {
				headervalue.add(ExcelUtils.getValueBasedOnCellType(sheet.getRow(row)
						.getCell(ExcelUtils.getColumnNumberBasedOnLabel(filePath, sheetName, header))));

			}

			headerList.remove("Status");

			LogsManager.getLogger().info("List1:" + String.valueOf(headerList));
			LogsManager.getLogger().info("List2:" + headervalue);
			map.put("headers", headerList);
			map.put("value", headervalue);
			// return getValueBasedOnCellType();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} finally {
			try {
				wb.close();
				fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}
		}
		return map;
	}

	public static boolean writeData(Workbook wb, String path, Object dataToWrite, String sheetName,
			excelLabel basedOnLabel, String basedOnValue, excelLabel writeUnderLabel) {
		try {

			if (dataToWrite != null) {
				writeDataInExcel(wb, path, dataToWrite, sheetName,
						getRowNumberBasedOnLabelAndValue(wb, path, sheetName, basedOnLabel, basedOnValue),
						getColumnNumberBasedOnLabel(wb, path, sheetName, writeUnderLabel));

				//				try {
				//					FileOutputStream fileOut = new FileOutputStream(path);
				//					wb.write(fileOut);
				//					fileOut.close();
				//					LogsManager.getLogger().error("Excel file updated and saved successfully!");
				//				} catch (IOException e) {
				//					e.printStackTrace();
				//				}

				return true;
			} else {

				LogsManager.getLogger()
				.error("Not Able to Write Data: " + dataToWrite + " in Sheet: " + sheetName + " of Column: "
						+ writeUnderLabel + " correspond to Column: " + basedOnLabel + " of Workbook: " + path);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	public static void clearColumn(Workbook workbook, String sheetName, String outputPath, excelLabel columnName) {
		Sheet sheet = workbook.getSheet(sheetName);
		if (sheet == null) {
			LogsManager.getLogger().error("Sheet not found");
			return;
		}

		int columnIndex = -1;
		Row headerRow = sheet.getRow(0);

		if (headerRow != null) {
			for (Cell cell : headerRow) {
				if (cell.getStringCellValue().equalsIgnoreCase(columnName.toString())) {
					columnIndex = cell.getColumnIndex();
					break;
				}
			}
		}

		if (columnIndex != -1) {
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);
				if (row != null) {
					Cell cell = row.getCell(columnIndex);
					if (cell != null) {
						cell.setCellValue("");
					}
				}
			}
		} else {
			LogsManager.getLogger().error("Column not found");
		}

		try {
			FileOutputStream fileOut = new FileOutputStream(outputPath);
			workbook.write(fileOut);
			fileOut.close();
			LogsManager.getLogger().info("Excel file updated and saved successfully!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void writeDataInExcel(Workbook wb, String path, Object msg, String sheetName, int rowNum,
			int cellNum) {

		try {

			fos = new FileOutputStream(new File(path));
			wb.getSheet(sheetName).getRow(rowNum).createCell(cellNum).setCellValue(msg.toString());
			wb.write(fos);

			fos.close();
		} catch (IOException | EncryptedDocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//	public static void addScreenshotToCell(Workbook workbook, String screenshotPath, Sheet sheet, int column, int row) {
	//        try {
	//            // Add the image to the workbook
	//            InputStream inputStream = new FileInputStream(screenshotPath);
	//            byte[] bytes = IOUtils.toByteArray(inputStream);
	//            int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
	//            inputStream.close();
	//
	//            CreationHelper helper = workbook.getCreationHelper();
	//            Drawing drawing = sheet.createDrawingPatriarch();
	//            ClientAnchor anchor = helper.createClientAnchor();
	//
	//            // Set the position of the image in the sheet
	//            anchor.setCol1(column); // Column position
	//            anchor.setRow1(row); // Row position
	//
	//            // Create the drawing object and add the picture to it
	//            Picture pict = drawing.createPicture(anchor, pictureIdx);
	//
	//            // Auto-size picture to fit its size
	//            pict.resize();
	//
	//        } catch (IOException e) {
	//            e.printStackTrace();
	//        }
	//    }

	//	public static void addScreenshotToCell(Workbook workbook, String screenshotPath, String sheetName, int column,
	//			int row, String filePath) {
	//		try {
	//			// Add the image to the workbook
	//			InputStream inputStream = new FileInputStream(screenshotPath);
	//			byte[] bytes = IOUtils.toByteArray(inputStream);
	//			int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
	//			inputStream.close();
	//
	//			Sheet sheet = workbook.getSheet(sheetName);
	//
	//			if (sheet != null) {
	//				CreationHelper helper = workbook.getCreationHelper();
	//				Drawing<?> drawing = sheet.createDrawingPatriarch();
	//				ClientAnchor anchor = helper.createClientAnchor();
	//
	//				// Set the position of the image in the sheet
	//				anchor.setCol1(column); // Column position
	//				anchor.setRow1(row); // Row position
	//
	//				// Create the drawing object and add the picture to it
	//				Picture pict = drawing.createPicture(anchor, pictureIdx);
	//
	//				// Auto-size picture to fit its size
	//				pict.resize();
	//
	//				// Save the workbook to a file
	//				FileOutputStream fileOut = new FileOutputStream(filePath);
	//				workbook.write(fileOut);
	//				fileOut.close();
	//				LogsManager.getLogger().error("Screenshot added to Excel and saved successfully.");
	//			} else {
	//				LogsManager.getLogger().error("Sheet Name: " + sheetName + " is not present there in location: " + filePath);
	//
	//			}
	//
	//		} catch (IOException e) {
	//			e.printStackTrace();
	//		}
	//	}

	public static void addScreenshotToCell(Workbook workbook, String screenshotPath, String sheetName, int column,
			int row, String filePath) {
		try {
			// Add the image to the workbook
			InputStream inputStream = new FileInputStream(screenshotPath);
			byte[] bytes = IOUtils.toByteArray(inputStream);
			int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
			inputStream.close();
			Sheet sheet = workbook.getSheet(sheetName);

			if (sheet != null) {

				CreationHelper helper = workbook.getCreationHelper();
				Drawing drawing = sheet.createDrawingPatriarch();
				ClientAnchor anchor = helper.createClientAnchor();

				// Set the position of the image in the sheet
				anchor.setCol1(column); // Column position
				anchor.setRow1(row); // Row position

				// Create the drawing object and add the picture to it
				Picture pict = drawing.createPicture(anchor, pictureIdx);

				// Auto-size picture to fit its size
				pict.resize();

				// Save the workbook to a file
				FileOutputStream fileOut = new FileOutputStream(filePath);
				workbook.write(fileOut);
				fileOut.close();
				LogsManager.getLogger().info("Screenshot added to Excel and saved successfully.");

			} else {
				LogsManager.getLogger()
				.error("Sheet Name: " + sheetName + " is not present there in location: " + filePath);

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//	public static void addImageReferenceToCell(String sheetName, int column, int row, String imagePath, Workbook workbook, String outputPath) {
	//        try {
	//            Sheet sheet = workbook.getSheet(sheetName);
	//            if (sheet == null) {
	//                sheet = workbook.createSheet(sheetName);
	//            }
	//
	//            Row excelRow = sheet.getRow(row);
	//            if (excelRow == null) {
	//                excelRow = sheet.createRow(row);
	//            }
	//
	//            Cell cell = excelRow.getCell(column);
	//            if (cell == null) {
	//                cell = excelRow.createCell(column);
	//            }
	//
	//            CreationHelper createHelper = workbook.getCreationHelper();
	//            Hyperlink link = createHelper.createHyperlink(HyperlinkType.FILE);
	//
	//            // Set the link address as the image path
	//            link.setAddress(imagePath);
	//            cell.setHyperlink(link);
	//
	//            // Set some display text for the cell (optional)
	//            cell.setCellValue("Click to view image");
	//
	//            // Save the workbook to a new file
	//            FileOutputStream fileOut = new FileOutputStream(outputPath);
	//            workbook.write(fileOut);
	//            fileOut.close();
	//            LogsManager.getLogger().error("Image reference added to Excel and saved successfully.");
	//
	//        } catch (IOException e) {
	//            e.printStackTrace();
	//        }
	//    }

	//	public static void addImageReferenceToCell(String sheetName, int column, int row, String imagePath,
	//			Workbook workbook, String outputPath) {
	//		try {
	//			Sheet sheet = workbook.getSheet(sheetName);
	//			if (sheet == null) {
	//				sheet = workbook.createSheet(sheetName);
	//			}
	//
	//			Row excelRow = sheet.getRow(row);
	//			if (excelRow == null) {
	//				excelRow = sheet.createRow(row);
	//			}
	//
	//			Cell cell = excelRow.getCell(column);
	//			if (cell == null) {
	//				cell = excelRow.createCell(column);
	//			}
	//
	//			Hyperlink existingLink = cell.getHyperlink();
	//			String existingText = cell.getStringCellValue();
	//
	//			CreationHelper createHelper = workbook.getCreationHelper();
	//
	//			Hyperlink newLink = createHelper.createHyperlink(HyperlinkType.FILE);
	//			newLink.setAddress(imagePath);
	//			String newText = " | Click to view image";
	//			String newTextForSingle = "Click to view image";
	//
	//			if (existingLink != null) {
	//				// If there's an existing hyperlink in the cell, append the new hyperlink
	//				String updatedText = existingText + newText;
	//				cell.setCellValue(updatedText);
	//
	//				// Set the updated hyperlink to the cell
	//				cell.setHyperlink(existingLink);
	//
	//				LogsManager.getLogger().error("New link appended or created successfully.");
	//			} else {
	//				// If no existing hyperlink, set the new hyperlink directly
	//				cell.setCellValue(newTextForSingle);
	//				cell.setHyperlink(newLink);
	//			}
	//
	//			// Save the workbook to a new file
	//			FileOutputStream fileOut = new FileOutputStream(outputPath);
	//			workbook.write(fileOut);
	//			fileOut.close();
	//			LogsManager.getLogger().error("Image reference added to Excel and saved successfully.");
	//
	//		} catch (IOException e) {
	//			e.printStackTrace();
	//		}
	//	}

	//	public static void addImageReferenceToCell(String sheetName, int column, int row, String imagePath,
	//			Workbook workbook, String outputPath) {
	//		try {
	//			Sheet sheet = workbook.getSheet(sheetName);
	//			if (sheet == null) {
	//				sheet = workbook.createSheet(sheetName);
	//			}
	//
	//			Row excelRow = sheet.getRow(row);
	//			if (excelRow == null) {
	//				excelRow = sheet.createRow(row);
	//			}
	//
	//			Cell cell = excelRow.getCell(column);
	//			if (cell == null) {
	//				cell = excelRow.createCell(column);
	//			}
	//			
	//			
	//			
	//
	//			CreationHelper createHelper = workbook.getCreationHelper();
	//			Hyperlink link = createHelper.createHyperlink(HyperlinkType.FILE);
	//
	//			// Convert local file path to URI
	//			URI uri = new File(imagePath).toURI();
	//
	//			// Set the URI path as the hyperlink address
	//			link.setAddress(uri.toString());
	//			cell.setHyperlink(link);
	//
	//			// Set some display text for the cell (optional)
	//			cell.setCellValue("Click to view image");
	//
	//			// Save the workbook to a new file
	//			FileOutputStream fileOut = new FileOutputStream(outputPath);
	//			workbook.write(fileOut);
	//			fileOut.close();
	//			LogsManager.getLogger().error("Image reference added to Excel and saved successfully.");
	//			
	//			
	//			
	//			
	//			
	//			
	//			
	//			
	//			
	//			
	//			
	//			
	//			
	//			
	//			
	//			
	//			
	//			
	//			
	//			
	//			
	//	
	//
	//		} catch (IOException e) {
	//			e.printStackTrace();
	//		}
	//	}

	//	public static void addImageReferenceToCell(String sheetName, int column, int row, String imagePath,
	//			Workbook workbook, String outputPath) {
	//		try {
	//			Sheet sheet = workbook.getSheet(sheetName);
	//			if (sheet == null) {
	//				sheet = workbook.createSheet(sheetName);
	//			}
	//
	//			Row excelRow = sheet.getRow(row);
	//			if (excelRow == null) {
	//				excelRow = sheet.createRow(row);
	//			}
	//
	//			Cell cell = excelRow.getCell(column);
	//			if (cell == null) {
	//				cell = excelRow.createCell(column);
	//			}
	//
	//			Hyperlink existingLink = cell.getHyperlink();
	//			String existingText = cell.getStringCellValue();
	//
	//			CreationHelper createHelper = workbook.getCreationHelper();
	//
	//// Create the initial link with the first image path
	//			Hyperlink newLink = createHelper.createHyperlink(HyperlinkType.FILE);
	//			StringBuilder newTextBuilder = new StringBuilder();
	//			newTextBuilder.append(existingText != null ? existingText : "");
	//
	//			imagePath = imagePath.replace("\\", "/"); // Replace backslashes with forward slashes
	//			imagePath = imagePath.replaceAll(" ", "%20"); // Encode spaces as %20
	//
	//			try {
	//				URI uri = new URI("file:///" + imagePath);
	//				newLink = createHelper.createHyperlink(HyperlinkType.FILE);
	//				newLink.setAddress(uri.toString());
	//
	//				newTextBuilder.append(" | Click to view image");
	//				cell.setCellValue(newTextBuilder.toString());
	//				cell.setHyperlink(newLink);
	//
	//				newTextBuilder.delete(0, newTextBuilder.length()); // Clear StringBuilder for next iteration
	//				newTextBuilder.append(existingText != null ? existingText : "");
	//			} catch (URISyntaxException e) {
	//				LogsManager.getLogger().error("Invalid URI: " + e.getMessage());
	//			}
	//
	//// Save the workbook to a new file
	//			FileOutputStream fileOut = new FileOutputStream(outputPath);
	//			workbook.write(fileOut);
	//			fileOut.close();
	//			LogsManager.getLogger().error("Image references added to Excel and saved successfully.");
	//
	//		} catch (IOException e) {
	//			e.printStackTrace();
	//		}
	//	}

	public static void addImageReferenceToCell(String sheetName, int column, int row, String imagePath,
			Workbook workbook, String outputPath) {
		try {
			Sheet sheet = workbook.getSheet(sheetName);
			if (sheet == null) {
				sheet = workbook.createSheet(sheetName);
			}

			Row excelRow = sheet.getRow(row);
			if (excelRow == null) {
				excelRow = sheet.createRow(row);
			}

			Cell cell = excelRow.getCell(column);
			if (cell == null) {
				cell = excelRow.createCell(column);
			}

			String existingText = cell.getStringCellValue();
			Hyperlink existingLink = cell.getHyperlink();

			CreationHelper createHelper = workbook.getCreationHelper();

			StringBuilder newTextBuilder = new StringBuilder(existingText != null ? existingText : "");

			if (existingLink != null) {
				newTextBuilder.append("\n"); // Add a line break if existing link is present
			}

			imagePath = imagePath.replace("\\", "/"); // Replace backslashes with forward slashes
			imagePath = imagePath.replaceAll(" ", "%20"); // Encode spaces as %20

			try {
				URI uri = new URI("file:///" + imagePath);
				Hyperlink newLink = createHelper.createHyperlink(HyperlinkType.FILE);
				newLink.setAddress(uri.toString());

				newTextBuilder.append("Click to view image");
				if (existingLink != null) {
					newTextBuilder.append("\n");
				}

				cell.setCellValue(newTextBuilder.toString());
				cell.setHyperlink(newLink);

				newTextBuilder = new StringBuilder(existingText != null ? existingText : "");
				existingLink = null; // Reset existingLink to avoid duplication
			} catch (URISyntaxException e) {
				LogsManager.getLogger().error("Invalid URI: " + e.getMessage());
			}

			// Save the workbook to a new file
			FileOutputStream fileOut = new FileOutputStream(outputPath);
			workbook.write(fileOut);
			fileOut.close();
			LogsManager.getLogger().info("Image references added to Excel and saved successfully.");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public static String getSheetNameOfDatasheetFromTestcaseData(String testCaseName)
	{
		String filePath = PathConstants.testCasesExcelPath;
		String sheetName = PathConstants.testCasesExcelSheetName;


		int testsciptColumn=ExcelUtils.getColumnNumberBasedOnLabel(filePath, sheetName,excelLabel.Test_Cases);
		int dataDrivenSheetName=ExcelUtils.getColumnNumberBasedOnLabel(filePath, sheetName,excelLabel.DataDrivenTestingSheetName);

		Object[][] data=ExcelUtils.getAllRowDataOfSheetColumnwise(filePath, sheetName, testsciptColumn,dataDrivenSheetName);			
		int k=0;
		String finSheetName=null;
		for(int i=0; i<data.length; i++)
		{
			if(data[i][0].toString().equals(testCaseName))
			{
				k++;
				finSheetName=data[i][1].toString();
				break;
			}
		}
		return finSheetName;
	}

	public static void updateExecutionStatusOnDataSheet(String row,String sheetName, String status)
	{
		String fileDataSheetPath= PathConstants.testDataExcelPath;
		int sheetNumber=ExcelUtils.getColumnNumberBasedOnLabel(fileDataSheetPath, sheetName,excelLabel.Status);
		writeDataInExcel(fileDataSheetPath,status, sheetName, Integer.parseInt(row), sheetNumber);

	}

}
