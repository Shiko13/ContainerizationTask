package com.epam.cucumber.steps;

import com.epam.cucumber.client.HttpClient;
import com.epam.model.dto.TrainingTypeOutputDto;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class TrainingTypesSteps {

    @Autowired
    private HttpClient httpClient;

    private List<String> expectedTrainingTypes;
    private List<TrainingTypeOutputDto> trainingTypes;

    @Given("the training types service is running")
    public void givenTrainingTypesServiceIsRunning() {
        expectedTrainingTypes =
                List.of("Strength Training", "Cardiovascular Exercise", "Yoga", "CrossFit", "Pilates", "Martial Arts",
                        "Zumba", "Spinning", "Swimming", "Plyometrics");
    }

    @When("the client requests to get all training types")
    public void whenClientRequestsToGetAllTrainingTypes() {
        trainingTypes = httpClient.getAllTrainingTypes();
    }

    @Then("the response should contain a list of training types")
    public void thenResponseShouldContainListOfTrainingTypes() {
        List<String> actualTypes = trainingTypes.stream().map(TrainingTypeOutputDto::getName).toList();
        assertEquals(actualTypes, expectedTrainingTypes);
    }
}
