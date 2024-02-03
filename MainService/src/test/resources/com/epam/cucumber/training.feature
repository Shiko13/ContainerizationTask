Feature: Training

  Scenario Outline: Trainer searches for trainings by date range and trainee's name
    Given the trainer wants to search for trainings
    When the trainer filters by date range <startDate> to <endDate>, trainee's name "<traineeName>", trainer's name "<trainerName>", and training type "<trainingType>"
    Then the response should contain a list of trainings for the specified criteria

    Examples:
      | startDate  | endDate    | traineeName   | trainerName       | trainingType |
      | 2021-01-01 | 2024-04-01 | alice.johnson | charlotte.martin  | Yoga         |
      | 2021-01-01 | 2024-04-01 | bob.brown     | benjamin.thompson | CrossFit     |
