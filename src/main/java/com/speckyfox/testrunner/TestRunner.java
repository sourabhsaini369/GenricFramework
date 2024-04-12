package com.speckyfox.testrunner;

import com.speckyfox.framework.FrameworkManager;

public class TestRunner {

	public static void main(String[] args) throws Exception {

		try {
			FrameworkManager.getFramework().runner();
		} catch (Exception e) {

			e.printStackTrace();
		}

	}
}
