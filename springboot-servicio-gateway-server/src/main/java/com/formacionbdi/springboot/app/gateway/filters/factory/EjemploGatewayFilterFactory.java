package com.formacionbdi.springboot.app.gateway.filters.factory;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

//El nombre de la clase debe terminar por "GatewayFilterFactory"
@Component
//Esta clase sirve para indicar en el fichero application.yml el filtro
//además de los campos asociados a la clase interna creada Configuracion.
public class EjemploGatewayFilterFactory extends AbstractGatewayFilterFactory<EjemploGatewayFilterFactory.Configuracion>{

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	public EjemploGatewayFilterFactory() {
		super(Configuracion.class);
	}

	@Override
	public GatewayFilter apply(Configuracion config) {
		//Con OrderedGatewayFilter le estamos dando el orden al filtro
		return new OrderedGatewayFilter ((exchange, chain)->{
			logger.info("ejecutando pre gateway filter factory: " + config.mensaje);
			return chain.filter(exchange).then(Mono.fromRunnable(()->{
				Optional.ofNullable(config.cookieValor).ifPresent(cookieValor->{
					exchange.getResponse().addCookie(ResponseCookie.from(config.cookieNombre, cookieValor).build());
				});
				logger.info("ejecutando post gateway filter factory: "+ config.mensaje);
				
			}));
		},2);
	}
	
	/*Este método sirve para establecer en el fichero application.yml (en una línea)
	- name: Ejemplo=Hola mi mensaje personalizado, usuario, JuanAlejandro
          
	@Override
	public List<String> shortcutFieldOrder() {
		return Arrays.asList("mensaje","cookieNombre","cookieValor");
	}
	*/
	
	/*
	 * Si redefinimos el método name, ya no toma el filtro el nombre de la clase (Ejemplo)
	 * Para que esto funcionase tendríamos que modificar el nombre en el fichero application.yml
	@Override
	public String name() {
		return "EjemploCookie";
	}
	*/
	
	public static class Configuracion {
		private String mensaje;
		private String cookieValor;
		private String cookieNombre;
		public String getMensaje() {
			return mensaje;
		}
		public void setMensaje(String mensaje) {
			this.mensaje = mensaje;
		}
		public String getCookieValor() {
			return cookieValor;
		}
		public void setCookieValor(String cookieValor) {
			this.cookieValor = cookieValor;
		}
		public String getCookieNombre() {
			return cookieNombre;
		}
		public void setCookieNombre(String cookieNombre) {
			this.cookieNombre = cookieNombre;
		}
	}

}
