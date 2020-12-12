package com.example.demo.Service;

import com.example.demo.Classes.Validator;
import com.example.demo.Classes.ValidatorRepo;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class ControlService extends Thread {
    private final ValidatorRepo repo;
    private final RestTemplate template = new RestTemplate();
    private final Map<Integer, String> validatorInfo;
    private final Map<Integer, Double> electionResults;
    private List<Integer> validatorQueue;
    private Integer validatorIdToGetResult;

    @Autowired
    public ControlService(ValidatorRepo repo) {
        this.repo = repo;
        validatorInfo = new HashMap<>();
        electionResults = new HashMap<>();
        validatorQueue = new ArrayList<>();
    }

    public Integer getValidatorIdToGetResult() {
        return validatorIdToGetResult;
    }

    public void setValidatorInfo(Integer id, String ip) {
        validatorInfo.put(id, ip);
    }


    public void setElectionResults(Map<Integer, Double> results) {
        for (Integer integer : results.keySet()) {
            Validator validator = repo.getValidatorByValidatorId(integer);
            double previousCash = validator.getElectionCash();
            double newCash = results.get(integer);
            validator.setElectionCash(previousCash + newCash);
            repo.save(validator);
        }

        this.validatorQueue = getValidatorsQueue();
    }

    public void sendValidatorsIpsToNode(String ip) {
        Set<Integer> ips = validatorInfo.keySet();
        Gson json = new Gson();
        String request = json.toJson(ips);
        String address = "http://" + ip + ":8081/node";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(address).
                queryParam("json", request);
        template.exchange(builder.toUriString(), HttpMethod.POST, null, Void.class);
    }

    private List<Integer> findWinnersAmongValidators() {
        List<Integer> idsList = repo.getSortedIdList();
        for (int i = 3; i < idsList.size(); i++) {
            idsList.remove(i);
        }
        return idsList;
    }

    private List<Integer> getValidatorsQueue() {
        List<Integer> idList = findWinnersAmongValidators();
        Collections.shuffle(idList);
        return idList;
    }

    public String getValidatorsIpQueue(Integer id) {

        return validatorInfo.get(id);
    }

    @Override
    public void run() {
        while (true) {
            try {
                int index = (int) (Math.random() * 3);
                validatorIdToGetResult = validatorQueue.get(index);
                TimeUnit.SECONDS.sleep(5);
            } catch (Exception ignored) {

            }

        }
    }
}
