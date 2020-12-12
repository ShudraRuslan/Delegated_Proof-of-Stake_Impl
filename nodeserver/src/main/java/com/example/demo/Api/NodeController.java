package com.example.demo.Api;

import com.example.demo.Classes.Node;
import com.example.demo.Classes.ValidatorRepo;
import com.example.demo.Service.ElectionService;
import com.example.demo.Service.IpService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/node")
public class NodeController {
    private Set<Integer> validatorsIdList;
    private final Node node;
    private final ValidatorRepo repo;

    @Autowired
    public NodeController(ValidatorRepo repo) {

        this.node = new Node();
        this.validatorsIdList = new HashSet<>();
        this.repo = repo;
        ElectionService electionService = new ElectionService(repo, node, validatorsIdList);
        IpService ipService = new IpService();
        electionService.start();
        ipService.start();
    }

    @PostMapping
    public ResponseEntity<Void> getValidatorsId(@RequestParam String json) throws UnsupportedEncodingException {
        Gson gson = new Gson();
        Type type = new TypeToken<Set<Integer>>() {
        }.getType();
        String result = java.net.URLDecoder.decode(json, StandardCharsets.UTF_8.name());
        validatorsIdList = gson.fromJson(result, type);
        return ResponseEntity.ok().build();

    }


}
