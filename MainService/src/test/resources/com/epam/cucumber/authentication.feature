Feature: Authentication

  @auth
  Scenario Outline: Login with different credentials
    Given the login endpoint is called with the username "<username>" and password "<password>"
    When the login endpoint is called with the username and password
    Then the response should contain next "<expectedResult>"

    Examples:
      | username | password         | expectedResult           |
      | john.doe | password1        | success                  |
      | john.doe | invalid_password | HttpClientErrorException |