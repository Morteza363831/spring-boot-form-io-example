package org.camunda.bpm.getstarted.springbootformioexample.controller;

import org.camunda.bpm.getstarted.springbootformioexample.service.FormService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Controller
@RequestMapping("/api/forms")
public class FormController {

    @GetMapping("/form")
    public String form() {
        return "formio.html";
    }

    @PostMapping("/form/complete")
    public String completeForm(@RequestBody Map<String, String> formData) {
        System.out.println(formData.get("name") + " " + formData.get("password"));
        return "redirect:/api/forms/form";
    }


}
