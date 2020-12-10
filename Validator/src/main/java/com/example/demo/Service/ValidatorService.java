package com.example.demo.Service;

import com.example.demo.Classes.Validator;
import com.example.demo.Classes.ValidatorRepo;
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

@Service
public class ValidatorService {
    private final ValidatorRepo repo;
    private final String address = "http://mainserver:8083";
    private final RestTemplate template = new RestTemplate();
    private Integer sendCoef;


    @Autowired
    public ValidatorService(ValidatorRepo repo) {
        this.repo = repo;
    }

    public void createNewValidator() {
        Validator validator = new Validator();
        repo.save(validator);
        Integer id = validator.getValidatorId();
        this.sendCoef = id;
        sendIpAddressAndId(id);
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


    private void sendIpAddressAndId(Integer validatorId) {
        String newAddress = address + "/validatorIpAndId";
        String ip = getLocalIpAddress();
        if (ip != null) {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(newAddress).
                    queryParam("validatorId", validatorId).
                    queryParam("validatorIp", ip);

            HttpEntity<String> response = template.exchange(builder.toUriString(), HttpMethod.POST, null, String.class);
            System.out.println(response.getBody());
        }
    }

    public int getSendValue() {
        return (int) (Math.random() * (10 * sendCoef + 10));
    }
}
