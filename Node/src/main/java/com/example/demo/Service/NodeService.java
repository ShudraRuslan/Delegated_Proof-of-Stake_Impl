package com.example.demo.Service;


import com.example.demo.Classes.Node;
import com.example.demo.Classes.ValidatorRepo;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.lang.Math.random;

@Service
public class NodeService {
    private final String address = "http://mainserver:8083";
    private final RestTemplate template = new RestTemplate();
    private final ValidatorRepo repo;

    @Autowired
    public NodeService(ValidatorRepo repo) {
        this.repo = repo;
    }

    public Node createNewNode() {
        Node node = new Node();
        sendIpAddress();
        return node;
    }

    private Map<Integer, Double> getValidatorsVotingResult(Node node, Set<Integer> idList) {
        Map<Integer, Double> result = new HashMap<Integer, Double>();
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

    public void sendResults(Node node, Set<Integer> idList) {

        String newAddress = address + "/validatorElections";
        Map<Integer, Double> results = getValidatorsVotingResult(node, idList);
        Gson gson = new Gson();
        String object = gson.toJson(results);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(newAddress).
                queryParam("electionResults", object);
        HttpEntity<String> response = template.exchange(builder.toUriString(), HttpMethod.POST, null, String.class);
        System.out.println(response.getBody());

    }

    private static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }


    private void sendIpAddress() {
        String newAddress = address + "/nodeIp";
        String ip = getLocalIpAddress();
        if (ip != null) {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(newAddress).
                    queryParam("nodeIp", ip);

            HttpEntity<String> response = template.exchange(builder.toUriString(), HttpMethod.POST, null, String.class);
            System.out.println(response.getBody());

        }


    }
}
