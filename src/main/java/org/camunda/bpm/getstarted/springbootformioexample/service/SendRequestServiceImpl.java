package org.camunda.bpm.getstarted.springbootformioexample.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class SendRequestServiceImpl implements SendRequestService{

    private final RestTemplate restTemplate;

    public SendRequestServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    @Override
    public ResponseEntity<Map> sendRequest(String uri, Map<String, Object> parameters) {
        // Create headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // Create the HttpEntity with form data and headers
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(parameters, headers);
        // Make a POST request to the first project's /submit-form endpoint
        return restTemplate.postForEntity(uri, requestEntity, Map.class);
    }
}
