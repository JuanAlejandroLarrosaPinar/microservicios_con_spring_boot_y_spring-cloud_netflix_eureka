package com.formacionbdi.springboot.app.item.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.formacionbdi.springboot.app.item.models.Item;
import com.formacionbdi.springboot.app.item.models.Producto;
import com.formacionbdi.springboot.app.item.models.service.ItemService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
//import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;//lo comentamos porque ya no vamos a utilizar hystrix, sino resilience4j


@RefreshScope //nos permite actualizar las variables inyectadas sin reiniciar el servidor
@RestController
public class ItemController {
	
	private Logger logger = LoggerFactory.getLogger(ItemController.class);
	
	@Autowired
	private CircuitBreakerFactory cbFactory;
	
	@Autowired
	private Environment env;

	@Autowired
	@Qualifier(value = "serviceRestTemplate")
	private ItemService itemService;
	
	@Value("${configuracion.texto}")
	private String texto;
	
	@GetMapping("/listar")
	public List<Item> listar(@RequestParam(name="nombre", required = false) String nombre, @RequestHeader(name="token-request", required = false) String tokenRequest){
		logger.info("nombre: {}", nombre);
		logger.info("tokenRequest: {}", tokenRequest);
		
		return itemService.findAll();
	}
	
	//@HystrixCommand(fallbackMethod = "metodoAlternativo") //lo comentamos porque ya no vamos a utilizar hystrix, sino resilience4j
	@GetMapping("/ver/{id}/cantidad/{cantidad}")
	public Item detalle(@PathVariable Long id, @PathVariable Integer cantidad) {
		return cbFactory.create("items").run(()->
			itemService.findById(id, cantidad)
			//si se produce un error al llamar al servicio de productos, ejecuta metodoAlternativo.
		, 
		//si comentamos el método alternativo, no hay respuesta por parte del servicio de productos
		//y se produce un NoFallbackAvailableException. 
		e->metodoAlternativo(id, cantidad, e)
		);
	}
	
	@CircuitBreaker(name = "items", fallbackMethod = "metodoAlternativo") //hace la asociación a través del fichero application.yml
	@GetMapping("/ver2/{id}/cantidad/{cantidad}")
	public Item detalle2(@PathVariable Long id, @PathVariable Integer cantidad) {
		return itemService.findById(id, cantidad);
	}
	
	@CircuitBreaker(name = "items", fallbackMethod = "metodoAlternativo2") //hace la asociación a través del fichero application.yml
	@TimeLimiter(name = "items"/*, fallbackMethod = "metodoAlternativo2"*/)
	@GetMapping("/ver3/{id}/cantidad/{cantidad}")
	public CompletableFuture<Item> detalle3(@PathVariable Long id, @PathVariable Integer cantidad) {
		//Estamos haciendo una llamada que tiene delay. No podemos calcular cuánto tiempo tarda
		//Utilizamos una clase llamada CompletableFuture y es para controlar llamadas asíncronas.
		return CompletableFuture.supplyAsync(()->itemService.findById(id, cantidad)) ;
	}

	public Item metodoAlternativo(@PathVariable Long id, @PathVariable Integer cantidad, Throwable e) {
		logger.error(e.getMessage());
		Item item = new Item();
		item.setCantidad(cantidad);
		
		Producto producto = new Producto();
		producto.setId(id);
		producto.setNombre("Camara Sony");
		producto.setPrecio(500.0);
		item.setProducto(producto);
		return item;
	}
	
	public CompletableFuture<Item> metodoAlternativo2(@PathVariable Long id, @PathVariable Integer cantidad, Throwable e) {
		logger.error(e.getMessage());
		Item item = new Item();
		item.setCantidad(cantidad);
		
		Producto producto = new Producto();
		producto.setId(id);
		producto.setNombre("Camara Sony 2");
		producto.setPrecio(510.0);
		item.setProducto(producto);
		return CompletableFuture.supplyAsync(()->item);
	}
	
	@GetMapping("/obtener-config")
	public ResponseEntity<?> obtenerConfig(@Value("${server.port}") String puerto){
		Map<String,String> json = new HashMap<>();
		json.put("texto", texto);
		json.put("puerto", puerto);
		
		if(env.getActiveProfiles().length>0 && env.getActiveProfiles()[0].equals("dev")) {
			json.put("autor.nombre", env.getProperty("configuracion.autor.nombre"));
			json.put("autor.email", env.getProperty("configuracion.autor.email"));
		}
		return new ResponseEntity<Map<String,String>>(json, HttpStatus.OK);
	}
}
