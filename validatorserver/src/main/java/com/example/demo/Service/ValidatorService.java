package com.example.demo.Service;

import com.example.demo.Classes.Validator;
import com.example.demo.Classes.ValidatorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static java.lang.Math.random;

@Service
public class ValidatorService {
    private final ValidatorRepo repo;
    private final String address = "http://localhost:8083";
    private final RestTemplate template = new RestTemplate();
    private Integer sendValue;


    @Autowired
    public ValidatorService(ValidatorRepo repo) {
        this.repo = repo;
    }

    public void createNewValidator() throws InterruptedException {
        Validator validator = new Validator();
        repo.save(validator);
        Integer id = validator.getValidatorId();
        this.sendValue = id;
        IpService ipService = new IpService(id);
        ipService.start();
    }


    public Double getSendValue() {
        return random() + sendValue;
    }
}
