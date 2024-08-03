package org.camunda.bpm.getstarted.springbootformioexample.controller;

import ch.qos.logback.classic.Logger;
import jakarta.servlet.http.HttpSession;
import org.camunda.bpm.getstarted.springbootformioexample.service.FormService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

@RestController
@RequestMapping("/api/forms")
public class FormController {

    @Autowired
    private RestTemplate restTemplate;

    private static final Logger logger = (Logger) LoggerFactory.getLogger(FormController.class);

    private final FormService formService;
    private final ResourceLoader resourceLoader;

    public FormController(FormService formService, ResourceLoader resourceLoader) {
        this.formService = formService;
        this.resourceLoader = resourceLoader;
    }

    @GetMapping("/file")
    public ResponseEntity<Resource> file() {
        String RESOURCE = "classpath:templates/file.html";
        Resource resource = resourceLoader.getResource(RESOURCE);
        if (resource.exists()) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE,"text/html")
                    .body(resource);
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/rest/form")
    public ResponseEntity<String> restForm() throws IOException {
        String formDefinition = formService.getFormObject();

        // Log the form definition
        logger.info("Form Definition: {}", formDefinition);

        // Escape double quotes in JSON string to safely embed in HTML
        String escapedFormDefinition = formDefinition.replace("\"", "\\\"");

        Resource resource = resourceLoader.getResource("classpath:templates/form.html");
        String content = new String(Files.readAllBytes(Paths.get(resource.getURI())));

        // Inject the escaped form definition into the HTML template
        content = content.replace("/*[[${formDefinition}]]*/ '{}'", "\"" + escapedFormDefinition + "\"");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "text/html")
                .body(content);
    }



    @GetMapping("/form")
    public ResponseEntity<Resource> form() {
        String RESOURCE = "classpath:templates/formio.html";
        Resource resource = resourceLoader.getResource(RESOURCE);
        if (resource.exists()) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE,"text/html")
                    .body(resource);
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/form/complete")
    public ResponseEntity<Map<String, String>> completeForm(@RequestBody Map<String, String> formData, HttpSession session) {
        session.setAttribute("formData", formData);
        System.out.println(formData.get("name") + " " + formData.get("password"));
        return ResponseEntity.ok(Map.of("name",formData.get("name"),"password",formData.get("password")));
    }

    @GetMapping("/result")
    public ResponseEntity<Resource> result(@RequestParam("name") String name,
                                           @RequestParam("password") String password) {
        String RESOURCE = "classpath:templates/result.html";
        Resource resource = resourceLoader.getResource(RESOURCE);

        if (resource.exists()) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE,"text/html")
                    .body(resource);
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/user/form")
    public ResponseEntity<Resource> userForm() {
        String RESOURCE = "classpath:templates/userForm.html";
        Resource resource = resourceLoader.getResource(RESOURCE);
        if (resource.exists()) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE,"text/html")
                    .body(resource);
        }
        return ResponseEntity.notFound().build();
    }
    @PostMapping("/user/form/complete")
    public ResponseEntity<Map<String,Object>> completeUserForm(@RequestBody Map<String, Object> formData, HttpSession session) {
        String url = "http://localhost:8090/submit-form";

        // Create headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // Create the HttpEntity with form data and headers
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(formData, headers);

        // Make a POST request to the first project's /submit-form endpoint
        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(url, requestEntity, Map.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            Map<String, Object> updatedData = responseEntity.getBody();
            return ResponseEntity.ok(updatedData);
        } else {
            return ResponseEntity.status(responseEntity.getStatusCode()).body(null);
        }
    }


    @GetMapping("/user/display")
    public ResponseEntity<Resource> displayUser(@RequestParam String userName, @RequestParam Integer userAge, @RequestParam String userJob) {

        String RESOURCE = "classpath:templates/displayUser.html";
        Resource resource = resourceLoader.getResource(RESOURCE);

        if (resource.exists()) {
            System.out.println(userName);
            // Replace placeholders in the HTML template with actual data
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "text/html")
                    .body(resource);
        }
        return ResponseEntity.notFound().build();
    }

}
