package com.formacionbdi.springboot.app.productos.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.formacionbdi.springboot.app.productos.models.entity.Producto;
import com.formacionbdi.springboot.app.productos.models.service.IProductoService;

@RestController
public class ProductoController {

	@Autowired
	private IProductoService productoService;
	
	@Autowired
	private Environment env;
	
	private Logger logger = LoggerFactory.getLogger(ProductoController.class);
	
	@Value("${server.port}")
	private String serverPort; //se inyecta dicha variable y no hace falta hacer uso del entorno de spring.
	
	@GetMapping("/listar")
	public List<Producto> listar(){
		List<Producto> lista = productoService.findAll()
			.stream().map(p->{
				p.setPort(serverPort);
				return p;
			}).collect(Collectors.toList());
		logger.info("hola");
		return lista;
	}
	
	@GetMapping("/ver/{id}")
	public Producto detalle( @PathVariable Long id) {
		return productoService.findById(id);
	}
	
	
}
