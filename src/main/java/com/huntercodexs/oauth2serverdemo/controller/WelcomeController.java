package com.huntercodexs.oauth2serverdemo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}")
public class WelcomeController {

    @GetMapping(value = "/welcome")
    public ResponseEntity<?> welcome() {
        return ResponseEntity.ok().body("Welcome to OAUTH2-SERVER-DEMO");
    }
}
