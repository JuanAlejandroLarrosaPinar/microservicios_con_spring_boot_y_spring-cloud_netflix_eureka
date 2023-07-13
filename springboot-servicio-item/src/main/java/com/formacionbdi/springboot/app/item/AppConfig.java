package com.formacionbdi.springboot.app.item;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

	@Bean(name = "clienteRest")
	public RestTemplate registrarRestTemplate() {
		return new RestTemplate();
	}
}
