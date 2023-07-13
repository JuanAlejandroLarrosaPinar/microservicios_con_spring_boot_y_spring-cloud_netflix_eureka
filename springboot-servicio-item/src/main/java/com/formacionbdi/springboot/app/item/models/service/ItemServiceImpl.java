package com.formacionbdi.springboot.app.item.models.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.formacionbdi.springboot.app.item.models.Item;
import com.formacionbdi.springboot.app.item.models.Producto;

@Service
public class ItemServiceImpl implements ItemService{

	@Autowired
	private RestTemplate clienteRest;
	
	@Override
	public List<Item> findAll() {
		List<Producto> productos =
				Arrays.asList(
				clienteRest.getForObject("http://localhost:8001/listar", Producto[].class));
		return productos.stream().map(p->
			new Item(p, 1)
		).toList();
	}

	@Override
	public Item findById(Long id, Integer cantidad) {
		Map<String, String> mapa = new HashMap<>();
		mapa.put("id", id.toString());
		Producto producto = clienteRest.getForObject("http://localhost:8001/ver/{id}",
				Producto.class, mapa);
		return new Item(producto, cantidad);
	}

}
