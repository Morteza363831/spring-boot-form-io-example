package org.camunda.bpm.getstarted.springbootformioexample.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FormService {

    private final RestTemplate restTemplate;
    private final String FORM_URL = "https://ryotllgbrhejmhd.form.io/myform";

    public FormService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getFormObject() {
        return restTemplate.getForObject(FORM_URL, String.class);
    }
}
