package com.speckyfox.businessLayer.web;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.speckyfox.drivermanager.DriverManager;
import com.speckyfox.logmanager.LogsManager;
import com.speckyfox.pageObjects.webPages.HiCareAppWebPage;
import com.speckyfox.utilities.commonUtilityFunctions.CommonUtils;

import net.bytebuddy.agent.builder.AgentBuilder.RedefinitionListenable.ResubmissionImmediateMatcher;

public class HiCareAppWebPageBusinessLayer extends HiCareAppWebPage {

	public HiCareAppWebPageBusinessLayer(WebDriver driver) {
		super(driver);

	}

	public boolean login(String userName, String password) {

		CommonUtils.sendKeys(DriverManager.getDriver(), getEmailInputBox(10), userName, "Email Input Box",
				Duration.ofSeconds(10));
		CommonUtils.sendKeys(DriverManager.getDriver(), getPasswordInputBox(5), password, "Password Input Box",
				Duration.ofSeconds(10));
		CommonUtils.click(DriverManager.getDriver(), getSignInButton(5), "Login Button", Duration.ofSeconds(5));

		if (getHicareLogo(10) != null) {
			LogsManager.getLogger().info("Logged in Successfull");
			return true;
		}

		LogsManager.getLogger().error("Logged in Not Successfull");
		return false;

	}

