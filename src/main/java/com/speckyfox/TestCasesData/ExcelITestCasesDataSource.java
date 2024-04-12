package com.speckyfox.TestCasesData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.speckyfox.enums.EnumConstants.excelLabel;
import com.speckyfox.utilities.commonUtilityFunctions.CommonUtils;
import com.speckyfox.utilities.excelUtilityFunctions.ExcelUtils;

public class ExcelITestCasesDataSource implements ITestCasesDataSource {

	private static ITestCasesDataSource instance;
	private static final Object lock = new Object();
	private List<String> exludeMethodsList = new ArrayList<String>();
	private List<String> includeMethodsList = new ArrayList<String>();
	private HashMap<String, String> includeOrExludeClassesList = new HashMap<String, String>();

	private ExcelITestCasesDataSource() {
		CommonUtils.execution();
		exludeMethodsList = CommonUtils.excludedMethods;
		includeMethodsList = CommonUtils.includedMethods;

		HashMap<String, String> classesToIncludeOrNot = new HashMap<String, String>();

		Class[] classesInScriptPackage = CommonUtils.getClasses("com.speckyfox.webtest");
		for (int i = 0; i < classesInScriptPackage.length; i++) {

			String className = classesInScriptPackage[i].toString();
			System.out.println(className.split("class ")[1]);

			String requiredClassName = className.substring(className.lastIndexOf(".") + 1);

			String moduleFlag = ExcelUtils.readData("Modules", excelLabel.Module_Name, requiredClassName,
					excelLabel.Execute);

			classesToIncludeOrNot.put(requiredClassName, moduleFlag);
			classesToIncludeOrNot.get(moduleFlag);

		}

		includeOrExludeClassesList = classesToIncludeOrNot;

	}

	@Override
	public List<String> excludeMethods() {
		return exludeMethodsList;
	}

	@Override
	public List<String> includeMethods() {
		return includeMethodsList;
	}

	@Override
	public HashMap<String, String> classesIncludeOrNot() {
		return includeOrExludeClassesList;
	}

	public static ITestCasesDataSource getInstance() {

		synchronized (lock) {

			if (instance == null) {
				instance = new ExcelITestCasesDataSource();
			}
			return instance;

		}

	}
	
	

	public HashMap<String, String> classesIncludeOrNotDELETE() {
		return includeOrExludeClassesList;
	}

}
