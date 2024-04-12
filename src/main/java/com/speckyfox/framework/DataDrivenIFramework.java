package com.speckyfox.framework;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.testng.*;

import org.testng.xml.*;

import com.speckyfox.TestCasesData.ITestCasesDataSource;
import com.speckyfox.TestCasesData.TestCasesDataSourceManager;
import com.speckyfox.utilities.commonUtilityFunctions.CommonUtils;

public class DataDrivenIFramework implements IFramework {

	@SuppressWarnings({ "rawtypes" })
	@Override
	public void runner() throws Exception {

		List<XmlSuite> suites = new ArrayList<XmlSuite>();
		List<XmlClass> classes = new ArrayList<XmlClass>();
		List<String> listenerClasses = new ArrayList<String>();
		Map<String, String> parameters = new LinkedHashMap<String, String>();

		listenerClasses.add("com.speckyfox.listners.AppListeners");
		XmlSuite suite = new XmlSuite();
		suite.setName("SpeckyfoxSuite");
		suite.setListeners(listenerClasses);
		XmlTest test = new XmlTest(suite);
		test.setName("Test");
		test.setParameters(parameters);

		List<XmlClass> xmlClass = new ArrayList<XmlClass>();

		Class[] classesInWebScriptPackage = CommonUtils.getClasses("com.speckyfox.webtest");
		Class[] classesInMobileScriptPackage = CommonUtils.getClasses("com.speckyfox.mobiletest");
		Class[] classesInAPIScriptPackage = CommonUtils.getClasses("com.speckyfox.apitest");

		Class[] classesInScriptPackage = new Class[classesInWebScriptPackage.length
				+ classesInMobileScriptPackage.length + classesInAPIScriptPackage.length];

		System.arraycopy(classesInWebScriptPackage, 0, classesInScriptPackage, 0, classesInWebScriptPackage.length);
		System.arraycopy(classesInMobileScriptPackage, 0, classesInScriptPackage, classesInWebScriptPackage.length,
				classesInMobileScriptPackage.length);

		System.arraycopy(classesInAPIScriptPackage, 0, classesInScriptPackage,
				classesInWebScriptPackage.length + classesInMobileScriptPackage.length,
				classesInAPIScriptPackage.length);

		ITestCasesDataSource testCasesData = TestCasesDataSourceManager.getTestCasesDataSource();

		if (testCasesData != null) {
			HashMap<String, String> getTestCasesAndTheirFlags = testCasesData.classesIncludeOrNot();
			List<String> getExcludeMethods = testCasesData.excludeMethods();

			for (int i = 0; i < classesInScriptPackage.length; i++) {

				String className = classesInScriptPackage[i].toString();
				System.out.println(className.substring(className.lastIndexOf(".") + 1));

				String moduleFlag = getTestCasesAndTheirFlags.get(className.substring(className.lastIndexOf(".") + 1));
				if (!"No".equalsIgnoreCase(moduleFlag)) {

					XmlClass cl = new XmlClass(className.split("class ")[1]);

					try {
						String priorityNumber = String.valueOf(i) + 1;
						cl.setIndex(Integer.parseInt(priorityNumber));
					} catch (Exception e) {
						cl.setIndex(0);
					}
					xmlClass.add(cl);

				} else {

					Method[] allMethods = classesInScriptPackage[i].getDeclaredMethods();
					for (Method m : allMethods) {

						if (m.toString().contains("lambda$")) {
							continue;
						}

						String requiredMethodName = "";
						int startIndex = 0;
						int endIndex = m.toString().indexOf('(');

						requiredMethodName = m.toString().substring(startIndex, endIndex);
						if (requiredMethodName.contains(".")) {

							requiredMethodName = requiredMethodName.substring(requiredMethodName.lastIndexOf('.') + 1);
						}

						getExcludeMethods.add(requiredMethodName);

					}

					System.err.println(("Module: " + className + " is switched Off"));

				}

			}

			for (int i = 0; i < xmlClass.size(); i++) {
				classes.add(xmlClass.get(i));
				xmlClass.get(i).setExcludedMethods(getExcludeMethods);

			}
			test.setXmlClasses(classes);

			suites.add(suite);
			TestNG tng = new TestNG();
			tng.setXmlSuites(suites);
			tng.run();

		}

	}

}
