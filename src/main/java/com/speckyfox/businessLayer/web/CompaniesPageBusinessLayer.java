package com.speckyfox.businessLayer.web;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.speckyfox.drivermanager.DriverManager;
import com.speckyfox.pageObjects.webPages.CompaniesPage;
import com.speckyfox.utilities.commonUtilityFunctions.CommonUtils;

public class CompaniesPageBusinessLayer extends CompaniesPage {

	public CompaniesPageBusinessLayer(WebDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}

	private void enterLocation(String location, int timeout) {
		CommonUtils.sendKeys(DriverManager.getDriver(), getLocationInput(timeout), location, "Location: " + location,
				Duration.ofSeconds(timeout));
	}

	private void selectFromSuggetion(String entity, int timeout) throws InterruptedException {
		CommonUtils.click(DriverManager.getDriver(), getInputSuggestion(entity, timeout), "Entity: " + entity,
				Duration.ofSeconds(timeout));
	}

	private void enterIndustries(String industry, int timeout) {
		CommonUtils.sendKeys(DriverManager.getDriver(), getIndustriesInput(timeout), industry, "Industry: " + industry,
				Duration.ofSeconds(timeout));
	}

	private void enterJobTitle(String jobTitle, int timeout) {
		CommonUtils.sendKeys(DriverManager.getDriver(), getJobTitleInput(timeout), jobTitle, "Job Title: " + jobTitle,
				Duration.ofSeconds(timeout));
	}

	private void jobFunctionExpandClick(int timeout) {
		CommonUtils.click(DriverManager.getDriver(), getJobFunctionsInputButton(timeout), "Job Function Button",
				Duration.ofSeconds(timeout));
	}

	private void jobFunctionCheckboxesSelection(List<String> jobFunctionsToSelect, int timeout) {

		List<String> getJobFunctionsNamesText = getJobFunctionsNames(10).stream().map(x -> x.getText())
				.collect(Collectors.toList());
		List<WebElement> getJobFunctionsCheckBoxesElements = getJobFunctionsCheckBoxes(10);

		if (getJobFunctionsNamesText.size() != 0 && getJobFunctionsCheckBoxesElements.size() != 0
				&& (getJobFunctionsCheckBoxesElements.size() == getJobFunctionsNamesText.size())) {

			for (String jobFunctionToSelect : jobFunctionsToSelect) {

				try {

					if (getJobFunctionsNamesText.contains(jobFunctionToSelect)) {

						Integer index = getJobFunctionsNamesText.indexOf(jobFunctionToSelect);

						CommonUtils.click(DriverManager.getDriver(), getJobFunctionsCheckBoxesElements.get(index),
								"Job Function: " + jobFunctionToSelect, Duration.ofSeconds(timeout));

					}

				} catch (Exception e) {
					System.out
							.println("No Job Function named Found: " + jobFunctionToSelect + ", So Not able to select");

				}

			}

		} else {
			System.out.println("No Job Function names Found or Xpath is not correct");
		}

	}

	private void companyRatingDropDownClick(int timeout) {
		CommonUtils.click(DriverManager.getDriver(), employerRatingDropDownButton(timeout),
				"Company Rating Dropdown Button", Duration.ofSeconds(timeout));
	}

	private void companyRatingDropDownSuggestionSelect(String rating, int timeout) {
		CommonUtils.click(DriverManager.getDriver(), getRatingSelectFromDropDown(rating, timeout),
				"Company Rating Dropdown Suggetion Select: " + rating, Duration.ofSeconds(timeout));
	}

	private void companySizeSelect(String companySize, int timeout) {
		CommonUtils.click(DriverManager.getDriver(), getCompanySize(companySize, timeout),
				"Company Size Select: " + companySize, Duration.ofSeconds(timeout));
	}

