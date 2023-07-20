package com.formacionbdi.springboot.app.gateway.filters;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class EjemploGlobalFilter implements GlobalFilter, Ordered{

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		//a partir del exchange podemos acceder al request y al response
		logger.info("ejecutando filtro pre");
		//modificamos el request añadiendo un header
		exchange.getRequest().mutate().headers(h->{
			h.add("token", "123456");
		});
		return chain.filter(exchange).then(Mono.fromRunnable(()->{
			logger.info("ejecutando filtro post");
			Optional.ofNullable(exchange.getRequest().getHeaders().getFirst("token")).ifPresent(token->{
				exchange.getResponse().getHeaders().add("token", token);
			});
			exchange.getResponse().getCookies().add("color", ResponseCookie.from("color", "rojo").build());
			//exchange.getResponse().getHeaders().setContentType(MediaType.TEXT_PLAIN);
		}));
	}

	@Override
	public int getOrder() {
		return 1; //cuanto mayor es el número, menor prioridad. Debe ser positivo.
	}
	
	

}
