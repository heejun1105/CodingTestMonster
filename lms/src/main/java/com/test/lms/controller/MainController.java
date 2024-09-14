package com.test.lms.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("/api/message")
    public Map<String, String> getMessage() {
        Map<String, String> message = new HashMap<>();
        message.put("content", "Hello from Spring Boot!");
        return message;
    }
    
}
