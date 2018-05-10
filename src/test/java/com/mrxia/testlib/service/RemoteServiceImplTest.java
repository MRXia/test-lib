package com.mrxia.testlib.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.mrxia.testlib.service.impl.RemoteServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RemoteServiceImplTest {

    @Autowired
    private RemoteService remoteService;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void login() throws Exception {

        String sessionId = remoteService.login("曾雪萍", "920424zxp");
        System.out.println(sessionId);
    }

}