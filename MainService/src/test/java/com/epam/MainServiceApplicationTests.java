package com.epam;

import com.epam.cucumber.config.JwtProviderTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = JwtProviderTestConfig.class)
//@ContextConfiguration(classes = JwtProviderTestConfig.class)
class MainServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
