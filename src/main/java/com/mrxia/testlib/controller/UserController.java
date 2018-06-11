package com.mrxia.testlib.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mrxia.testlib.constant.SessionKey;
import com.mrxia.testlib.domain.User;
import com.mrxia.testlib.service.TestLibService;

/**
 * 用户操作相关接口控制类s
 *
 * @author xiazijian
 */
@Controller
@RequestMapping("/user")
public class UserController {

    private final TestLibService testLibService;

    public UserController(TestLibService testLibService) {
        this.testLibService = testLibService;
    }

    /**
     * 用户登录页面
     *
     * @return 登录页面路径
     */
    @GetMapping("/login")
    public String login() {
        return "user/login";
    }

    /**
     * 用户登录操作
     *
     * @param username 用户名
     * @param password 密码
     * @param session  会话对象
     * @return 如果登录成功，跳转到主页，否则重定向到登录页面
     */
    @PostMapping("/login")
    public String login(String username, String password, HttpSession session) {

        User user = testLibService.login(username, password);
        if (user == null) {
            return "redirect:/login";
        }

        session.setAttribute(SessionKey.USER, user);
        return "redirect:/index";
    }
}
