package com.example.demo.Api;

import com.example.demo.Classes.Validator;
import com.example.demo.Classes.ValidatorRepo;
import com.example.demo.Service.ControlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.concurrent.TimeUnit;

@RestController
public class ClientController {
    private final ValidatorRepo repo;
    private final ControlService service;
    private final RestTemplate template = new RestTemplate();

    @Autowired
    public ClientController(ValidatorRepo repo, ControlService service)  {
        this.repo = repo;
        this.service = service;
        service.start();
    }

    @GetMapping("/client")
    public ResponseEntity<Double> getValue() throws InterruptedException {
        Double returnValue = getReturnValue();
        return ResponseEntity.ok(returnValue);
    }

    public Double getReturnValue() throws InterruptedException {
        Integer validatorIdToGetResult = service.getValidatorIdToGetResult();
        String validatorIpToGetResult = service.getValidatorsIpQueue(validatorIdToGetResult);
        String address = "http://" + validatorIpToGetResult + ":8082/validator/";
        Validator validator = repo.getValidatorByValidatorId(validatorIdToGetResult);
        boolean flag = false;
        Double resultValue = null;
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(address);
            ResponseEntity<Double> response = template.exchange(builder.toUriString(), HttpMethod.GET, null, Double.class);
            if (response.getBody() == null) {
                if (validator.getReputation() > 0) {
                    validator.setReputation(validator.getReputation() - 1);
                    repo.save(validator);
                }
            } else {
                validator.setReputation(validator.getReputation() + 1);
                repo.save(validator);
                flag = true;
                resultValue = response.getBody();
            }
        } catch (Exception e) {
            if (validator.getReputation() > 0) {
                validator.setReputation(validator.getReputation() - 1);
                repo.save(validator);
            }
        }
        if (flag)
            return resultValue;
        else {
            TimeUnit.SECONDS.sleep(5);
            getReturnValue();
        }
        return 0.0;
    }
}





