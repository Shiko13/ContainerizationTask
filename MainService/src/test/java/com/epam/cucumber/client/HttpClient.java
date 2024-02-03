package com.epam.cucumber.client;

import com.epam.model.dto.AuthResponse;
import com.epam.model.dto.TraineeDtoInput;
import com.epam.model.dto.TraineeDtoOutput;
import com.epam.model.dto.TraineeSaveDtoOutput;
import com.epam.model.dto.TrainerForTraineeDtoOutput;
import com.epam.model.dto.TrainingForTraineeDtoOutput;
import com.epam.model.dto.TrainingTypeOutputDto;
import com.epam.model.dto.UserActivateDtoInput;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Scope;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

@Component
@NoArgsConstructor
@Scope(SCOPE_CUCUMBER_GLUE)
public class HttpClient {

    @Autowired
    private RestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Value("${security.token}")
    private String token;

    private final String SERVER_URL = "http://localhost";
    private final String ENDPOINT_TRAINEE = "/trainee";
    private final String ENDPOINT_TRAINER = "/trainer";
    private final String ENDPOINT_TRAINING = "/training";
    private final String ENDPOINT_TRAINING_TYPES = "/training-types";
    private final String ENDPOINT_LOGIN = "/login";
    private final String ENDPOINT_USER = "/user";

    public TraineeDtoOutput getProfile(String username) {
        String url = buildUrl(ENDPOINT_TRAINEE + "/username?username=" + username);
        HttpHeaders headers = buildHeaders();
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);

        return executeRequest(url, HttpMethod.GET, httpEntity, TraineeDtoOutput.class);
    }

    public TraineeSaveDtoOutput registration(TraineeDtoInput traineeDtoInput) {
        String url = buildUrl(ENDPOINT_TRAINEE);
        HttpHeaders headers = buildHeaders();
        HttpEntity<Object> httpEntity = new HttpEntity<>(traineeDtoInput, headers);

        return executeRequest(url, HttpMethod.POST, httpEntity, TraineeSaveDtoOutput.class);
    }

    public ResponseEntity<Void> switchActivate(String username, UserActivateDtoInput userInput) {
        String url = buildUrl(ENDPOINT_USER + "/activate?username=" + username);
        HttpHeaders headers = buildHeaders();
        HttpEntity<UserActivateDtoInput> httpEntity = new HttpEntity<>(userInput, headers);

        return restTemplate.exchange(url, HttpMethod.PUT, httpEntity, Void.class);
        //TODO: method patch doesn't work
    }

    public AuthResponse login(String username, String password) {
        String url = buildUrl(ENDPOINT_LOGIN + "?username={username}&password={password}");
        HttpHeaders headers = buildHeaders();
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);

        return executeRequest(url, HttpMethod.POST, httpEntity, AuthResponse.class, username, password);
    }

    public List<TrainingForTraineeDtoOutput> findByDateRangeAndTrainee(LocalDate periodFrom, LocalDate periodTo,
                                                                       String username, String trainerName,
                                                                       String trainingType) {
        String url = buildUrl(ENDPOINT_TRAINING + "/criteria-trainee");

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                                                           .queryParam("periodFrom", periodFrom)
                                                           .queryParam("periodTo", periodTo)
                                                           .queryParam("username", username)
                                                           .queryParam("trainerName", trainerName)
                                                           .queryParam("trainingType", trainingType);

        HttpHeaders headers = buildHeaders();
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);

        String entireUrl = builder.toUriString();

        ResponseEntity<List<TrainingForTraineeDtoOutput>> responseEntity =
                restTemplate.exchange(entireUrl, HttpMethod.GET, httpEntity, new ParameterizedTypeReference<>() {
                }, username);

        return responseEntity.getBody();
    }

    public List<TrainingTypeOutputDto> getAllTrainingTypes() {
        String url = buildUrl(ENDPOINT_TRAINING_TYPES);

        HttpHeaders headers = buildHeaders();
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<List<TrainingTypeOutputDto>> responseEntity =
                restTemplate.exchange(url, HttpMethod.GET, httpEntity, new ParameterizedTypeReference<>() {
                });

        return responseEntity.getBody();
    }

    public List<TrainerForTraineeDtoOutput> getTrainersWithEmptyTrainees() {
        String url = buildUrl(ENDPOINT_TRAINER + "/free");
        HttpHeaders headers = buildHeaders();
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<List<TrainerForTraineeDtoOutput>> responseEntity =
                restTemplate.exchange(url, HttpMethod.GET, httpEntity, new ParameterizedTypeReference<>() {
                });

        return responseEntity.getBody();
    }

    private String buildUrl(String endpoint) {
        return SERVER_URL + ":" + port + endpoint;
    }

    private HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        return headers;
    }

    private <T> T executeRequest(String url, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType,
                                 Object... uriVariables) {
        ResponseEntity<T> response = restTemplate.exchange(url, method, requestEntity, responseType, uriVariables);
        return response.getBody();
    }
}


