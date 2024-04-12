package com.speckyfox.configreader;

import java.io.FileInputStream;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.speckyfox.constants.PathConstants;

public class ConfigReader {

	private static Properties mainPropertyObject;

	private static Object lock = new Object();

	static String configPath = PathConstants.configPropertyFilePath;

	public static Properties getMainPropertyFile() {

		synchronized (lock) {

			{
				if (mainPropertyObject == null) {

					return getPropertyObject(configPath);
				}
				return mainPropertyObject;
			}
		}
	}

	private static Properties getPropertyObject(String path) {

		Properties properties = new Properties();
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(path);
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
		try {
			properties.load(fileInputStream);
		} catch (IOException e) {

			e.printStackTrace();
		}
		return properties;

	}

}
