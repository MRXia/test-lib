package com.mrxia.testlib.service.impl;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.common.collect.Maps;
import com.mrxia.testlib.domain.Subject;
import com.mrxia.testlib.domain.TestPaper;
import com.mrxia.testlib.service.HtmlParseService;
import com.mrxia.testlib.service.RemoteService;

/**
 * @author xiazijian
 */
@Service
public class RemoteServiceImpl implements RemoteService {

    private final RestTemplate restTemplate;

    private final HtmlParseService htmlParseService;

    @Value("${remote.zhengda.address}")
    private String zhengdaAddress;

    private static final String URL_SEPARATOR = "/";

    private static final String COOKIES_SEPARATOR = ";";

    private static final String KEY_VALUE_SEPARATOR = "=";


    @Autowired
    public RemoteServiceImpl(RestTemplate restTemplate, HtmlParseService htmlParseService) {
        this.restTemplate = restTemplate;
        this.htmlParseService = htmlParseService;
    }

    @Override
    public String login(String userName, String password) {

        ResponseEntity<String> response = restTemplate.getForEntity(
                requestUrl("/tc/lgproc.jsp?model=login&username={userName}&password={password}"),
                String.class, userName, password);


        return cookieMap(response.getHeaders()).get("JSESSIONID");
    }

    @Override
    public Collection<Subject> getSubjectList(String sessionId) {

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add(HttpHeaders.COOKIE, "JSESSIONID=" + sessionId);
        HttpEntity<?> requestEntity = new HttpEntity<>(null, requestHeaders);

        // 目前仅抓取第二类题
        ResponseEntity<String> response = restTemplate.exchange(requestUrl("/tc/selectsj_cj.jsp?kemuid=2"),
                HttpMethod.GET,
                requestEntity,
                String.class);

        return htmlParseService.parseSubjectList(response.getBody());
    }

    @Override
    public TestPaper selectTestPaper(Integer paperId) {
        return null;
    }


    private String requestUrl(String path) {
        if (!path.startsWith(URL_SEPARATOR)) {
            path = URL_SEPARATOR + path;
        }
        return "http://" + zhengdaAddress + path;
    }

    private Map<String, String> cookieMap(HttpHeaders headers) {

        String cookieString = headers.getFirst(HttpHeaders.SET_COOKIE);

        if (cookieString == null) {
            return Collections.emptyMap();
        }

        LinkedHashMap<String, String> cookieMap = Maps.newLinkedHashMap();
        String[] kv;
        for (String keyValue : cookieString.split(COOKIES_SEPARATOR)) {
             kv = keyValue.trim().split(KEY_VALUE_SEPARATOR);
            cookieMap.put(kv[0], kv[1]);
        }
        return cookieMap;
    }
}
