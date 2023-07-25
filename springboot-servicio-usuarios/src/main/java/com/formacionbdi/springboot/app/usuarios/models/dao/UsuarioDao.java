package com.formacionbdi.springboot.app.usuarios.models.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.formacionbdi.springboot.app.usuarios.models.entity.Usuario;

public interface UsuarioDao extends PagingAndSortingRepository<Usuario, Long>{

	public Usuario findByUsernameAndEmail(String username, String email);
}
