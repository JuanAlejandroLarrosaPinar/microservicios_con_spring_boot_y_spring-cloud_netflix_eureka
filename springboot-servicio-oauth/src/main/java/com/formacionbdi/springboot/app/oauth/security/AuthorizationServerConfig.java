package com.formacionbdi.springboot.app.oauth.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@RefreshScope
@Configuration
@EnableAuthorizationServer 
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter{

	@Autowired
	private BCryptPasswordEncoder bcryptPasswordEncoder;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private InfoAdicionalToken infoAdicionalToken;
	
	@Autowired
	private Environment env;
	
	@Override
	//1-aquí configuramos el authenticationManager y local store (para que sea jwt) y el converter (componente)
	//para guardar los datos del usuario en el token.
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
		tokenEnhancerChain.setTokenEnhancers(Arrays.asList(infoAdicionalToken, accessTokenConverter()));
		endpoints.authenticationManager(authenticationManager).
		accessTokenConverter(accessTokenConverter())
		.tokenStore(tokenStore())
		.tokenEnhancer(tokenEnhancerChain);
	}
	
	@Bean
	public JwtTokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}

	//tiene que ser público (por el @Bean)
	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();
		tokenConverter.setSigningKey(env.getProperty("config.security.oauth.jwt.key"));
		return tokenConverter;
	}
	
	//2-clientes (clientes que llamarán a nuestros servicios)
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.
		inMemory().
		withClient(env.getProperty("config.security.oauth.client.id")).
		secret( this.bcryptPasswordEncoder.encode(env.getProperty("config.security.oauth.client.secret"))).
		scopes("read","write"). //especificamos lo que puede hacer el usuario
		authorizedGrantTypes("password", "refresh_token"). //cuando es con credenciales (cuando nuestros usuarios están en bbdd)
		accessTokenValiditySeconds(3600).
		refreshTokenValiditySeconds(3600)
		/*
		.and().inMemory().
		withClient("androidapp").
		secret( this.bcryptPasswordEncoder.encode("12345")).
		scopes("read","write"). //especificamos lo que puede hacer el usuario
		authorizedGrantTypes("password", "refresh_token"). //cuando es con credenciales (cuando nuestros usuarios están en bbdd)
		accessTokenValiditySeconds(3600).
		refreshTokenValiditySeconds(3600)
		*/
		;
	}

	//3-permiso que van a tener nuestros endpoints
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.tokenKeyAccess("permitAll()").
		checkTokenAccess("isAuthenticated()");
	}
	
	
}
