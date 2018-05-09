package com.mrxia.testlib;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * 应用启动类
 *
 * @author xiazijian
 */
@SpringBootApplication
public class TestlibApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestlibApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
}
