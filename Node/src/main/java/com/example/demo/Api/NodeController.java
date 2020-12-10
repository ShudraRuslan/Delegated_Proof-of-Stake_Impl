package com.example.demo.Api;

import com.example.demo.Classes.Node;
import com.example.demo.Service.NodeService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/node")
public class NodeController {
    private final NodeService service;
    private Set<Integer> validatorsIdList;
    private final Node node;

    @Autowired
    public NodeController(NodeService service) {
        this.service = service;
        this.node = service.createNewNode();
        this.validatorsIdList = new HashSet<>();
    }

    @PostMapping
    public ResponseEntity<Void> getValidatorsId(@RequestParam String json) throws InterruptedException {
        Gson gson = new Gson();
        Type type = new TypeToken<Set<Integer>>() {
        }.getType();
        validatorsIdList = gson.fromJson(json, type);
        makeElections();
        return ResponseEntity.ok().build();

    }

    public void makeElections() throws InterruptedException {
        while (true) {
            TimeUnit.SECONDS.sleep(30);
            service.sendResults(node, validatorsIdList);
        }
    }

}
