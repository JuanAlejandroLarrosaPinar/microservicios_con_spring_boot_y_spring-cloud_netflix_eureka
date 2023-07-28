package com.formacionbdi.springboot.app.usuarios;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;

import com.formacionbdi.springboot.app.commons.usuarios.models.entity.Role;
import com.formacionbdi.springboot.app.commons.usuarios.models.entity.Usuario;

//Esta clase sirve para mostrar los IDs de las clases Usuario y Role, ya que con la anotación 
//@RepositoryRestResource y @RestResource no se muestran por defecto.
@Configuration
public class RepositoryConfig implements RepositoryRestConfigurer{

	
	@Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
		config.exposeIdsFor(Usuario.class, Role.class);
	}
}
