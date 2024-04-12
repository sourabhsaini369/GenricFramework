package com.speckyfox.TestCasesData;

import com.speckyfox.enums.EnumConstants.DataSource;

public class TestCasesDataSourceManager {

	public static ITestCasesDataSource getTestCasesDataSource() {

		DataSource getDataSourceEnumBasedOnPropertyFile = TestCasesDataFactory.getInstance()
				.getTestCasesDataSourceEnum();
		return TestCasesDataFactory.getInstance().createObject(getDataSourceEnumBasedOnPropertyFile);

	}

}
