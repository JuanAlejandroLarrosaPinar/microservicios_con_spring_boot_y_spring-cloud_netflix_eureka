package com.formacionbdi.springboot.app.item.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.formacionbdi.springboot.app.item.models.Item;
import com.formacionbdi.springboot.app.item.models.Producto;
import com.formacionbdi.springboot.app.item.models.service.ItemService;
//import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;//lo comentamos porque ya no vamos a utilizar hystrix, sino resilience4j


@RestController
public class ItemController {
	
	private Logger logger = LoggerFactory.getLogger(ItemController.class);
	
	@Autowired
	private CircuitBreakerFactory cbFactory;

	@Autowired
	@Qualifier(value = "serviceRestTemplate")
	private ItemService itemService;
	
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
}
