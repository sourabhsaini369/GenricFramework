package com.speckyfox.framework;

import org.testng.TestNG;

import com.speckyfox.cucumberRunnerSetup.CucumberRunnerSetup;

public class CucumberIFramework implements IFramework {

	@Override
	public void runner() {

		try{
		TestNG testNG = new TestNG();
		// Add the Test Runner class to the TestNG object
		testNG.setTestClasses(new Class[] { CucumberRunnerSetup.class });
		
		
		// Run the tests
		testNG.run();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}

}
