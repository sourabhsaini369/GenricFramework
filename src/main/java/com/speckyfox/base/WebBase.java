package com.speckyfox.base;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;


import com.speckyfox.constants.ConfigConstants;
import com.speckyfox.dataprovider.ExcelDataProvider;
import com.speckyfox.driver.IWebDriver;
import com.speckyfox.driver.WebDriverData;
import com.speckyfox.driverfactory.DriverFactory;
import com.speckyfox.drivermanager.DriverManager;
import com.speckyfox.enums.EnumConstants.WebRunMode;
import com.speckyfox.listners.AppListeners;
import com.speckyfox.utilities.excelUtilityFunctions.ExcelUtils;
import com.speckyfox.videoConfig.VideoConfig;

import io.cucumber.java.After;
import io.cucumber.java.Before;

public class WebBase extends ExcelDataProvider {

	@BeforeMethod
	public void WebSetup(ITestResult result)
			throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

		setup(result);
	}

	@Before("@WebHook")
	public void WebSetup()
			throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
		setup();
	}

	@After("@WebHook")
	@AfterMethod
	public void webTearDown(Method method,ITestContext context,ITestResult result)
			throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

		if (!Objects.isNull(DriverManager.getDriver())) {
			DriverManager.getDriver().quit();
			DriverManager.setDriver(null);
			VideoConfig.getInstance().videoRecordingEnd();
		}
		if (method.getParameterTypes().length > 0) {
			Object[] parameters = result.getParameters();
	        String firstColumnValue = (String) parameters[0];
	        String sheetName=ExcelUtils.getSheetNameOfDatasheetFromTestcaseData(method.getName());
	        
	        switch (result.getStatus()) {
            case ITestResult.SUCCESS:
            	ExcelUtils.updateExecutionStatusOnDataSheet(firstColumnValue,sheetName,"Passed");
                break;
            case ITestResult.FAILURE:
                ExcelUtils.updateExecutionStatusOnDataSheet(firstColumnValue,sheetName,"Failed");
                
                break;
            case ITestResult.SKIP:
                ExcelUtils.updateExecutionStatusOnDataSheet(firstColumnValue,sheetName,"Skip");
                break;
        }    
	        
	        
	        
		}

	}

	private void setup(ITestResult result)
			throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
		VideoConfig.getInstance().videoRecordingStart(AppListeners.getMethodOrScenarioName(result), true);
		if (Objects.isNull(DriverManager.getDriver())) {

			WebDriverData driverData = new WebDriverData(ConfigConstants.webBrowser, ConfigConstants.webRunMode,
					ConfigConstants.webRemoteModeType);

			IWebDriver driver = DriverFactory	
					.getDriverForWeb(WebRunMode.valueOf(driverData.getWebRunMode().toUpperCase()));

			WebDriver driverToSet = driver.getDriver(driverData);

			DriverManager.setDriver(driverToSet);

			DriverManager.getDriver().manage().window().maximize();

			DriverManager.getDriver().get(ConfigConstants.webURL);

		}
	}

	private void setup()
			throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
		VideoConfig.getInstance().videoRecordingStart(AppListeners.currentlyExecutingTC, true);
		if (Objects.isNull(DriverManager.getDriver())) {

			WebDriverData driverData = new WebDriverData(ConfigConstants.webBrowser, ConfigConstants.webRunMode,
					ConfigConstants.webRemoteModeType);

			IWebDriver driver = DriverFactory
					.getDriverForWeb(WebRunMode.valueOf(driverData.getWebRunMode().toUpperCase()));

			WebDriver driverToSet = driver.getDriver(driverData);

			DriverManager.setDriver(driverToSet);

			DriverManager.getDriver().manage().window().maximize();

			DriverManager.getDriver().get(ConfigConstants.webURL);

		}
	}

}
