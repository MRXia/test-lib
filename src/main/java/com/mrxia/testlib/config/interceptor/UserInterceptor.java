package com.mrxia.testlib.config.interceptor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import com.mrxia.testlib.constant.SessionKey;

/**
 * 用户登录拦截器
 *
 * @author xiazijian
 */
public class UserInterceptor implements HandlerInterceptor {

    private String loginPath;

    public UserInterceptor(String loginPath) {
        this.loginPath = loginPath;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {

        // 如果用户未登录，则跳转登录页面
        Object user = request.getSession().getAttribute(SessionKey.USER);
        if (user == null) {
            response.sendRedirect(loginPath);
            return false;
        }
        return true;
    }
}
