package com.formacionbdi.springboot.app.usuarios.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.formacionbdi.springboot.app.commons.usuarios.models.entity.Usuario;
import com.formacionbdi.springboot.app.usuarios.models.dao.UsuarioDao;

@RestController
public class UsuarioController {
	
	@Autowired
	private UsuarioDao usuarioDao;

	@GetMapping("/buscarPorNombre")
	public Usuario buscarPorNombre(@RequestParam String nombre) {
		List<Usuario> list = usuarioDao.findByUsername(nombre);
		if(ObjectUtils.isEmpty(list)) {
			return null;
		}
		return list.get(0);
	}
}
