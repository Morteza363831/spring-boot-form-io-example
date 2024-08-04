package org.camunda.bpm.getstarted.springbootformioexample.controller;

import ch.qos.logback.classic.Logger;
import jakarta.servlet.http.HttpSession;
import org.camunda.bpm.getstarted.springbootformioexample.service.FormService;
import org.camunda.bpm.getstarted.springbootformioexample.service.SendRequestService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

@RestController
@RequestMapping("/api/forms")
public class FormController {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(FormController.class);


    private final RestTemplate restTemplate;
    private final SendRequestService sendRequestService;
    private final ResourceLoader resourceLoader;

    public FormController(RestTemplate restTemplate,
                          SendRequestService sendRequestService,
                          ResourceLoader resourceLoader) {
        this.restTemplate = restTemplate;
        this.sendRequestService = sendRequestService;
        this.resourceLoader = resourceLoader;
    }


    /*// display front file for fluid simulation ...
    // there is no relation between this and project
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

    // this endpoint get json file from form io then render it and display in html file
    @GetMapping("/rest/form")
    public ResponseEntity<String> restForm() throws IOException {
        // get json base of form from uri (form io)
        String formDefinition = formService.getFormObject();

        // Log the form definition
        logger.info("Form Definition: {}", formDefinition);

        // Escape double quotes in JSON string to safely embed in HTML
        String escapedFormDefinition = formDefinition.replace("\"", "\\\"");

        // find html resource
        Resource resource = resourceLoader.getResource("classpath:templates/form.html");

        // select content of html file
        String content = new String(Files.readAllBytes(Paths.get(resource.getURI())));

        // Inject the escaped form definition into the HTML template
        content = content.replace("[[${formDefinition}]] '{}'", "\"" + escapedFormDefinition + "\"");

        // return response object with headers and html file and the updated content
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "text/html")
                .body(content);
    }


    // this form is like upper ! but
    // here we have static json form ! we did not get the form from uri !
    @GetMapping("/form")
    public ResponseEntity<Resource> form() {
        // get resource
        String RESOURCE = "classpath:templates/formio.html";
        Resource resource = resourceLoader.getResource(RESOURCE);
        // check the resource is correct
        if (resource.exists()) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE,"text/html")
                    .body(resource);
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    // submit form using post endpoint
    @PostMapping("/form/complete")
    public ResponseEntity<Map<String, String>> completeForm(@RequestBody Map<String, String> formData, HttpSession session) {
        // return response object with status a map body
        return ResponseEntity.ok(Map.of("name",formData.get("name"),"password",formData.get("password")));
    }

    @GetMapping("/result")
    public ResponseEntity<Resource> result(@RequestParam("name") String name,
                                           @RequestParam("password") String password) {
        // find resource
        String RESOURCE = "classpath:templates/result.html";
        Resource resource = resourceLoader.getResource(RESOURCE);

        // check it
        if (resource.exists()) {
            // return response object
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE,"text/html")
                    .body(resource);
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    */


    // display user form to user
    @GetMapping("/user/form")
    public ResponseEntity<Resource> userForm() {
        // find resource
        String RESOURCE = "classpath:templates/userForm.html";
        Resource resource = resourceLoader.getResource(RESOURCE);
        //  check the resource is correct or no
        if (resource.exists()) {
            // return response object
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE,"text/html")
                    .body(resource);
        }
        return ResponseEntity.notFound().build();
    }
    // retrieve data after submitting and make a process
    @PostMapping("/user/form/complete")
    public ResponseEntity<Map<String,Object>> completeUserForm(@RequestBody Map<String, Object> formData, HttpSession session) {

        String destinationUrl = "http://localhost:8090/submit-form";
        ResponseEntity<Map> response = sendRequestService.sendRequest(destinationUrl,formData);
        if (response.getStatusCode().is2xxSuccessful()) {
            Map<String, Object> updatedData = (Map<String, Object>) response.getBody();
            return ResponseEntity.ok(updatedData);
        } else {
            return ResponseEntity.status(response.getStatusCode()).body(null);
        }
    }


    @GetMapping("/user/display")
    public ResponseEntity<Resource> displayUser(HttpSession session) {

        String RESOURCE = "classpath:templates/displayUser.html";
        Resource resource = resourceLoader.getResource(RESOURCE);

        if (resource.exists()) {
            // Replace placeholders in the HTML template with actual data
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "text/html")
                    .body(resource);
        }
        return ResponseEntity.notFound().build();
    }

}
