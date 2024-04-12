package com.speckyfox.TestCasesData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataBaseITestCasesDataSource implements ITestCasesDataSource {

	private static DataBaseITestCasesDataSource instance;
	private static final Object lock = new Object();
	private List<String> exludeMethodsList = new ArrayList<String>();
	private List<String> includeMethodsList = new ArrayList<String>();
	private HashMap<String, String> exludeClassesList = new HashMap<String, String>();

	private DataBaseITestCasesDataSource() {

		String url = "jdbc:mysql://localhost:3306/Master_Test_Cases";
		String username = "root";
		String password = "root";
		String tableNameofTestCases = "Test_Cases";
		String columnNameOfTestCases = "TestCases";
		String columnNameOfTestCasesOfFlags = "Execution";

		String tableNameofModules = "Modules";
		String columnNameOfModules = "Module_Name";
		String columnNameOfModulesOfFlags = "Execution";
		Connection connection = null;
		Statement statement = null;
		Statement statementInclude = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(url, username, password);

			// Create a statement object to execute SQL commands
			statement = connection.createStatement();
			statementInclude = connection.createStatement();

			// Execute SQL commands
			String sqlQueryForTestCases = "SELECT " + columnNameOfTestCases + " from " + tableNameofTestCases
					+ " where " + columnNameOfTestCasesOfFlags + " = \"No\"";
			String sqlQueryForTestCasesInclude = "SELECT " + columnNameOfTestCases + " from " + tableNameofTestCases
					+ " where " + columnNameOfTestCasesOfFlags + " = \"Yes\"";

			String sqlQueryForModules = "SELECT * from " + tableNameofModules + "";
			ResultSet resultSetForTestCases = statement.executeQuery(sqlQueryForTestCases);
			ResultSet resultSetForTestCasesInclude = statementInclude.executeQuery(sqlQueryForTestCasesInclude);

			// Process the results if needed

			while (resultSetForTestCases.next()) {

				exludeMethodsList.add(resultSetForTestCases.getString(columnNameOfTestCases));
				

			}
			
			while (resultSetForTestCasesInclude.next()) {

				includeMethodsList.add(resultSetForTestCasesInclude.getString(columnNameOfTestCases));
				

			}
			ResultSet resultSetForModules = statement.executeQuery(sqlQueryForModules);

			while (resultSetForModules.next()) {

				exludeClassesList.put(resultSetForModules.getString(columnNameOfModules),
						resultSetForModules.getString(columnNameOfModulesOfFlags));

			}

			resultSetForTestCases.close();
			resultSetForModules.close();

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

		finally {

			// Close resources in a finally block to ensure they're closed properly
			try {
				if (statement != null) {
					statement.close();

				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

	}

	public static ITestCasesDataSource getInstance() {

		synchronized (lock) {

			if (instance == null) {
				instance = new DataBaseITestCasesDataSource();
			}
			return instance;

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

		return exludeClassesList;
	}

}
