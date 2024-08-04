package org.camunda.bpm.getstarted.springbootformioexample.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    // rest template (rest client to transfer data between endpoints)
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
