Feature: Trainee

  Scenario Outline: Retrieve trainee profile with different usernames
    Given a valid trainee with the username "<username>"
    When the getProfile endpoint is called with the username
    Then the response should contain "<expectedResult>"

    Examples:
      | username         | expectedResult           |
      | john.doe         | success                  |
      | invalid_username | HttpServerErrorException |
      | nonexistent.user | HttpClientErrorException |

  Scenario: Trainee submits valid registration details
    Given a trainee with the following registration details
      | firstName   | John            |
      | lastName    | Doherty         |
      | dateOfBirth | 1990-01-01      |
      | address     | 123 Main Street |
    When the trainee submits the registration request
    Then the response should contain the trainee registration details
      | username | john.doherty |