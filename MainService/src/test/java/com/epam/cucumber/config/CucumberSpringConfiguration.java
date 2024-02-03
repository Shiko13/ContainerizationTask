package com.epam.cucumber.config;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
//        ,classes = {JwtProviderTestConfig.class, TraineeHttpClient.class}
)
public class CucumberSpringConfiguration {
}
