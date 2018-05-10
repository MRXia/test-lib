package com.mrxia.testlib.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.mrxia.testlib.service.RemoteService;

/**
 * @author xiazijian
 */
@Service
public class RemoteServiceImpl implements RemoteService {

    private final RestTemplate restTemplate;

    @Value("${remote.zhengda.address}")
    private String zhengdaAddress;

    @Autowired
    public RemoteServiceImpl(RestTemplate restTemplate) {this.restTemplate = restTemplate;}

    @Override
    public String login(String userName, String password) {

        ResponseEntity<String> response = restTemplate.getForEntity(
                requestUrl("lgproc.jsp?model=login&username={userName}&password={password}"),
                String.class, userName, password);

        List<String> cookies = response.getHeaders().get("Set-Cookie");

        return Optional.of(cookies).map(cs -> cs.get(0)).orElse("");
    }

    private String requestUrl(String path) {
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        return "http://" + zhengdaAddress + path;
    }
}