	public void companyFilterHandler(String location, String industry, String jobTitle, List<String> jobFunctions,
			String companyRating, String companySize, int timeout) {

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (location != null && !"".equals(location)) {
			enterLocation(location, timeout);
			try {
				selectFromSuggetion(location, timeout);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (industry != null && !"".equals(industry)) {
			enterIndustries(industry, timeout);
			try {
				selectFromSuggetion(industry, timeout);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (jobTitle != null && !"".equals(jobTitle)) {
			enterJobTitle(jobTitle, timeout);
			try {
				selectFromSuggetion(jobTitle, timeout);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (jobFunctions != null) {
			if (jobFunctions.size() != 0) {
				jobFunctionExpandClick(timeout);
				jobFunctionCheckboxesSelection(jobFunctions, timeout);
			}
		}

		if (companyRating != null && !"".equals(companyRating)) {
			companyRatingDropDownClick(timeout);
			companyRatingDropDownSuggestionSelect(companyRating, timeout);
		}

		if (companySize != null && !"".equals(companySize)) {

			companySizeSelect(companySize, timeout);
		}

	}

	public List<String[]> cardHandlerAndFetchDetails(Integer paginationSize, int timeOut) {
		List<WebElement> companyCards = getCompanyCards(timeOut);
		List<String[]> cardDetails = new ArrayList<String[]>();

		if (companyCards.size() > 0) {

			if (paginationSize > 0) {
				for (Integer i = 1; i <= paginationSize; i++) {

					List<WebElement> paginationLinks = getPaginationLinks(timeOut);
					List<String> paginationLinksText = paginationLinks.stream().map(x -> x.getText())
							.collect(Collectors.toList());
					if (paginationLinksText.contains(String.valueOf(i))) {
						Integer index = paginationLinksText.indexOf(String.valueOf(i));

						if (CommonUtils.click(driver, paginationLinks.get(index), String.valueOf(i),
								Duration.ofSeconds(timeOut))) {

							try {
								Thread.sleep(4000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							companyCards = getCompanyCards(timeOut);

							System.out.println(
									"------Total Cards: " + companyCards.size() + " on Page: " + i + "-------");
							for (WebElement companyCard : companyCards) {

								if (CommonUtils.click(driver, companyCard, "Company Card",
										Duration.ofSeconds(timeOut))) {

									String parentWindowID = CommonUtils.switchToWindowOpenNextToParentWindow(driver);
									if (parentWindowID != null) {
										System.out.println("Switched to new window");
										if (getNotHumanCheckboxIframe(4) != null) {

											if (CommonUtils.switchToFrame(driver, Duration.ofSeconds(timeOut),
													getNotHumanCheckboxIframe(4))) {
												CommonUtils.click(driver, getNotHumanCheckbox(5), "Not a Human",
														Duration.ofSeconds(timeOut));
												System.out.println("Clicked on Not a Human Checkbox");
												try {
													Thread.sleep(3000);
												} catch (InterruptedException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}

											} else {

												System.out.println("Not Able to Switch to Frame");

											}

										}
										WebElement employerHeading = getEmployerHeading(timeOut);

										String[] cardDetailArray = new String[3];

										if (employerHeading != null) {
											WebElement employerWebsite = getEmployerWebsite(timeOut);
											WebElement employerLocation = getEmployerLocation(timeOut);

											String employerHeadingText = employerHeading.getText();

											String employerWebsiteText = employerWebsite.getText();
											String employerLocationText = employerLocation.getText();

											cardDetailArray[0] = employerHeadingText;
											cardDetailArray[1] = employerWebsiteText;
											cardDetailArray[2] = employerLocationText;
											cardDetails.add(cardDetailArray);
											System.out.println("------Detail of Card: "
													+ Arrays.toString(cardDetailArray) + "------");

										} else {
											cardDetailArray[0] = "";
											cardDetailArray[1] = "";
											cardDetailArray[2] = "";
											cardDetails.add(cardDetailArray);
											System.out.println(
													"Not able to fetch Details, Either Page not Open Properly or xpath issue");

										}

										driver.close();
										driver.switchTo().window(parentWindowID);

									} else {
										System.out.println("Not Able to Switch to New Window after click on Card, ");
									}
								} else {

								}
							}

						}

						else {

							System.out.println("Not Able to Click on Pagination: " + i);
							return cardDetails;

						}

					}

					else {

						System.out.println("---------Pages Available till " + (i - 1) + "----------");
						return cardDetails;

					}

				}

				return cardDetails;

			} else {

				System.out.println("Provided Pagination Size should be Greater than 0");
				return new ArrayList<String[]>();

			}

		} else {

			System.out.println("Either No Result Found or Locator has been changed");
			return new ArrayList<String[]>();

		}
	}

}
