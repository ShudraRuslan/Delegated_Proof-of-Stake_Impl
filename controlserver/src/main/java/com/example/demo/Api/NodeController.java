package com.example.demo.Api;

import com.example.demo.Service.ControlService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController
public class NodeController {
    private final ControlService service;

    @Autowired
    public NodeController(ControlService service) {
        this.service = service;
    }

    @PostMapping("/nodeIp")
    public ResponseEntity<String> getNodeIp(@RequestParam String nodeIp) {

        try {
            service.sendValidatorsIpsToNode(nodeIp);
        } catch (Exception ignored) {

        }
        return ResponseEntity.ok("Ip and id were saved!");

    }

    @PostMapping("/validatorElections")
    public ResponseEntity<String> getElectionsResult(@RequestParam String electionResults) throws UnsupportedEncodingException {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<Integer, Double>>() {
        }.getType();
        String result = java.net.URLDecoder.decode(electionResults, StandardCharsets.UTF_8.name());
        Map<Integer, Double> results = gson.fromJson(result, type);
        service.setElectionResults(results);
        return ResponseEntity.ok("Results were committed!");
    }
}
