package com.speckyfox.cucumberStepDefinitions;


import static org.testng.Assert.*;

import com.speckyfox.drivermanager.DriverManager;
import com.speckyfox.logmanager.LogsManager;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CalculatorsSteps {

	private int result;

	@Given("I have a calculator")
	public void iHaveACalculator() {
		DriverManager.getDriver();

		LogsManager.getLogger().info("Go To Google Website");
//		ReportManager.getReportType().getTestLogger().info("Go to Google in Cucumber");
	}

	@When("I add {int} and {int}")
	public void iAdd(int num1, int num2) {
		result = this.add(num1, num2);
		LogsManager.getLogger().info("Adding.......");
	}

	@When("I subtract {int} from {int}")
	public void iSubtract(int num2, int num1) {
		result = this.subtract(num1, num2);
		LogsManager.getLogger().info("Subtracting..........");
	}

	@Then("the result should be {int}")
	public void theResultShouldBe(int expected) {
		assertEquals(expected, result);
	}

	public int add(int num1, int num2) {
		return num1 + num2;
	}

	public int subtract(int num1, int num2) {
		return num1 - num2;
	}
}
