package com.mrxia.testlib.service;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.mrxia.testlib.domain.Subject;
import com.mrxia.testlib.domain.TestPaper;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RemoteServiceImplTest {

    @Autowired
    private RemoteService remoteService;

    @Before
    public void setUp() {
    }

    @Test
    public void login() {

        String sessionId = remoteService.login("曾雪萍", "920424zxp");
        System.out.println(sessionId);
    }

    @Test
    public void getSubjectList() {

        String sessionId = remoteService.login("曾雪萍", "920424zxp");
        Collection<Subject> subjectList = remoteService.getSubjectList(sessionId);
        for (Subject subject : subjectList) {
            System.out.println(subject);
            for (TestPaper testPaper : subject.getTestPapers()) {
                System.out.println(testPaper);
            }
        }
    }
}