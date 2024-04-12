@MobileHook
Feature: Calculator


@regressionMobile @smokeMobile
  Scenario: Add two numbers in Mobile
    Given I have a calculator
    When I add 5 and 7
    Then the result should be 12

@smokeMobile @regressionMobile
  Scenario: Subtract two numbers in Mobile
    Given I have a calculator
    When I subtract 8 from 15
    Then the result should be 7
