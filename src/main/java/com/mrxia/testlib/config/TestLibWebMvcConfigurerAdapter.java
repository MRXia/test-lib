package com.mrxia.testlib.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.mrxia.testlib.config.interceptor.UserInterceptor;

/**
 * MVC相关配置
 *
 * @author xiazijian
 */
@SpringBootConfiguration
public class TestLibWebMvcConfigurerAdapter implements WebMvcConfigurer {

    @Value("${spring.mvc.login-path:/login}")
    private String loginPath;

    @Value("${spring.mvc.static-path-pattern}")
    private String staticPath;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册登录拦截器，排除登录页面和静态资源
        registry.addInterceptor(new UserInterceptor(loginPath))
                .addPathPatterns("/**")
                .excludePathPatterns(loginPath, staticPath, "/error");
    }
}
