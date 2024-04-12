package com.speckyfox.drivermanager;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;

public class EdgeManager implements ILocalBrowserManager {
	
	public EdgeManager() {
	}

	public WebDriver getDriver() {

		WebDriver edgeDriver = new EdgeDriver();
		return edgeDriver;
	}

}
