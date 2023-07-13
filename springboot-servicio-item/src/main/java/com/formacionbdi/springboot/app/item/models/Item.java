package com.formacionbdi.springboot.app.item.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Item {

	private Producto producto;
	private Integer cantidad;
	
	public Double getTotal() {
		return producto.getPrecio()*cantidad.doubleValue();
	}
}
