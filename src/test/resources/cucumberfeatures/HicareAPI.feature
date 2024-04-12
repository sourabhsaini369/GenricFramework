@ApiHook
Feature: HiCare API Testing

@APITest
 Scenario: Verify Login functionality
   Given The API base URL
   When I send a post request
   Then The status code should be 200
   And The message should be Login Successful

