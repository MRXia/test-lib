package com.mrxia.testlib.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mrxia.common.result.RestResult;
import com.mrxia.testlib.bean.request.StartTestRequest;
import com.mrxia.testlib.domain.TestPaper;
import com.mrxia.testlib.domain.User;
import com.mrxia.testlib.service.impl.TestLibServiceImpl;

/**
 * 题库Rest接口控制类
 * @author xiazijian
 */
@RestController
public class TestLibRestController {

    private final TestLibServiceImpl testLibService;

    public TestLibRestController(TestLibServiceImpl testLibService) {this.testLibService = testLibService;}

    @PostMapping("/paper/start")
    public RestResult<TestPaper> startTest(@Validated @RequestBody StartTestRequest startRequest,
                                           @ModelAttribute User loginUser) {
        return RestResult.success(
                testLibService.getTestPaper(loginUser, startRequest.getSubjectId(), startRequest.getPaperId()));
    }
}
