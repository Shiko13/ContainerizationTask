Feature: Trainer Assignment

  Scenario Outline: Get not assigned trainers with empty trainees
    Given <trainerCount> trainers with no assigned trainees
    When the client requests the list of trainers with empty trainees
    Then the response should contain a list of trainers with empty trainees

    Examples:
      | trainerCount |
      | 0            |
      | 5            |