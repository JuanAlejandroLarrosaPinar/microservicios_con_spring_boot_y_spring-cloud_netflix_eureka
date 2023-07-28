package com.formacionbdi.springboot.app.commons.usuarios.models.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="usuarios")
@Getter
@Setter
public class Usuario implements Serializable{

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true, length=20)
	private String username;
	
	@Column(length=60)
	private String password;
	
	private Boolean enabled;
	private String nombre;
	private String apellido;
	
	@Column(unique= true, length=20)
	private String email;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "usuarios_to_roles" 
		, joinColumns = @JoinColumn(name="usuario_id")
		, inverseJoinColumns = @JoinColumn(name="role_id")
		, uniqueConstraints =  {@UniqueConstraint(columnNames = {"usuario_id", "role_id"})}
	)
	
	private List<Role> roles;
	
	private static final long serialVersionUID = 4002221912401133094L;
	

}
