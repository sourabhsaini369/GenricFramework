package com.speckyfox.utilities.commonUtilityFunctions;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.speckyfox.enums.EnumConstants.AlertAction;
import com.speckyfox.listners.AppListeners;
import com.speckyfox.logmanager.LogsManager;
import com.speckyfox.utilities.excelUtilityFunctions.ExcelUtils;

public class CommonUtils {

	public static List<String> excludedMethods = null;
	public static List<String> includedMethods = null;

	public static void execution() {
		excludedMethods = new ArrayList<String>();
		includedMethods = new ArrayList<String>();
		excludedMethods.addAll(ExcelUtils.getTcsToRun("testcases", "no"));
		includedMethods.addAll(ExcelUtils.getTcsToRun("testcases", "yes"));
		for (int i = 0; i < excludedMethods.size(); i++) {
			AppListeners.status.put(excludedMethods.get(i), "Skip: Disabled in excel.");
		}
	}

	@SuppressWarnings("rawtypes")
	public static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
		@SuppressWarnings("rawtypes")
		List<Class> classes = new ArrayList<Class>();
		if (!directory.exists()) {
			LogsManager.getLogger().error("Directory not found: " + directory);

			return classes;
		}
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				assert !file.getName().contains(".");
				classes.addAll(findClasses(file, packageName + "." + file.getName()));
			} else if (file.getName().endsWith(".class")) {
				classes.add(
						Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
			}
		}
		return classes;
	}

	@SuppressWarnings("rawtypes")
	public static Class[] getClasses(String packageName) {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		assert classLoader != null;
		String path = packageName.replace('.', '/');
		Enumeration<URL> resources;
		List<Class> classes = null;
		String folderPath = null;
		try {
			resources = classLoader.getResources(path);
			List<File> dirs = new ArrayList<File>();
			while (resources.hasMoreElements()) {
				URL resource = resources.nextElement();
				dirs.add(new File(resource.getFile()));
			}
			classes = new ArrayList<Class>();
			for (File directory : dirs) {
				try {
					if (directory.toString().contains("%20")) {
						folderPath = directory.toString().replace("%20", " ");
					} else {
						folderPath = directory.toString();
					}
					classes.addAll(findClasses(new File(folderPath), packageName));
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return classes.toArray(new Class[classes.size()]);
	}

//	public static WebElement getElementVisibility(WebDriver driver, List<WebElement> elements, Duration timeout,
//			String elementName) {
//		try {
//			if (elements != null && elements.size() != 0) {
//				WebDriverWait wait = new WebDriverWait(driver, timeout);
//
//				WebElement ele = null;
//				for (WebElement element : elements) {
//					try {
//
//						scrollDownThroughWebelement(driver, element, elementName);
//
//						ele = wait.until(ExpectedConditions.visibilityOf(element));
//						if (ele != null) {
//							LogsManager.getLogger().info("Element Found: " + elementName);
//							break;
//						}
//					} catch (Exception e) {
//						continue;
//					}
//				}
//				return ele;
//
//			} else {
//				return null;
//			}
//		} catch (Exception e) {
//
//			LogsManager.getLogger().info("Exception Occured: " + e.getMessage());
//			return null;
//		}
//	}

	public static WebElement getElementVisibility(WebDriver driver, List<WebElement> elements, String optionToSelect,
			Duration timeout, String elementName, boolean ignoreCases) {
		try {
			if (elements != null && elements.size() != 0) {

				WebDriverWait wait = new WebDriverWait(driver, timeout);
				WebElement ele = null;

				if (ignoreCases) {
					elements = elements.stream().filter(x -> x.getText().equalsIgnoreCase(optionToSelect))
							.collect(Collectors.toList());
				} else {
					elements = elements.stream().filter(x -> x.getText().equals(optionToSelect))
							.collect(Collectors.toList());
				}
				for (WebElement element : elements) {
					scrollDownThroughWebelement(driver, element, elementName);
					ele = wait.until(ExpectedConditions.visibilityOf(element));
					if (ele != null) {

						LogsManager.getLogger().info("Element Found: " + elementName);

						break;
					}
				}
				return ele;

			} else {
				return null;
			}
		} catch (Exception e) {

			LogsManager.getLogger().error("Exception Occured: " + e.getMessage());

			return null;
		}
	}

	public static WebElement FindElement(WebDriver driver, String xpath, String elementName, int timeOut) {

		try {
			int timeout = 0;
			WebElement ele = null;
			while (true) {
				try {
					ele = driver.findElement(By.xpath(xpath));
					break;
				} catch (Exception e) {

					Thread.sleep(250);
					timeout++;
					if (timeout > timeOut * 4) {
						ele = driver.findElement(By.xpath(xpath));
						break;
					}
				}
			}
			if (elementName != null && !elementName.isEmpty() && ele != null) {
				scrollDownThroughWebelement(driver, ele, elementName);

			}
			return ele;
		} catch (Exception e) {
			LogsManager.getLogger().error("No Element Found Named: " + elementName);

		}
		return null;
	}

	public static WebElement FindElement(WebDriver driver, By by, String elementName, int timeOut) {

		try {
			int timeout = 0;
			WebElement ele = null;
			while (true) {
				try {
					ele = driver.findElement(by);
					break;
				} catch (Exception e) {

					Thread.sleep(250);
					timeout++;
					if (timeout > timeOut * 4) {
						ele = driver.findElement(by);
						break;
					}
				}
			}
			if (elementName != null && !elementName.isEmpty() && ele != null) {
				scrollDownThroughWebelement(driver, ele, elementName);

			}
			return ele;
		} catch (StaleElementReferenceException e) {

			return FindElement(driver, by, elementName, timeOut);
		} catch (Exception e) {
			LogsManager.getLogger().error("No Element Found Named: " + elementName);

		}
		return null;
	}

	public static boolean click(WebDriver driver, WebElement element, String elementName, Duration timeout) {
		try {

			WebDriverWait wait = new WebDriverWait(driver, timeout);
			scrollDownThroughWebelement(driver, element, elementName);
			element = wait.until(ExpectedConditions.elementToBeClickable(element));

			element.click();

			LogsManager.getLogger().info("Clicked on Element: " + elementName);

			return true;

		} catch (Exception e) {

			try {

				return clickUsingJavaScript(driver, element, elementName);
			} catch (Exception excep) {
				LogsManager.getLogger().error("Not Able to Click Using JavaScriptClick on Element: " + elementName);
				LogsManager.getLogger().error("Getting exception: " + excep);

				return false;

			}

		}
	}

	public static boolean sendKeys(WebDriver driver, WebElement element, String value, String elementName,
			Duration timeout) {
		try {
			if (element != null) {

				WebDriverWait wait = new WebDriverWait(driver, timeout);
				scrollDownThroughWebelement(driver, element, elementName);
				element.clear();
				element.sendKeys(value);

				if (element.getTagName().equals("input")) {
					if (element.getAttribute("type").equals("file")) {
						LogsManager.getLogger().info("Entered Value: " + value + " to the element: " + elementName);

						return true;
					}
				}

				if (wait.until(ExpectedConditions.textToBePresentInElementValue(element, value))) {
					LogsManager.getLogger().info("Entered Value: " + value + " to the element: " + elementName);

				} else {
					LogsManager.getLogger()
							.error("Not Able to Enter Value: " + value + " to the element: " + elementName);

					return false;
				}

				return true;

			} else {

				LogsManager.getLogger().error("No Element Found Named: " + elementName);

				return false;
			}
		} catch (Exception e) {

			LogsManager.getLogger().error("Not Able to Enter Value: " + value);
			LogsManager.getLogger().error("Getting exception: " + e);

			return false;
		}
	}

	public static boolean scrollDownThroughWebelement(WebDriver driver, WebElement Element, String elementName) {
		try {
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", Element);
			if (elementName != "")
				LogsManager.getLogger().info("Window Scrolled to " + elementName);

			return true;
		} catch (Exception e) {
			if (elementName != "")
				LogsManager.getLogger().error("Can not scrolled Window to " + elementName);

			return false;
		}
	}

	public static List<WebElement> FindElements(WebDriver driver, String xpath, String elementName) {
		try {
			for (int i = 0; i < 41; i++) {
				if (driver.findElements(By.xpath(xpath)).isEmpty()) {

					Thread.sleep(250);

				} else {
					break;
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return driver.findElements(By.xpath(xpath));

	}

	/**
	 * @author Ankur Huria
	 * @param driver
	 * @return String
	 * @description switch to the very next window open and return the parent
	 *              session id.
	 */
	public static String switchToWindowOpenNextToParentWindow(WebDriver driver) {
		int limitForWait = 0;
		String parentWindowId = driver.getWindowHandle();
		String childWindowID = null;
		Set<String> s1 = null;
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (true) {

			s1 = driver.getWindowHandles();
			if (s1.size() <= 1) {
				try {
					Thread.sleep(500);
					limitForWait++;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (limitForWait > 200) {
					LogsManager.getLogger().error("No new window is open for switch.");

					return null;
				}
			} else {
				break;
			}
		}
		Iterator<String> I1 = s1.iterator();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (I1.hasNext()) {
			childWindowID = I1.next();

			if (parentWindowId.equals(childWindowID)) {
				if (I1.hasNext()) {
					childWindowID = I1.next();
					LogsManager.getLogger()
							.info("parent window: " + parentWindowId + ">>>>> child window: " + childWindowID);

					if (!parentWindowId.equals(childWindowID)) {
						LogsManager.getLogger().info("child window :" + childWindowID);

						try {
							driver.switchTo().window(childWindowID);
						} catch (NoSuchWindowException e) {
							LogsManager.getLogger().error("No Such Window");

							e.printStackTrace();
							return null;
						}
						LogsManager.getLogger().error(2000);
						break;
					}
				} else {

					return null;
				}

			}

		}
		return parentWindowId;
	}

	public static boolean switchToFrame(WebDriver driver, Duration timeOut, WebElement frameElement) {

		WebDriverWait wait = new WebDriverWait(driver, timeOut);
		if (frameElement != null) {
			try {
				if (wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameElement)) != null) {
					LogsManager.getLogger().info("Switched to the Frame");

					return true;
				} else {
					LogsManager.getLogger().info("Not Able to Switched to the Frame");

					return false;
				}
			} catch (Exception e) {

				LogsManager.getLogger().error(
						"Not Able to Switched to the Frame as the exception Occured: " + e.getLocalizedMessage());

				return false;
			}

		} else {
			LogsManager.getLogger().error("As No Element Frame Found, So not able to switch to it");

			return false;
		}

	}

	public List<Class<?>> getClassesImplementingInterface(String packageName, Class<?> interfaceType) {
		List<Class<?>> implementingClasses = new ArrayList<>();

		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			String path = packageName.replace('.', '/');
			java.net.URL resource = classLoader.getResource(path);

			if (resource != null) {
				java.io.File directory = new java.io.File(resource.getFile());
				if (directory.exists()) {
					java.io.File[] files = directory.listFiles();
					for (java.io.File file : files) {
						if (file.getName().endsWith(".class")) {
							String className = packageName + '.'
									+ file.getName().substring(0, file.getName().length() - 6);
							Class<?> clazz = Class.forName(className);
							if (interfaceType.isAssignableFrom(clazz) && !clazz.isInterface() && !clazz.isEnum()) {
								implementingClasses.add(clazz);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return implementingClasses;
	}

	public static boolean clickUsingJavaScript(WebDriver driver, WebElement element, String elementName) {

		try {

			((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
			LogsManager.getLogger().info("Able to Clicked using JavaScript");

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			LogsManager.getLogger().error("Exception in Clicked using JavaScript");
			LogsManager.getLogger().error("Cannot Click Element: " + elementName);

		}
		LogsManager.getLogger().error("Not Able to Click using JavaScript");

		return false;
	}

	public static boolean adjustZoomOfScreen(WebDriver driver, String zoomPercentage) {

		try {

			((JavascriptExecutor) driver).executeScript("document.body.style.zoom='" + zoomPercentage + "%'");
			LogsManager.getLogger().info("Zoom Set to : " + zoomPercentage + "%");

			return true;
		} catch (Exception e) {
			// TODO: handle exception
			LogsManager.getLogger().error("Not able to Zoom Set to : " + zoomPercentage + "%");

		}

		return false;
	}

	public static boolean datePickerHandle(WebDriver driver, WebElement MonthSelector, WebElement YearSelector,
			String DaySelectorXpath, String elementName, String Year, String Month, String Date, Duration timeout) {
		boolean flag = false;
		try {

			if (selectVisibleTextFromDropDown(driver, YearSelector, "YearSelector: " + Year, Year))

			{

				LogsManager.getLogger().info("Select The Year: " + Year);

				if (selectVisibleTextFromDropDown(driver, MonthSelector, "MonthSelector: " + Month, Month))

				{

					LogsManager.getLogger().info("Month is Selected: " + Month);

					List<WebElement> DaySelector = FindElements(driver, DaySelectorXpath, "dateSelector");
					for (WebElement day : DaySelector) {
						try {

							if (Integer.parseInt(Date) < 10 && Date.length() == 2) {
								Date = Date.replace("0", "");
							}

							if (day.getText().trim().equalsIgnoreCase(Date)) {
								if (click(driver, day, "", timeout)) {

									LogsManager.getLogger().info(Date + " Date is Selected");

									flag = true;
								} else {

									LogsManager.getLogger().error(Date + " Date is Not Selected");

									flag = false;
								}
							}
						} catch (StaleElementReferenceException e) {
							e.getMessage();
						}
					}

				} else {
					LogsManager.getLogger().error("Not ABle to Select Month: " + Month);

				}
			} else {
				LogsManager.getLogger().error("Not ABle to Select Year: " + Year);

			}

		} catch (Exception e) {
			flag = false;

			LogsManager.getLogger().error(elementName + " Date is Not Selected");
			LogsManager.getLogger().error(e.getMessage());

		}

		return flag;

	}

	public static boolean selectVisibleTextFromDropDown(WebDriver driver, WebElement webElement, String elementName,
			Object value) {
		try {
			checkElementVisibility(driver, webElement, elementName, Duration.ofSeconds(30));
			Select select = new Select(webElement);
			if (value instanceof Integer) {
				LogsManager.getLogger().info("Selecting value by index: " + value);

				select.selectByIndex(Integer.parseInt(value.toString()));
			} else {
				try {
					select.selectByVisibleText(value.toString());
				} catch (Exception e) {
					select.selectByValue(value.toString());
				}
			}
			LogsManager.getLogger().info("Selected " + value.toString() + " from the drop down.");

			return true;
		} catch (Exception e) {
			if (value instanceof Integer) {
				LogsManager.getLogger().info("Index passed is not found.");

			} else {
				LogsManager.getLogger().error("'" + value.toString() + "' is not available in drop down.");

			}
			return false;
		}
	}

	public static boolean checkElementVisibility(WebDriver driver, WebElement element, String elementName,
			Duration time) {
		WebDriverWait wait = new WebDriverWait(driver, time);
		WebElement ClickElement = null;
		try {

			ClickElement = wait.until(ExpectedConditions.visibilityOf(element));
			if (ClickElement != null) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {

			return false;
		}
	}

	public static boolean isValidDate(String dateStr, String formatStr) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(formatStr);
		dateFormat.setLenient(false);
		try {
			dateFormat.parse(dateStr);
			return true;
		} catch (ParseException e) {
			return false;
		}
	}

	public static boolean mouseOverOperation(WebDriver driver, WebElement webElement) {
		Actions actions = new Actions(driver);
		try {
			actions.moveToElement(webElement).build().perform();
			LogsManager.getLogger().info("Mouse Over Performed on " + webElement);

		} catch (Exception e) {
			LogsManager.getLogger().error("Mouse Over Not able to Perform on " + webElement);
			return false;
		}
		return true;
	}

	public static boolean scrollDownThroughWebelementInCenter(WebDriver driver, WebElement Element,
			String elementName) {
		try {
			JavascriptExecutor j = (JavascriptExecutor) driver;
			j.executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'})", Element);

			LogsManager.getLogger().info("Window Scrolled to " + elementName);
			return true;
		} catch (Exception e) {

			LogsManager.getLogger().error("Can not scrolled Window to " + elementName);
			return false;
		}
	}

	public static String getValueFromElementUsingJavaScript(WebDriver driver, WebElement element, String elementName) {
		String text = null;
		try {

			text = (String) ((JavascriptExecutor) driver).executeScript("return arguments[0].value;", element);
			if (!Objects.isNull(text)) {
				LogsManager.getLogger().info("Get the value from Element: " + elementName);
			} else {
				LogsManager.getLogger()
						.error("Cannot get the value from Element: " + elementName + " as it return null");

			}
		} catch (Exception e) {

			LogsManager.getLogger().error("Cannot get the value from Element: " + elementName);
		}
		return text;
	}

	public static boolean switchToAlertAndAcceptOrDecline(WebDriver driver, int timeForWait, AlertAction alert) {
		int a = timeForWait * 2;
		while (true) {
			if (isAlertPresent(driver)) {
				LogsManager.getLogger().info("Alert is Present and Switched to it successfully");
				if (alert == AlertAction.ACCEPT) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {

						e.printStackTrace();
					}
					driver.switchTo().alert().accept();
					LogsManager.getLogger().info("Alert Successfully accepted.");
					return true;
				} else if (alert == AlertAction.DECLINE) {
					driver.switchTo().alert().dismiss();
					LogsManager.getLogger().info("Alert Successfully Declined.");
					return true;
				} else {
					LogsManager.getLogger().error("Kindly check the key passed.");
					return false;
				}
			} else {

				try {

					Thread.sleep(500);
					a--;
					if (a <= 0) {
						LogsManager.getLogger().error("Not able to accept the alert in given time.");
						return false;
					}
				} catch (InterruptedException e) {

					e.printStackTrace();

				}
			}
		}

	}

	public static boolean isAlertPresent(WebDriver driver) {
		try {
			driver.switchTo().alert();

			return true;
		} catch (NoAlertPresentException e) {

			return false;
		}
	}

	public static void refresh(WebDriver driver, Duration timeOut)
	{
		driver.navigate().refresh();
	}
	
	public static String getText(WebDriver driver,WebElement element, String elementName)
	{
		String text="";
		try
		{
			text=element.getText();
			if(text!=null)
			{
				LogsManager.getLogger().info(text+ " value from Element: " + elementName);
			}
			else
			{
				LogsManager.getLogger()
				.error("Cannot get the value from Element: " + elementName + " as it return null");
			}
		}
		catch(Exception ex)
		{
			LogsManager.getLogger().error("Cannot get the value from Element: " + elementName);
		}
		return text;
	}
}
