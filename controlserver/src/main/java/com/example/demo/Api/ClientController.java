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

@RestController
public class ClientController {
    private final ValidatorRepo repo;
    private final ControlService service;
    private final RestTemplate template = new RestTemplate();

    @Autowired
    public ClientController(ValidatorRepo repo, ControlService service) throws InterruptedException {
        this.repo = repo;
        this.service = service;
    }

    @GetMapping("/client")
    public ResponseEntity<Double> getValue() {
        Double returnValue = updateReturnValue();
        return ResponseEntity.ok(returnValue);
    }

    public Double updateReturnValue() {

        Integer validatorId = service.getValidatorsQueue();
        String validatorIp = service.getValidatorsIpQueue(validatorId);
        String address = "http://" + validatorIp + ":8082/validator/";
        Validator validator = repo.getValidatorByValidatorId(validatorId);
        Double resultValue = 1.0;
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(address);
            ResponseEntity<Double> response = template.exchange(builder.toUriString(), HttpMethod.GET, null, Double.class);
            if (response.getBody() == null) {
                if (validator.getReputation() > 0) {
                    validator.setReputation(validator.getReputation() - 1);
                    repo.save(validator);
                    resultValue = 0.0;
                }
            } else {
                validator.setReputation(validator.getReputation() + 1);
                repo.save(validator);
                resultValue = response.getBody();
            }
        } catch (Exception e) {
            if (validator.getReputation() > 0) {
                validator.setReputation(validator.getReputation() - 1);
                repo.save(validator);
                resultValue = -1.0;
            }
        }
        return resultValue;
    }
}





