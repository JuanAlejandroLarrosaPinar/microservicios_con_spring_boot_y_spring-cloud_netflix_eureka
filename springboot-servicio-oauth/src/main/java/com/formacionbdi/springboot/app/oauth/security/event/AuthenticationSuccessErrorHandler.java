package com.formacionbdi.springboot.app.oauth.security.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import com.formacionbdi.springboot.app.commons.usuarios.models.entity.Usuario;
import com.formacionbdi.springboot.app.oauth.services.IUsuarioService;

@Component
public class AuthenticationSuccessErrorHandler implements AuthenticationEventPublisher{

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IUsuarioService usuarioService;
	
	@Override
	public void publishAuthenticationSuccess(Authentication authentication) {
		if(authentication.getDetails() instanceof WebAuthenticationDetails) {
			return ;
		}
		UserDetails user = (UserDetails)authentication.getPrincipal();
		logger.info("Success login: "+ user.getUsername());
		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		if(usuario!=null) {
			if(usuario.getIntentos()!=null && usuario.getIntentos()>0) {
				usuario.setIntentos(0);
				usuarioService.update(usuario, usuario.getId());
			}
			
		}
		
	}

	@Override
	public void publishAuthenticationFailure(AuthenticationException exception, Authentication authentication) {
		logger.error("Error en el login:" + exception.getMessage());
		
		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		if(usuario!=null) {
			if(usuario.getIntentos()==null) {
				usuario.setIntentos(0);
			}
			usuario.setIntentos(usuario.getIntentos()+1);
			if(usuario.getIntentos()>=3) {
				usuario.setEnabled(false);
			}
			usuarioService.update(usuario, usuario.getId());
		}
		
		
		
	}

	
}
