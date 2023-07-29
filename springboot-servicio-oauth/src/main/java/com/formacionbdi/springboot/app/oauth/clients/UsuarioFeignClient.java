package com.formacionbdi.springboot.app.oauth.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.formacionbdi.springboot.app.commons.usuarios.models.entity.Usuario;

@FeignClient(name="servicio-usuarios")
public interface UsuarioFeignClient {

	@GetMapping("/usuarios/search/buscar-username")
	public List<Usuario> findByUserName(@RequestParam String nombre);
	
	@GetMapping("/buscarPorNombre")
	public Usuario buscarPorNombre(@RequestParam String nombre);
}
