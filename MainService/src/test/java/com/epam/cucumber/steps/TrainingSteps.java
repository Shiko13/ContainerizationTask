package com.epam.cucumber.steps;

import com.epam.cucumber.client.HttpClient;
import com.epam.model.dto.TrainingForTraineeDtoOutput;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TrainingSteps {

//    private TrainingTraineeFilter filter;

    @Autowired
    private HttpClient httpClient;
    List<TrainingForTraineeDtoOutput> trainings;

    @Given("^the trainer wants to search for trainings$")
    public void givenTrainerWantsToSearchForTrainings() {
//        filter = new TrainingTraineeFilter();
        System.out.println(3);
    }

    @When("the trainer filters by date range {myDate} to {myDate}, trainee's name {string}, trainer's name {string}, " +
            "and training type {string}")
    public void theTrainerFiltersByDateRangeAndTraineeName(LocalDate startDate, LocalDate endDate, String traineeName,
                                                           String trainerName, String trainingType) {

        trainings = httpClient.findByDateRangeAndTrainee(startDate, endDate, traineeName, trainerName, trainingType);
    }


    @Then("^the response should contain a list of trainings for the specified criteria$")
    public void thenResponseShouldContainListOfTrainings() {
        assertEquals(trainings.size(), 1);
    }
}
