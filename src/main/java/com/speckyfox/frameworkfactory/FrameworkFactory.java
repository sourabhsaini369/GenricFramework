package com.speckyfox.frameworkfactory;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import com.speckyfox.constants.ConfigConstants;
import com.speckyfox.enums.EnumConstants.Framework;
import com.speckyfox.framework.CucumberIFramework;
import com.speckyfox.framework.DataDrivenIFramework;
import com.speckyfox.framework.IFramework;

public class FrameworkFactory {

	private final Map<Framework, Class<?>> FrameworkSelector;
	private IFramework frameWorkInstance;
	private static FrameworkFactory instance;
	private static final Object lock = new Object();

	private FrameworkFactory() {

		FrameworkSelector = new HashMap<>();
		FrameworkSelector.put(Framework.CUCUMBER, CucumberIFramework.class);
		FrameworkSelector.put(Framework.DATADRIVEN, DataDrivenIFramework.class);

	}

	public static FrameworkFactory getInstance() {

		synchronized (lock) {

			if (instance == null) {
				instance = new FrameworkFactory();
			}
			return instance;

		}

	}

	public IFramework createObject(Framework frame) {

		synchronized (this)

		{

			if (this.frameWorkInstance == null) {
				Class<?> clazz = FrameworkSelector.get(frame);
				if (clazz != null) {
					try {

						Constructor<?> constructor = clazz.getDeclaredConstructor();
						this.frameWorkInstance = (IFramework) constructor.newInstance();
						return this.frameWorkInstance;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				return null;
			} else {
				return this.frameWorkInstance;
			}

		}

	}

	public Framework getFrameworkEnum() {
		Framework getFrameworkFromPropertyFile = null;

		try {

			getFrameworkFromPropertyFile = Framework.valueOf(ConfigConstants.testingFramework.toUpperCase());

		} catch (IllegalArgumentException illegalException) {

			System.out.println("Please Provide the correct Framework in Config.Property File");
		}
		return getFrameworkFromPropertyFile;
	}

}
