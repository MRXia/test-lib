package com.mrxia.testlib.service.impl;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

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

    public static final String ZHENGDA_SESSION_ID = "ZDSESSIONID";

    private final RestTemplate restTemplate;

    private final RestTemplate noRedirectRestTemplate;

    private final HtmlParseService htmlParseService;

    @Value("${remote.zhengda.address}")
    private String zhengdaAddress;

    private static final String URL_SEPARATOR = "/";

    private static final String COOKIES_SEPARATOR = ";";

    private static final String KEY_VALUE_SEPARATOR = "=";


    @Autowired
    public RemoteServiceImpl(RestTemplate restTemplate,
                             RestTemplate noRedirectRestTemplate,
                             HtmlParseService htmlParseService) {
        this.restTemplate = restTemplate;
        this.noRedirectRestTemplate = noRedirectRestTemplate;
        this.htmlParseService = htmlParseService;
    }

    @Override
    public String login(String userName, String password) {

        String request = requestBuilder().path("/tc/lgproc.jsp")
                .queryParam("model", "login")
                .queryParam("username", userName)
                .queryParam("password", password)
                .build().toString();
        ResponseEntity<String> response = restTemplate.getForEntity(request, String.class);

        return cookieMap(response.getHeaders()).get("JSESSIONID");
    }

    @Override
    public String getSubjectSelectAddress(String sessionId) {

        HttpEntity<?> requestEntity = createHttpEntity(sessionId);

        ResponseEntity<String> response = noRedirectRestTemplate.exchange(
                requestBuilder("/tc/selecttype.jsp"),
                HttpMethod.GET,
                requestEntity,
                String.class);

        return response.getHeaders().getFirst(HttpHeaders.LOCATION);
    }

    @Override
    public Collection<Subject> getSubjectList(String sessionId) {

        HttpEntity<?> requestEntity = createHttpEntity(sessionId);

        ResponseEntity<String> response = restTemplate.exchange(
                requestBuilder("/tc/selecttype.jsp"),
                HttpMethod.GET,
                requestEntity,
                String.class);

        // 解析请求得到的body，获得科目集合
        return htmlParseService.parseSubjectList(response.getBody());
    }

    @Override
    public TestPaper selectTestPaper(String sessionId, Integer subjectType, Integer paperId) {

        HttpEntity<?> requestEntity = createHttpEntity(sessionId);

        // 第一次请求，选择做题试卷
        String request = requestBuilder().path("/tc/lgproc.jsp")
                .queryParam("model", "opensj")
                .queryParam("kemu", subjectType)
                .queryParam("shijuanid", paperId)
                .build().toString();
        ResponseEntity<String> response = restTemplate.exchange(request, HttpMethod.GET, requestEntity, String.class);

        // 从响应中获取tickId
        Integer tickId = htmlParseService.parseTickId(response.getBody());

        // 第二次请求，获取考题
        request = requestBuilder().path("/pecf")
                .queryParam("model", "cjgetkaoti")
                .queryParam("tid", tickId)
                .queryParam("subjectType", subjectType)
                .build().toString();
        return restTemplate.postForEntity(request, requestEntity, TestPaper.class).getBody();
    }

    private UriComponentsBuilder requestBuilder() {
        return UriComponentsBuilder.fromHttpUrl("http://" + zhengdaAddress);
    }

    private String requestBuilder(String path) {
        return requestBuilder().path(path).build().toString();
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

    /**
     * 创建带有sessionId的httpEntity
     *
     * @return httpEntity
     */
    private HttpEntity<?> createHttpEntity(String sessionId) {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add(HttpHeaders.COOKIE, "JSESSIONID=" + sessionId);
        return new HttpEntity<>(null, requestHeaders);
    }
}
