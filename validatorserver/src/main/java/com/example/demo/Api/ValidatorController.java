package com.example.demo.Api;

import com.example.demo.Service.ValidatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/validator")
public class ValidatorController {
    private final ValidatorService service;

    @Autowired
    public ValidatorController(ValidatorService service) throws InterruptedException {
        this.service = service;
        service.createNewValidator();
    }

    @GetMapping
    public ResponseEntity<Double> getResultValue() {
        return ResponseEntity.ok(service.getSendValue());
    }
}
