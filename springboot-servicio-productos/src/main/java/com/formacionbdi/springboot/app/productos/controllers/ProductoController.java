package com.formacionbdi.springboot.app.productos.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.formacionbdi.springboot.app.productos.models.entity.Producto;
import com.formacionbdi.springboot.app.productos.models.service.IProductoService;

@RestController
public class ProductoController {

	@Autowired
	private IProductoService productoService;
	
	private Logger logger = LoggerFactory.getLogger(ProductoController.class);
	
	@GetMapping("/listar")
	public List<Producto> listar(){
		logger.info("Antes de consultar " + System.currentTimeMillis());
		List<Producto> lista = productoService.findAll();
		logger.info("Despues de consultar "+ System.currentTimeMillis());
		return lista;
	}
	
	@GetMapping("/ver/{id}")
	public Producto detalle( @PathVariable Long id) {
		return productoService.findById(id);
	}
	
	
}
