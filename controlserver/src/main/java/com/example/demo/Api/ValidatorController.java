package com.example.demo.Api;

import com.example.demo.Service.ControlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ValidatorController {
    private final ControlService service;

    @Autowired
    public ValidatorController(ControlService service) {
        this.service = service;
    }

    @PostMapping("/validatorIpAndId")
    public ResponseEntity<String> getValidatorInfo(@RequestParam Integer validatorId, @RequestParam String validatorIp) {
        service.setValidatorInfo(validatorId, validatorIp);
        return ResponseEntity.ok("Info was saved!");
    }
}
