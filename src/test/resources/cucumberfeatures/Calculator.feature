@WebHook
Feature: Calculator


@regressionWeb
  Scenario: Add two numbers in Web
    Given I have a calculator
    When I add 5 and 7
    Then the result should be 12

@smokeWeb
  Scenario: Subtract two numbers in Web
    Given I have a calculator
    When I subtract 8 from 15
    Then the result should be 7
    

