package com.speckyfox.utilities.commonUtilityFunctions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.speckyfox.drivermanager.DriverManager;
import com.speckyfox.logmanager.LogsManager;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.touch.offset.PointOption;

public class AndroidCommonUtils {

	public static WebElement findElement(String xpath, String elementName, boolean swipeToElement, int timeOut) {

		try {
			int timeout = 0;
			WebElement ele = null;
			while (true) {
				try {
					ele = DriverManager.getDriver().findElement(By.xpath(xpath));
					break;
				} catch (Exception e) {

					Thread.sleep(250);
					timeout++;
					if (timeout > timeOut * 4) {
						ele = DriverManager.getDriver().findElement(By.xpath(xpath));
						break;
					}
				}
			}
			if (elementName != null && !elementName.isEmpty() && ele != null) {
				if (swipeToElement) {
					swipeDownToElement(ele, elementName);
				}

			}
			return ele;
		} catch (Exception e) {
			LogsManager.getLogger().error("No Element Found Named: " + elementName);

		}
		return null;
	}

	public static WebElement findElement(By by, String elementName, boolean swipeToElement, int timeOut) {

		try {
			int timeout = 0;
			WebElement ele = null;
			while (true) {
				try {
					ele = DriverManager.getDriver().findElement(by);
					break;
				} catch (Exception e) {

					Thread.sleep(250);
					timeout++;
					if (timeout > timeOut * 4) {
						ele = DriverManager.getDriver().findElement(by);
						break;
					}
				}
			}
			if (ele != null) {
				LogsManager.getLogger().info("Element Found named: " + elementName);
				if (swipeToElement) {
					int loopCount = 0;
					while (loopCount < 3) {
						swipeDownToElement(ele, elementName);
						loopCount++;

					}

				}

			}
			return ele;
		} catch (Exception e) {
			LogsManager.getLogger().error(e.getMessage());
			LogsManager.getLogger().error("No Element Found Named: " + elementName);

		}
		return null;
	}

	public static boolean click(WebElement element, String elementName, boolean swipeToElement, Duration timeout) {
		try {

			WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), timeout);
			if (swipeToElement) {
				swipeDownToElement(element, elementName);
			}
			element = wait.until(ExpectedConditions.elementToBeClickable(element));

			element.click();

			LogsManager.getLogger().info("Tapped on Element: " + elementName);

			return true;

		} catch (Exception e) {

			e.printStackTrace();
			return false;

		}
	}

	public static boolean sendKeys(WebElement element, String value, String elementName, boolean swipeToElement,
			boolean fieldClear, Duration timeout) {
		try {
			if (element != null) {

				WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), timeout);
				swipeDownToElement(element, elementName);
				if (fieldClear) {
					element.clear();
				}

				element.sendKeys(value);

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

	public static boolean swipeDownToElement(WebElement Element, String elementName) {
		try {
			Dimension size = DriverManager.getDriver().manage().window().getSize();
			int startX = size.getWidth() / 2;
			int startY = size.getHeight() / 2;
			int endX = startX;
			int endY = (int) (size.getHeight() * 0.25);
			PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
			Sequence seq = new Sequence(finger, 1)
					.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY))
					.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
					.addAction(new Pause(finger, Duration.ofMillis(200))).addAction(finger
							.createPointerMove(Duration.ofMillis(100), PointerInput.Origin.viewport(), endX, endY))
					.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

			((AppiumDriver) DriverManager.getDriver()).perform(Collections.singletonList(seq));

			LogsManager.getLogger().info("Swipe to " + elementName);
			return true;

		} catch (Exception e) {

			LogsManager.getLogger().error("Can not Swipe to " + elementName);

			return false;
		}
	}

	public static boolean isElementVisible(WebElement element, int timeOut) {

		WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeOut));
		WebElement ele = null;
		try {

			ele = wait.until(ExpectedConditions.visibilityOf(element));
			if (ele != null) {
				return true;
			} else {
				return false;
			}
		} catch (NoSuchElementException noSuchElementException) {
			return true;
		}
	}

	public static void swipeBack() {
		try {
			DriverManager.getDriver().navigate().back();
			LogsManager.getLogger().info("Swiped Back");
		} catch (Exception e) {
			LogsManager.getLogger().info("Not able to Swipe Back");
		}
	}

	public static boolean selectVisibleTextFromDropDown(WebElement webElement, String elementName, Object value,
			int timeOut) {
		try {
			isElementVisible(webElement, timeOut);
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

	/**
	 * 
	 * @author Ankur Huria
	 * @description- This method is used to convert Date From one format to another
	 */
	public static String convertDateFromOneFormatToAnother(String oldDateString, String oldDateFormat,
			String newDateFormat) {
		try {
			SimpleDateFormat format1 = new SimpleDateFormat(oldDateFormat);
			SimpleDateFormat format2 = new SimpleDateFormat(newDateFormat);
			Date date = format1.parse(oldDateString);
			return format2.format(date);
		} catch (Exception e) {
			e.getMessage();
			return null;
		}
	}

	public static int compareDates(String date1String, String date2String, String formatOfDate) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat(formatOfDate);

		Date date1 = dateFormat.parse(date1String);
		Date date2 = dateFormat.parse(date2String);

		return date1.compareTo(date2);

	}

	public static List<WebElement> FindElements(By by, String elementName) {

		for (int i = 0; i < 41; i++) {
			if (DriverManager.getDriver().findElements(by).isEmpty()) {
				try {
					Thread.sleep(250);
				} catch (InterruptedException e) {

				}

			} else {
				break;
			}
		}
		return DriverManager.getDriver().findElements(by);
	}

	public static void scrollDown(AndroidDriver driver) {
		TouchAction touchAction = new TouchAction(driver);
		int screenHeight = driver.manage().window().getSize().getHeight();
		int startX = driver.manage().window().getSize().getWidth() / 2;
		int startY = (int) (screenHeight * 0.8);
		int endY = (int) (screenHeight * 0.2);
		touchAction.press(PointOption.point(startX, startY)).moveTo(PointOption.point(startX, endY)).release()
				.perform();
	}

	public static WebElement findElementByContinuesScrolling(By by, String elementName) {
		boolean found = false;
		while (!found) {
			// Check if the element is present on the current page
			if (AndroidCommonUtils.FindElements(by, "Report Button").size() > 0) {
				// Element found
				LogsManager.getLogger().info("Element Found: " + elementName);

				found = true;
				return AndroidCommonUtils.FindElements(by, "Report Button").get(0);

			} else {
				// Scroll down to the next page
				AndroidCommonUtils.scrollDown(((AndroidDriver) DriverManager.getDriver()));
			}
		}

		return null;
	}
}
