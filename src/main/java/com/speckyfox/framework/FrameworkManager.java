package com.speckyfox.framework;

import com.speckyfox.enums.EnumConstants.Framework;
import com.speckyfox.frameworkfactory.FrameworkFactory;

public class FrameworkManager {

	public static IFramework getFramework() {

		Framework getFrameworkEnumBasedOnPropertyFile = FrameworkFactory.getInstance().getFrameworkEnum();
		return FrameworkFactory.getInstance().createObject(getFrameworkEnumBasedOnPropertyFile);

	}

}
