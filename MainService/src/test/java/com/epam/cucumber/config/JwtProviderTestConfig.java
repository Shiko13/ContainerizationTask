package com.epam.cucumber.config;

import com.epam.util.JwtProvider;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.net.http.HttpClient;

@Configuration
public class JwtProviderTestConfig {

//    @Bean
//    @Primary
//    public JwtProvider jwtProvider() {
//        return Mockito.mock(JwtProvider.class);
//    }
//
//    @Bean
//    ServletWebServerFactory servletWebServerFactory() {
//        return new TomcatServletWebServerFactory();
//    }
}
