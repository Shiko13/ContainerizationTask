package com.epam.cucumber.steps;

import com.epam.cucumber.client.HttpClient;
import com.epam.model.dto.TraineeDtoInput;
import com.epam.model.dto.TraineeDtoOutput;
import com.epam.model.dto.TraineeSaveDtoOutput;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

//@Testcontainers
//@DirtiesContext
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TraineeSteps {

    @Autowired
    HttpClient httpClient;

    //    @Autowired
//    private JwtProvider jwtProvider;
    private String username;
    private TraineeDtoOutput traineeProfile;
    private HttpServerErrorException serverException;
    private HttpClientErrorException clientException;
    private TraineeDtoInput traineeDtoInput;
    private TraineeSaveDtoOutput traineeSaveDtoOutput;


//    @Container
//    private static final PostgreSQLContainer<?> POSTGRE_SQL_CONTAINER =
//            new PostgreSQLContainer<>("postgres:14.8-alpine3.18");
//
//
//    @BeforeAll
//    public static void before_or_after_all() {
//        POSTGRE_SQL_CONTAINER.start();
//    }

    @Given("a valid trainee with the username {string}")
    public void givenValidTrainee(String username) {
        this.username = username;
    }

    @When("the getProfile endpoint is called with the username")
    public void whenGetProfileCalled() {
        try {
            traineeProfile = httpClient.getProfile(username);
        } catch (HttpServerErrorException e) {
            serverException = e;
        } catch (HttpClientErrorException e) {
            clientException = e;
        }
    }

    @Then("the response should contain {string}")
    public void thenResponseShouldContain(String expectedResult) {
        switch (expectedResult) {
            case "success":
                assertNotNull("Trainee profile should not be null", traineeProfile);
                assertEquals("John", traineeProfile.getFirstName());
                assertEquals("Doe", traineeProfile.getLastName());
                break;
            case "HttpServerErrorException":
                assertNotNull("Exception should not be null", serverException);
                assertEquals(500, serverException.getRawStatusCode());
                break;
            case "HttpClientErrorException":
                assertNotNull("Exception should not be null", clientException);
                assertEquals(401, clientException.getRawStatusCode());
                break;
        }
    }

    @Given("a trainee with the following registration details")
    public void givenTraineeWithRegistrationDetails(Map<String, String> registrationDetails) {
        traineeDtoInput = new TraineeDtoInput();
        traineeDtoInput.setFirstName(registrationDetails.get("firstName"));
        traineeDtoInput.setLastName(registrationDetails.get("lastName"));
        traineeDtoInput.setDateOfBirth(LocalDate.parse(registrationDetails.get("dateOfBirth")));
        traineeDtoInput.setAddress(registrationDetails.get("address"));
    }

    @When("the trainee submits the registration request")
    public void whenTraineeSubmitsRegistrationRequest() {
//        when(jwtProvider.validateClaims(any())).thenReturn(true);
        try {
            traineeSaveDtoOutput = httpClient.registration(traineeDtoInput);
        } catch (HttpClientErrorException e) {
            clientException = e;
        }
    }

    @Then("the response should contain the trainee registration details")
    public void thenResponseShouldContainTraineeRegistrationDetails(Map<String, String> expectedDetails) {
        assertEquals(expectedDetails.get("username"), traineeSaveDtoOutput.getUsername());
        assertNotNull(traineeSaveDtoOutput.getPassword());
    }
}
