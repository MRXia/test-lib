package com.mrxia.testlib;

import java.util.Collections;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
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
		RestTemplate template = builder.build();
		for(HttpMessageConverter<?> converter : template.getMessageConverters()){
			if(converter instanceof MappingJackson2HttpMessageConverter){
				((MappingJackson2HttpMessageConverter) converter)
						.setSupportedMediaTypes(Collections.singletonList(MediaType.TEXT_HTML));
			}
		}
		return template;
	}

	@Bean
	public RestTemplate noRedirectRestTemplate() {

		NoRedirectClientHttpRequestFactory factory = new NoRedirectClientHttpRequestFactory();
		factory.setConnectTimeout(6000);
		factory.setReadTimeout(60000);
		return new RestTemplate(factory);
	}

}
