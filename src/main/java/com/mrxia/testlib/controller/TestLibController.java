package com.mrxia.testlib.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.mrxia.testlib.domain.User;
import com.mrxia.testlib.service.TestLibService;

/**
 * 题库视图接口控制类
 * @author xiazijian
 */
@Controller
public class TestLibController {

    private final TestLibService testLibService;

    public TestLibController(TestLibService testLibService) {
        this.testLibService = testLibService;
    }

    @GetMapping({"/", "/index"})
    public String index(Map<String, Object> map, @SessionAttribute("login-user") User user) {

        map.put("subjects", testLibService.listSubject(user));
        return "index";
    }
}
