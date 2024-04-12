package com.speckyfox.TestCasesData;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.speckyfox.constants.ConfigConstants;
import com.speckyfox.enums.EnumConstants.DataSource;

public class TestCasesDataFactory {

	private final Map<DataSource, Supplier<?>> TestCasesDataSourceSelector;
	private ITestCasesDataSource TestCasesDataSourceInstance;
	private static TestCasesDataFactory instance;
	private static final Object lock = new Object();

	private TestCasesDataFactory() {

		TestCasesDataSourceSelector = new HashMap<>();
		TestCasesDataSourceSelector.put(DataSource.DATABASE, DataBaseITestCasesDataSource::getInstance);
		TestCasesDataSourceSelector.put(DataSource.EXCEL, ExcelITestCasesDataSource::getInstance);
		TestCasesDataSourceSelector.put(DataSource.JSON, JsonITestCasesDataSource::getInstance);

	}

	public static TestCasesDataFactory getInstance() {

		synchronized (lock) {

			if (instance == null) {
				instance = new TestCasesDataFactory();
			}
			return instance;

		}

	}

	public ITestCasesDataSource createObject(DataSource dataSource) {

		synchronized (this)

		{

			if (this.TestCasesDataSourceInstance == null) {
				return this.TestCasesDataSourceInstance = (ITestCasesDataSource) TestCasesDataSourceSelector
						.get(dataSource).get();

			} else {
				return this.TestCasesDataSourceInstance;
			}

		}

	}

	public DataSource getTestCasesDataSourceEnum() {
		DataSource getTestCaseDataSourceFromPropertyFile = null;

		try {

			getTestCaseDataSourceFromPropertyFile = DataSource
					.valueOf(ConfigConstants.testingTestCasesDataSource.toUpperCase());

		} catch (IllegalArgumentException illegalException) {

			System.out.println("Please Provide the correct Test Cases Data Source in Config.Property File");
		}
		return getTestCaseDataSourceFromPropertyFile;
	}

}
