package com.mrxia.testlib.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.common.base.Splitter;
import com.mrxia.testlib.domain.Subject;
import com.mrxia.testlib.domain.TestPaper;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RemoteServiceTest {

    @Autowired
    private RemoteService remoteService;

    private String sessionId;

    @Before
    public void setUp() {
        login();
    }

    public void login() {
        sessionId = remoteService.login("曾雪萍", "920424zxp");
        System.out.println(sessionId);
    }

    @Test
    public void getSubjectList() {

        Collection<Subject> subjectList = remoteService.getSubjectList(sessionId);
        for (Subject subject : subjectList) {
            System.out.println(subject);
        }
    }

    @Test
    public void getSubjectSelectAddress() {

        String subjectSelectAddress = remoteService.getSubjectSelectAddress(sessionId);
        System.out.println(subjectSelectAddress);

    }

    @Test
    public void selectTestPaper() {

        String subjectSelectAddress = remoteService.getSubjectSelectAddress(sessionId);
        URL url;
        try {
            url = new URL(subjectSelectAddress);
        } catch (MalformedURLException e) {
            System.out.println("url:" + subjectSelectAddress + "不合法！");
            return;
        }

        String queryString = url.getQuery();
        Map<String, String> queryMap = StreamSupport
                .stream(Splitter.on("&").split(queryString).spliterator(), true)
                .map(kv -> {
                    String[] keyValue = kv.split("=");
                    return Pair.of(keyValue[0], keyValue[1]);
                })
                .collect(Pair.toMap());

        Integer subjectType = Integer.parseInt(queryMap.get("kemuid"));

        TestPaper testPaper = remoteService.selectTestPaper(sessionId, subjectType, 492);

        System.out.println(testPaper);
    }
}