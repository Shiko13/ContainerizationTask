package com.epam.cucumber.steps;

import com.epam.cucumber.client.HttpClient;
import com.epam.model.dto.TrainerDtoInput;
import com.epam.model.dto.TrainerForTraineeDtoOutput;
import com.epam.model.dto.UserActivateDtoInput;
import com.epam.service.TrainerService;
import com.epam.service.UserService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import static org.junit.Assert.assertEquals;

public class TrainerSteps {

    @Autowired
    private TrainerService trainerService;

    @Autowired
    private UserService userService;

    @Autowired
    private HttpClient httpClient;
    private List<TrainerForTraineeDtoOutput> storedTrainersWithEmptyTrainees;
    private int count;

    @Given("^(\\d+) trainers with no assigned trainees$")
    public void givenTrainersWithNoAssignedTrainees(int trainerCount) {
        count = trainerCount;

        for (int i = 0; i < trainerCount; i++) {
            TrainerDtoInput trainerDtoInput = TrainerDtoInput.builder()
                                                             .firstName("Trainer" + i)
                                                             .lastName("Lastname" + i)
                                                             .specialization(1L)
                                                             .build();
            trainerService.save(trainerDtoInput);
            userService.switchActivate("trainer" + i + "." + "lastname" + i, new UserActivateDtoInput(true));
        }
    }

    @When("^the client requests the list of trainers with empty trainees$")
    public void whenClientRequestsListWithEmptyTrainees() {
        storedTrainersWithEmptyTrainees = httpClient.getTrainersWithEmptyTrainees();
    }

    @Then("^the response should contain a list of trainers with empty trainees$")
    public void thenResponseShouldContainListOfTrainersWithEmptyTrainees() {
        assertEquals(storedTrainersWithEmptyTrainees.size(), count);
    }
}
