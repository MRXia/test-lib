package com.mrxia.testlib.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
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

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        for (HttpMessageConverter<?> converter : converters) {
            if (converter instanceof MappingJackson2HttpMessageConverter) {

                // 先将原先支持的MediaType列表拷出
                List<MediaType> mediaTypeList = new ArrayList<>(converter.getSupportedMediaTypes());

                // 加入对text/html的支持
                mediaTypeList.add(MediaType.TEXT_HTML);

                // 将已经加入了text/html的MediaType支持列表设置为其支持的媒体类型列表
                ((MappingJackson2HttpMessageConverter) converter).setSupportedMediaTypes(mediaTypeList);
            }
        }
    }
}
