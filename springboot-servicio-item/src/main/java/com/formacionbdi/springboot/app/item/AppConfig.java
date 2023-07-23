package com.formacionbdi.springboot.app.item;

import java.time.Duration;

import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;

@Configuration
public class AppConfig {

	@Bean(name = "clienteRest")
	@LoadBalanced //sólo añadiendo esta anotación se configura Ribbon para RestTemplate
	public RestTemplate registrarRestTemplate() {
		return new RestTemplate();
	}
	
	@Bean
	public Customizer<Resilience4JCircuitBreakerFactory> defaultCustomizer(){
		return factory -> factory.configureDefault(id->
			 new Resilience4JConfigBuilder(id).
					circuitBreakerConfig(CircuitBreakerConfig.custom().
							slidingWindowSize(10).
							failureRateThreshold(50f).
							waitDurationInOpenState(Duration.ofSeconds(10L)).
							permittedNumberOfCallsInHalfOpenState(5).
							//slowCallRateThreshold(50f). //llamadas lentas
							//slowCallDurationThreshold(Duration.ofSeconds(2l)).
							build()).
					//timeLimiterConfig(TimeLimiterConfig.ofDefaults()).
					timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(6L)).build()).
					build()
		);
	}
}
