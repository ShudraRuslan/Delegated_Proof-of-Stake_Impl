package com.example.demo.Api;

import com.example.demo.Classes.ValidatorRepo;
import com.example.demo.Service.ControlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClientController {
    private final ValidatorRepo repo;
    private final ControlService service;
    public Integer returnValue;

    @Autowired
    public ClientController(ValidatorRepo repo, ControlService service) {
        this.repo = repo;
        this.service = service;
        updateReturnValue();
    }

    @GetMapping("/client")
    public ResponseEntity<Integer> getValue() {
        return ResponseEntity.ok(returnValue);
    }

    public void updateReturnValue() {

    }
}
