package com.speckyfox.TestCasesData;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.speckyfox.constants.PathConstants;

import io.cucumber.core.internal.com.fasterxml.jackson.databind.ObjectMapper;

public class JsonITestCasesDataSource implements ITestCasesDataSource {

	private static ITestCasesDataSource instance;
	private static final Object lock = new Object();
	private List<String> exludeMethodsList = new ArrayList<String>();
	private List<String> includeMethodsList = new ArrayList<String>();
	private HashMap<String, String> includeOrExludeClassesList = new HashMap<String, String>();

	@SuppressWarnings("unchecked")
	private JsonITestCasesDataSource() {
		String JsonTestMethodsFilePath = PathConstants.jsonTestCasesFilePath;
		String JsonTestModulesFilePath = PathConstants.jsonTestModulesFilePath;
		ObjectMapper objectMapperOfExcludeMethods = new ObjectMapper();

		try {

			File jsonFile = new File(JsonTestMethodsFilePath);

			@SuppressWarnings("rawtypes")
			Map data = objectMapperOfExcludeMethods.readValue(jsonFile, Map.class);

			if (data != null) {

				exludeMethodsList = (List<String>) data.keySet().stream()
						.filter(x -> data.get(x.toString()).toString().equalsIgnoreCase("No"))
						.collect(Collectors.toList());

				includeMethodsList = (List<String>) data.keySet().stream()
						.filter(x -> data.get(x.toString()).toString().equalsIgnoreCase("Yes"))
						.collect(Collectors.toList());

			}

		} catch (IOException e) {
			e.printStackTrace();

		}

		ObjectMapper objectMapperOfClasses = new ObjectMapper();

		try {
			// Replace "data.json" with your JSON file path
			File jsonFile = new File(JsonTestModulesFilePath);

			@SuppressWarnings({ "rawtypes" })
			Map data = objectMapperOfClasses.readValue(jsonFile, Map.class);

			includeOrExludeClassesList = (HashMap<String, String>) data;

		} catch (IOException e) {
			e.printStackTrace();

		}

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
				instance = new JsonITestCasesDataSource();
			}
			return instance;

		}

	}

}
