package com.speckyfox.TestCasesData;

import java.util.HashMap;
import java.util.List;

public interface ITestCasesDataSource {

	public List<String> excludeMethods();

	public List<String> includeMethods();

	public HashMap<String, String> classesIncludeOrNot();

}