	public List<String> verifyUserTravelLocation(String userSelect, String startDate, String endDate,
			String expectedAddress) {

		JavascriptExecutor js = (JavascriptExecutor) driver;
		List<String> negativeResult = new ArrayList<String>();
		CommonUtils.click(DriverManager.getDriver(), getSideMenuLink("Navigation", 10),
				"Side Menu Link: " + "Navigation", Duration.ofSeconds(5));
		CommonUtils.click(DriverManager.getDriver(), getSideMenuLink("Location", 10), "Side Menu Link: " + "Location",
				Duration.ofSeconds(5));
		if (CommonUtils.selectVisibleTextFromDropDown(DriverManager.getDriver(), getUserListSelectElement(10),
				"User Select: " + userSelect, userSelect)) {
			CommonUtils.click(DriverManager.getDriver(), getViewLocationButton(10), "View Location Button: ",
					Duration.ofSeconds(5));
			if (getUserOnMap(10) != null) {
				if (!CommonUtils.isValidDate(startDate, "yyyy-MM-dd")) {
					negativeResult.add("Start Date Format is not Correct, It Should be: " + "yyyy-MM-dd");

				}
				if (!CommonUtils.isValidDate(endDate, "yyyy-MM-dd")) {
					negativeResult.add("End Date Format is not Correct, It Should be: " + "yyyy-MM-dd");
				}

				js.executeScript("arguments[0].value=arguments[1];", getStartDate(10), startDate);
				LogsManager.getLogger().info("Enter Start Date: " + startDate);
				js.executeScript("arguments[0].value=arguments[1];", getEndDate(10), endDate);
				LogsManager.getLogger().info("Enter End Date: " + endDate);

				CommonUtils.click(DriverManager.getDriver(), getResultButton(10), "Result Button",
						Duration.ofSeconds(5));

				if (get1ImageInMap(8) != null) {
					LogsManager.getLogger().info("Image Link 1 is Showing in Map");
					if (CommonUtils.mouseOverOperation(DriverManager.getDriver(), get1ImageInMap(8))) {
						if (getAddressInMap(10) != null) {
							String actualAddressAfterHover = getAddressInMap(10).getText();
							if (expectedAddress.equals(actualAddressAfterHover)) {
								LogsManager.getLogger().info("Address Verified on Map: " + actualAddressAfterHover);
							}

							else {
								negativeResult.add("Address Not Verified on Map, Expected " + expectedAddress
										+ " but Actual is: " + actualAddressAfterHover);
								LogsManager.getLogger().error("Address Not Verified on Map, Expected " + expectedAddress
										+ " but Actual is: " + actualAddressAfterHover);
							}
						} else {
							negativeResult.add("No Element Found Having Address After Hover");
							LogsManager.getLogger().error("No Element Found Having Address After Hover");
						}
					} else {
						negativeResult.add("Not Able to Mouse Hover to The Image Link 1");
						LogsManager.getLogger().error("Not Able to Mouse Hover to The Image Link 1");
					}
				} else {
					negativeResult.add("Image Link 1 is not Showing in Map");
					LogsManager.getLogger().error("Image Link 1 is not Showing in Map");
				}

				if (getAddressInTable(10) != null) {
					String actualAddressInTable = getAddressInTable(10).getText();
					if (expectedAddress.equals(actualAddressInTable)) {
						LogsManager.getLogger().info("Address Verified on Table: " + actualAddressInTable);
					}

					else {
						negativeResult.add("Address Not Verified on Table, Expected " + expectedAddress
								+ " but Actual is: " + actualAddressInTable);
						LogsManager.getLogger().error("Address Not Verified on Table, Expected " + expectedAddress
								+ " but Actual is: " + actualAddressInTable);
					}
				} else {
					negativeResult.add("No Address Column Found in Table");
					LogsManager.getLogger().error("No Address Column Found in Table");
				}

			} else {
				negativeResult.add("User On Map Text is not present");
				LogsManager.getLogger().error("User On Map Text is not present");
			}
		} else {
			negativeResult.add("Not Able to Select User: " + userSelect);
			LogsManager.getLogger().error("Not Able to Select User: " + userSelect);
		}

		return negativeResult;
	}

	
	public ArrayList<String> createAuditFrequency(String name, String day, String status)
	{
		ArrayList<String> result=new ArrayList<String>();
		JavascriptExecutor js = (JavascriptExecutor) driver;
		if(CommonUtils.click(DriverManager.getDriver(), getSideMenuLink(15, "Maintenance"),
				"Side Menu Link: " + "Maintenance", Duration.ofSeconds(5)))
		{
			LogsManager.getLogger().info("Maintenance side menu has been selected");
			
			if(CommonUtils.click(DriverManager.getDriver(), getSideMenuLink("Audit Frequency", 10), "Side Menu Link: " + "Audit Frequency",
					Duration.ofSeconds(15)))
			{
				LogsManager.getLogger().info("Not able to clicked on Maintenance side menu link");
				if(CommonUtils.click(DriverManager.getDriver(), getAddAuditFrequency(10), "Audit Frequency",
						Duration.ofSeconds(15)))
				{
					LogsManager.getLogger().info("Clicked on add audit frequency link");
					
					
					if(CommonUtils.sendKeys(DriverManager.getDriver(), getAuditFrequencyTextField("Name", 10), name, "Audit Frequency days",
							Duration.ofSeconds(10)))
					{
						LogsManager.getLogger().info(name+ " name has been passed in Name field");
						getAuditFrequencyTextField("Days", 10).click();
						if(CommonUtils.sendKeys(DriverManager.getDriver(), getAuditFrequencyTextField("Days", 10), day, "Audit Frequency days",
								Duration.ofSeconds(10)))
						{
							LogsManager.getLogger().info(day+ " days has been passed in days field");
							
							if(CommonUtils.selectVisibleTextFromDropDown(DriverManager.getDriver(), getAuditFrequencyTextField(20),"Status",status))
							{
								LogsManager.getLogger().info(status+ " has been selected from status field");
								if(CommonUtils.click(DriverManager.getDriver(), getSaveBtn("Save", 10), "Save button", Duration.ofSeconds(15)))
								{
									LogsManager.getLogger().info("Clicked on save button");
								}
								else
								{
									LogsManager.getLogger().error("Not able to click on save button");
									result.add("Not able to click on save button");
								}
								
							}
							else
							{
								LogsManager.getLogger().error(status+ " is not selected from status field");
								result.add(status+ " is not selected from status field");
							}	
						}
						else
						{
							LogsManager.getLogger().error(day+ " days is not passed in days field");
							result.add(day+ " days is not passed in days field");
						}
						
					}
					else
					{
						LogsManager.getLogger().error(name+ " name is not passed in Name field");
						result.add(name+ " name is not passed in Name field");
					}
				}
				else
				{
					LogsManager.getLogger().error("Not able to clicked on add audit frequency link");
					result.add("Not able to clicked on add audit frequency link");
				}
			}
			else
			{
				LogsManager.getLogger().error("Clicked on Maintenance side menu link");
				result.add("Clicked on Maintenance side menu link");
			}
		}
		else
		{
			LogsManager.getLogger().error("Not able to select Maintenance side menu");
			result.add("Not able to select Maintenance side menu");
		}
		
		return result;
	}
	
	public ArrayList<String> verifyAuditFrequency(String name, List<String> freqList)
	{
		ArrayList<String> actualFrequencyList=new ArrayList<String>();
		ArrayList<String> result=new ArrayList<String>();
		List<WebElement> elements=getFrequencyRecord(name);
		for(int i=1; i<getFrequencyRecord(name).size(); i++)
		{
			actualFrequencyList.add(CommonUtils.getText(DriverManager.getDriver(), getFrequencyRecord(name).get(i), "frequency details"));
		}
		
		for(int i=0; i<freqList.size(); i++)
		{
			if(freqList.get(i).equals(actualFrequencyList.get(i)))
			{
				LogsManager.getLogger().info("Expected value "+freqList.get(i)+" has been matched with actual value "+actualFrequencyList.get(i));
			}
			else
			{
				LogsManager.getLogger().error("Expected value "+freqList.get(i)+" is not matched with actual value "+actualFrequencyList.get(i));	
				result.add("Expected value "+freqList.get(i)+" is not matched with actual value "+actualFrequencyList.get(i));
			}
		}
		return result;
		
		
	}
}
