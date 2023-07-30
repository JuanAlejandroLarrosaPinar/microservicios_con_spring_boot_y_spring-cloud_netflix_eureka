package com.formacionbdi.springboot.app.zuul.oauth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter{
	
	
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.tokenStore(tokenStore()); 
	}
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
//		http.authorizeRequests().antMatchers("/api/security/oauth/token").permitAll() //todo el mundo se puede authenticar
//		 .antMatchers(HttpMethod.GET, "/api/productos/listar", "/api/item/listar"," /api/usuarios/usuarios").permitAll()
//		 .antMatchers(HttpMethod.GET, "/api/productos/ver/{id}", "/api/item/ver/{id}/cantidad/{cantidad}").hasAnyRole("ADMIN","USER")
//		 .antMatchers(HttpMethod.POST,"/api/productos/crear","/api/item/crear","/api/usuarios/usuarios").hasRole("ADMIN")
//		 .antMatchers(HttpMethod.PUT,"/api/productos/editar/{id}","/api/item/crear/{id}","/api/usuarios/usuarios/{id}").hasRole("ADMIN")
//		 .antMatchers(HttpMethod.DELETE,"/api/productos/eliminar/{id}","/api/item/eliminar/{id}","/api/usuarios/usuarios/{id}").hasRole("ADMIN")
//		 ; 
		
		//podríamos resumir a lo siguiente
		http.authorizeRequests().antMatchers("/api/security/oauth/token").permitAll() //todo el mundo se puede authenticar
		 .antMatchers(HttpMethod.GET, "/api/productos/listar", "/api/item/listar","/api/usuarios/usuarios").permitAll()
		 .antMatchers(HttpMethod.GET, "/api/productos/ver/{id}", "/api/item/ver/{id}/cantidad/{cantidad}").hasAnyRole("ADMIN","USER")
		 .antMatchers("/api/productos/**","/api/item/**","/api/usuarios/**").hasRole("ADMIN")
		 .anyRequest().authenticated() //requiere autenticación cualquier otra petición
		 ; 
	}
	
	@Bean
	public JwtTokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();
		tokenConverter.setSigningKey("algun_codigo_secreto_aeiou");
		return tokenConverter;
	}
}
