package com.epam.cucumber.steps;

import com.epam.cucumber.client.HttpClient;
import com.epam.model.dto.AuthResponse;
import com.epam.service.AuthenticationService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AuthenticationSteps {

    @Autowired
    private HttpClient httpClient;
    @Autowired
    private AuthenticationService authenticationService;
    private String username;
    private String password;
    private AuthResponse authResponse;
    private HttpClientErrorException clientException;

    @Given("the login endpoint is called with the username {string} and password {string}")
    public void givenUserCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @When("the login endpoint is called with the username and password")
    public void whenUserLogsIn() {
        try {
            authResponse = httpClient.login(username, password);
        } catch (HttpClientErrorException e) {
            clientException = e;
        }
    }

    @Then("the response should contain next {string}")
    public void thenResponseShouldContainValidToken(String expectedResult) {
        switch (expectedResult) {
            case "success":
                assertNotNull("Auth response should not be null", authResponse);
                assertNotNull("Bearer token should not be null", authResponse.getToken());
                break;
            case "HttpClientErrorException":
                assertNotNull("Exception should not be null", clientException);
                assertEquals(401, clientException.getRawStatusCode());
        }
    }
}
