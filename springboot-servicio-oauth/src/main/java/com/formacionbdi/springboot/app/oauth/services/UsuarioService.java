package com.formacionbdi.springboot.app.oauth.services;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.formacionbdi.springboot.app.commons.usuarios.models.entity.Usuario;
import com.formacionbdi.springboot.app.oauth.clients.UsuarioFeignClient;

@Service
public class UsuarioService implements UserDetailsService{
	
	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private UsuarioFeignClient cliente;

	//se encarga de autenticar.
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = cliente.buscarPorNombre(username);
		
		if( usuario==null) {
			log.error("Error en el login, no existe el usuario '"+username+"' en el sistema ");
			throw new UsernameNotFoundException("Error en el login, no existe el usuario '"+username+"' en el sistema ");
		}
		
		List<GrantedAuthority> authorities = usuario.getRoles()
				.stream().map(r->new SimpleGrantedAuthority(r.getNombre()))
				.peek(a->log.info(a.getAuthority()))
				.collect(Collectors.toList());
		
		log.info("Usuario autenticado: "+username);
		
		return new User(usuario.getUsername(), usuario.getPassword(), authorities);
	}

}
