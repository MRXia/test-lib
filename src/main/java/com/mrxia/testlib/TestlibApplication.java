package com.mrxia.testlib;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.mrxia.testlib.config.http.NoRedirectClientHttpRequestFactory;

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

	@Bean
	public RestTemplate noRedirectRestTemplate() {

		NoRedirectClientHttpRequestFactory factory = new NoRedirectClientHttpRequestFactory();
		factory.setConnectTimeout(6000);
		factory.setReadTimeout(60000);
		return new RestTemplate(factory);
	}

}
