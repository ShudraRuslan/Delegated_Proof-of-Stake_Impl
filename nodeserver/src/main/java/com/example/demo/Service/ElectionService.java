package com.example.demo.Service;

import com.example.demo.Classes.Node;
import com.example.demo.Classes.ValidatorRepo;
import com.google.gson.Gson;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static java.lang.Math.random;

public class ElectionService extends Thread {

    private final String address = "http://controlserver:8083";
    private final RestTemplate template = new RestTemplate();
    private final ValidatorRepo repo;
    private final Node node;
    private final Set<Integer> validatorsIdList;


    public ElectionService(ValidatorRepo repo, Node node, Set<Integer> validatorsIdList) {
        this.repo = repo;
        this.node = node;
        this.validatorsIdList = validatorsIdList;
    }

    private Map<Integer, Double> getValidatorsVotingResult(Node node, Set<Integer> validatorIdList) {
        Map<Integer, Double> result = new HashMap<Integer, Double>();
        Set<Integer> idList;
        try {
            idList = repo.getIdList();
        } catch (Exception e) {
            idList = validatorIdList;
        }

        double nodeCash = node.getCash();
        for (Integer integer : idList) {
            double randomRez = random();
            int validatorReputation = repo.getReputationByValidatorId(integer) + 1;
            int maxReputation = repo.getMaxReputation() + 1;
            double voteCash = nodeCash * randomRez * validatorReputation / maxReputation;
            result.put(integer, voteCash);
            nodeCash = nodeCash - voteCash;
        }
        return result;
    }

    public void sendResults(Node node, Set<Integer> validatorIdList) {

        String newAddress = address + "/validatorElections";
        Map<Integer, Double> results = getValidatorsVotingResult(node, validatorIdList);
        Gson gson = new Gson();
        String object = gson.toJson(results);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(newAddress).
                queryParam("electionResults", object);

        try {
            HttpEntity<String> response = template.exchange(builder.toUriString(), HttpMethod.POST, null, String.class);
            System.out.println(response.getBody());
        } catch (Exception ignored) {

        }

    }


    public void makeElections(Node node, Set<Integer> validatorsIdList) throws InterruptedException {

        while (true) {
            sendResults(node, validatorsIdList);
            TimeUnit.SECONDS.sleep(30);
        }
    }

    @Override
    public void run() {
        try {
            makeElections(node, validatorsIdList);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
