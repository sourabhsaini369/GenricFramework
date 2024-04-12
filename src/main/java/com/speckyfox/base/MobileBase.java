package com.speckyfox.base;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.util.Objects;

import org.openqa.selenium.WebDriver;
import org.testng.*;
import org.testng.annotations.*;

import com.speckyfox.constants.ConfigConstants;
import com.speckyfox.constants.PathConstants;
import com.speckyfox.drivermanager.AndroidManager;
import com.speckyfox.drivermanager.DriverManager;
import com.speckyfox.listners.AppListeners;
import com.speckyfox.videoConfig.VideoConfig;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.cucumber.java.After;
import io.cucumber.java.Before;

public class MobileBase {
	static AppiumDriverLocalService server;
	Process emulatorProcess;
	ProcessBuilder emulatorProcessBuilder;
	boolean videoDeleteFlag = false;

	@BeforeMethod
	public void MobileSetup(ITestResult result)
			throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
		setup(result);
		DriverManager.getDriver();
	}

	@Before("@MobileHook")
	public void MobileSetup()
			throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
		setup();
		DriverManager.getDriver();
	}

	@After("@MobileHook")
	@AfterMethod
	public void mobileTearDown()
			throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

		if (!Objects.isNull(DriverManager.getDriver())) {

			DriverManager.getDriver().quit();
			DriverManager.setDriver(null);

		}

		VideoConfig.getInstance().videoRecordingEnd();
	}

	@AfterSuite
	public void serverClose() {
		if (server != null) {
			server.stop();
			System.out.println("#############Server Closed#############");
		}

		if (emulatorProcessBuilder != null) {
			try {
				// Command to stop the emulator
				String stopEmulatorCommand = "adb emu kill";

				// Stop the emulator using ProcessBuilder
				ProcessBuilder stopEmulatorProcessBuilder = new ProcessBuilder(stopEmulatorCommand.split(" "));
				Process stopEmulatorProcess = stopEmulatorProcessBuilder.start();

				// Wait for the process to complete
				stopEmulatorProcess.waitFor();

				while (true) {
					if (!server.isRunning()) {
						break;
					}

				}
				System.out.println("#############Emulator has been shut down.#############");

			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

	private static boolean isEmulatorBooted() throws IOException {
		ProcessBuilder processBuilder = new ProcessBuilder("adb", "shell", "getprop", "sys.boot_completed");
		Process process = processBuilder.start();
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line = reader.readLine();
		return line != null && line.equals("1");
	}

	private static boolean isAppiumServerStarted() {
		if (server != null) {
			return server.isRunning();
		}
		return false;

	}

	private void setup(ITestResult result) {
		if (!isAppiumServerStarted()) {

			try {
				// Command to start the emulator
				String emulatorCommand = "emulator -avd " + ConfigConstants.emulatorID;

				// Start the emulator using ProcessBuilder
				emulatorProcessBuilder = new ProcessBuilder(emulatorCommand.split(" "));
				emulatorProcess = emulatorProcessBuilder.start();

				// Check the emulator status dynamically
				boolean emulatorBooted = false;
				while (!emulatorBooted) {
					emulatorBooted = isEmulatorBooted();
					if (!emulatorBooted) {
						Thread.sleep(1000); // Wait for 2 seconds before checking again
					}
				}

				AppiumServiceBuilder builder = new AppiumServiceBuilder();

				String homePath = System.getProperty("user.home");
				builder.withAppiumJS(new File(homePath + PathConstants.nodeJSMainFilePath))
						.withTimeout(Duration.ofSeconds(100));

				server = AppiumDriverLocalService.buildService(builder);
				server.start();

			} catch (Exception e) {
				
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}

		if (Objects.isNull(DriverManager.getDriver())) {

			try {
				VideoConfig.getInstance().videoRecordingStart(AppListeners.getMethodOrScenarioName(result), true);
				WebDriver driver = AndroidManager.getDriver();
				DriverManager.setDriver(driver);

			} catch (Exception e) {

				System.out.println(e.getMessage());
				e.printStackTrace();
			}

		}
	}

	private void setup() {
		if (!isAppiumServerStarted()) {

			try {
				// Command to start the emulator
				String emulatorCommand = "emulator -avd " + ConfigConstants.emulatorID;

				// Start the emulator using ProcessBuilder
				emulatorProcessBuilder = new ProcessBuilder(emulatorCommand.split(" "));
				emulatorProcess = emulatorProcessBuilder.start();

				// Check the emulator status dynamically
				boolean emulatorBooted = false;
				while (!emulatorBooted) {
					emulatorBooted = isEmulatorBooted();
					if (!emulatorBooted) {
						Thread.sleep(1000); // Wait for 2 seconds before checking again
					}
				}

				AppiumServiceBuilder builder = new AppiumServiceBuilder();

				String homePath = System.getProperty("user.home");
				builder.withAppiumJS(new File(homePath + PathConstants.nodeJSMainFilePath))
						.withTimeout(Duration.ofSeconds(100));

				server = AppiumDriverLocalService.buildService(builder);
				server.start();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}

		if (Objects.isNull(DriverManager.getDriver())) {

			try {
				VideoConfig.getInstance().videoRecordingStart(AppListeners.currentlyExecutingTC, true);
				WebDriver driver = AndroidManager.getDriver();
				DriverManager.setDriver(driver);

			} catch (Exception e) {

				System.out.println(e.getMessage());
				e.printStackTrace();
			}

		}
	}

}
