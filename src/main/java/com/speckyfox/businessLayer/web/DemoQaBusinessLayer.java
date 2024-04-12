package com.speckyfox.businessLayer.web;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.LinkedMap;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.speckyfox.drivermanager.DriverManager;
import com.speckyfox.logmanager.LogsManager;
import com.speckyfox.pageObjects.webPages.DemoQaWebPage;
import com.speckyfox.utilities.commonUtilityFunctions.CommonUtils;

public class DemoQaBusinessLayer extends DemoQaWebPage{

	public DemoQaBusinessLayer(WebDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}
	
	
	public ArrayList<String> createWebTable(LinkedHashMap<String,String> map) 
	{
		ArrayList<String> result=new ArrayList<String>();
		ArrayList<String>expectedResult=new ArrayList<String>();
		ArrayList<String>actualResult=new ArrayList<String>();
		
		if(CommonUtils.click(DriverManager.getDriver(), getAddBtn(10), "Add button", Duration.ofSeconds(10)))
		{
			LogsManager.getLogger().info("Clicked on Add button");
			
			int k=0;
			for(LinkedMap.Entry<String, String> entry:map.entrySet())
			{
				
				if(CommonUtils.sendKeys(DriverManager.getDriver(), inputBoxRegistrationForm(entry.getKey(),10), entry.getValue(), entry.getKey()+" label name", Duration.ofSeconds(15)))
				{
					k++;
					expectedResult.add(entry.getValue());
				}
				
			}
			if(k==map.size())
			{
				if(CommonUtils.click(DriverManager.getDriver(), getSubmitBtn(15), "Submit button", Duration.ofSeconds(15)))
				{
					LogsManager.getLogger().info("Clicked on submit button");
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				else
				{
					LogsManager.getLogger().error("Not able to click on submit button");
					result.add("Not able to click on submit button");
				}
			}
			else
			{
				LogsManager.getLogger().error("Not able to enter the all value in fields");
				result.add("Not able to enter the all value in fields");
			}
			
		}
		else
		{
			LogsManager.getLogger().info("Not able to click on Add button");
			result.add("Not able to click on Add button");
		}
		return result;
	}
	
	
	public ArrayList<String> verifyWebtableRecord(List<String> expectedResult)
	{
		ArrayList<String> actualResult=new ArrayList<String>();
		ArrayList<String> result=new ArrayList<String>();

		int size=getWebTableRow().size();
		List<WebElement> elements=getWebTableRowRecord(size);
		
		for(int i=0; i<elements.size(); i++)
		{
			String actualVal=CommonUtils.getText(DriverManager.getDriver(),elements.get(i) ,"record");
			actualResult.add(actualVal);
			
		}
		
		if(actualResult.size()==expectedResult.size())
		{
			if(actualResult.containsAll(actualResult))
			{
				LogsManager.getLogger().info("All Webtable has been verified");
			}
			else
			{
				LogsManager.getLogger().error("Webtable values are not verified. Actual value "+actualResult+" Expected values "+expectedResult);
				result.add("Webtable values are not verified. Actual value "+actualResult+" Expected values "+expectedResult);
			}
		}
		else
		{
			LogsManager.getLogger().error("The size of actual data and expected data are not matched");
			result.add("The size of actual data and expected data are not matched");
		
		}
		
		return result;
	}
	
}
