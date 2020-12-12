package com.example.demo.Service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.concurrent.TimeUnit;

public class IpService extends Thread {

    private final Integer validatorId;
    private final String address = "http://controlserver:8083";
    private final RestTemplate template = new RestTemplate();

    public IpService(Integer validatorId) {
        this.validatorId = validatorId;
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


    private void sendIpAddressAndId(Integer validatorId) throws InterruptedException {
        String newAddress = address + "/validatorIpAndId";
        String ip = getLocalIpAddress();
        TimeUnit.SECONDS.sleep(2);
        while (true) {
            if (ip != null) {
                UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(newAddress).
                        queryParam("validatorId", validatorId).
                        queryParam("validatorIp", ip);

                try {
                    HttpEntity<String> response = template.exchange(builder.toUriString(), HttpMethod.POST, null, String.class);
                    System.out.println(response.getBody());
                } catch (Exception ignored) {

                }
            }
            TimeUnit.SECONDS.sleep(30);
        }
    }

    @Override
    public void run() {
        try {
            sendIpAddressAndId(validatorId);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
