package com.example.demo.Service;

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
public class ControlService {
    private final ValidatorRepo repo;
    private final RestTemplate template = new RestTemplate();
    private final Map<Integer, String> validatorInfo;
    private final List<String> nodeInfo;
    private final Map<Integer, Double> electionResults;

    @Autowired
    public ControlService(ValidatorRepo repo) {
        this.repo = repo;
        validatorInfo = new HashMap<>();
        nodeInfo = new ArrayList<>();
        electionResults = new HashMap<>();
    }

    public void setValidatorInfo(Integer id, String ip) {
        validatorInfo.put(id, ip);
    }

    public void setNodeInfo(String ip) {
        nodeInfo.add(ip);
    }

    public void setElectionResults(Map<Integer, Double> results) {
        for (Integer integer : results.keySet()) {
            double previousResult = electionResults.get(integer);
            electionResults.put(integer, previousResult + results.get(integer));
        }

    }

    public void sendValidatorsIpsToNodes() throws InterruptedException {
        Set<Integer> ips = validatorInfo.keySet();
        Gson json = new Gson();
        String request = json.toJson(ips);
        TimeUnit.SECONDS.sleep(40);
        for (String s : nodeInfo) {
            String address = "http://" + s + ":8081";
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(address).
                    queryParam("json", request);
            template.exchange(builder.toUriString(), HttpMethod.POST, null, Void.class);

        }
    }

    private List<Integer> findWinnersAmongValidators() {
        List<Integer> idsList = repo.getSortedIdList();
        for (int i = 3; i < idsList.size(); i++) {
            idsList.remove(i);
        }
        return idsList;
    }

    public List<Integer> getValidatorsQueue() {
        List<Integer> idsList = findWinnersAmongValidators();
        Collections.shuffle(idsList);
        return idsList;
    }

    public String getValidatorIpById(Integer id) {
        return validatorInfo.get(id);
    }
}
