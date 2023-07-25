package com.formacionbdi.springboot.app.productos.controllers;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.formacionbdi.springboot.app.commons.models.Producto;
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
				//p.setPort(serverPort);
				p.setPort(env.getProperty("local.server.port"));
				return p;
			}).collect(Collectors.toList());
		return lista;
	}
	
	@GetMapping("/ver/{id}")
	public Producto detalle( @PathVariable Long id) throws InterruptedException {
		if (id.equals(10L)) {
			throw new IllegalStateException("Producto no encontrado");
		}
		
		if(id.equals(7L)) {
			TimeUnit.SECONDS.sleep(5); //simula un Thread.sleep
		}
		boolean ok = true;
		//la variable ok se añadió para probar el funcionamiento hystrix. tolerancia a fallos.
		if(!ok) {
			throw new RuntimeException("No se pudo cargar el producto");
		}
		//try {
			//Lo dejamos en 500ms. Con zuul sí que da el error de timeout cuando llama de un microservicio a otro.
		//	Thread.sleep(5000L);
		//} catch (InterruptedException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
		//}
		return productoService.findById(id);
	}
	
	@PostMapping("/crear")
	@ResponseStatus(code = HttpStatus.CREATED)
	public Producto crear(@RequestBody Producto producto) {
		return productoService.save(producto);
	}
	
	@PutMapping("/editar/{id}")
	@ResponseStatus(code = HttpStatus.CREATED)
	public Producto editar(@PathVariable Long id, @RequestBody Producto producto) {
		Producto productoDB = productoService.findById(id);
		productoDB.setNombre(producto.getNombre());
		productoDB.setPrecio(producto.getPrecio());
		return productoService.save(productoDB);
	}
	
	@DeleteMapping("/eliminar/{id}")
	@ResponseStatus(code=HttpStatus.NO_CONTENT)
	public void eliminar(@PathVariable Long id) {
		productoService.deleteById(id);
	}
	
	
	
}
