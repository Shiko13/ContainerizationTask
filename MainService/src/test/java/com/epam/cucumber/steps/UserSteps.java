package com.epam.cucumber.steps;

import com.epam.cucumber.client.HttpClient;
import com.epam.model.dto.UserActivateDtoInput;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;

public class UserSteps {

    @Autowired
    private HttpClient httpClient;
    private String username;
    private ResponseEntity<Void> response;

    @Given("the user with username {string}")
    public void givenUserServiceIsRunning(String username) {
        this.username = username;
    }

    @When("the client sends a PATCH request to activate user")
    public void whenClientSendsPatchRequestToActivateUser() {
        UserActivateDtoInput activationInput = new UserActivateDtoInput(false);
        response = httpClient.switchActivate(username, activationInput);
    }

    @Then("response status should be 200")
    public void thenStatusShouldBeChanged() {
        assertEquals(response.getStatusCode().value(), 200);
    }
}
