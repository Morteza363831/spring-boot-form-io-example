package org.camunda.bpm.getstarted.springbootformioexample.service;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface SendRequestService {

    ResponseEntity<Map> sendRequest(String uri,Map<String,Object> parameters);
}
