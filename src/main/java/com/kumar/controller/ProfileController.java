package com.kumar.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/profile")
public class ProfileController {

    private final String message;

    public ProfileController(@Value("${spring.message}") String message) {
        this.message = message;
    }

    @GetMapping
    public String hello() {
        return message;
    }
}
