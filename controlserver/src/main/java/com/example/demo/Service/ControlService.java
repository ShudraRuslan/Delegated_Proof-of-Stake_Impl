package com.example.demo.Service;

import com.example.demo.Classes.ValidatorRepo;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

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

    public Integer getValidatorsQueue() {
        List<Integer> idList = findWinnersAmongValidators();
        Collections.shuffle(idList);
        return idList.get(0);
    }

    public String getValidatorsIpQueue(Integer id) {

        return validatorInfo.get(id);
    }
}
