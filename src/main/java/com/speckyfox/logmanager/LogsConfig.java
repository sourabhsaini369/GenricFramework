package com.speckyfox.logmanager;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.PropertyConfigurator;

import com.speckyfox.constants.PathConstants;

public class LogsConfig {

	public static void config() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");

		System.setProperty("LogFileLocation", PathConstants.logFileName + sdf.format(date) + ".log");
		PropertyConfigurator.configure(PathConstants.log4jConfigFileName);
	}

}
