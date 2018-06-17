package com.mrxia.testlib.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.mrxia.testlib.bean.request.StartTestRequest;
import com.mrxia.testlib.domain.User;
import com.mrxia.testlib.service.TestLibService;

/**
 * 题库视图接口控制类
 *
 * @author xiazijian
 */
@Controller
public class TestLibController {

    private final TestLibService testLibService;

    public TestLibController(TestLibService testLibService) {
        this.testLibService = testLibService;
    }

    @GetMapping({"/", "/index"})
    public String index(Map<String, Object> map, @ModelAttribute User loginUser) {

        map.put("subjects", testLibService.listSubject(loginUser));
        return "index";
    }

    @GetMapping("/paper/start")
    public String startTest(@Validated StartTestRequest startRequest,
                            @ModelAttribute User loginUser,
                            Map<String, Object> map) {
        map.put("testPaper",
                testLibService.getTestPaper(loginUser, startRequest.getSubjectId(), startRequest.getPaperId()));
        return "paper/start";
    }
}
