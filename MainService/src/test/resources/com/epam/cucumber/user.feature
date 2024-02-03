Feature: User

  Scenario: Switch user activity
    Given the user with username "john.doe"
    When the client sends a PATCH request to activate user
    Then response status should be 200