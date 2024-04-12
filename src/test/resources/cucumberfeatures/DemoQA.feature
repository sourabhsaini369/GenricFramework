@WebHook
Feature: Verify demo functionality

@DemoWeb
  Scenario: Verify data on Web tables
    Given I am on registration form
    When I filled the registration form with valid details
    Then I should see new records in the Web table