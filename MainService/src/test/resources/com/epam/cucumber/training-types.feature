Feature: Training Types

  Scenario: Retrieve all training types
    Given the training types service is running
    When the client requests to get all training types
    Then the response should contain a list of training types