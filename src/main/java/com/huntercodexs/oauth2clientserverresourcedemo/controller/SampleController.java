package com.huntercodexs.oauth2clientserverresourcedemo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}")
public class SampleController {

    @GetMapping(value = "/welcome")
    public ResponseEntity<?> welcome() {
        return ResponseEntity.ok().body("Welcome to OAUTH2-CLIENT-SERVER-RESOURCE-DEMO");
    }

    @GetMapping(value = "/products")
    public ResponseEntity<?> products() {
        return ResponseEntity.ok().body("Products is available !");
    }

    @GetMapping(value = "/samples")
    public ResponseEntity<?> samples() {
        return ResponseEntity.ok().body("Samples is available !");
    }
}
