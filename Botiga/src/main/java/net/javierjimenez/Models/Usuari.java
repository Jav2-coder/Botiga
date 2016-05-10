package net.javierjimenez.Models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Document(collection = "users")
public class Usuari {

	@Id
	private String id_persona;

	private String nom;
	private String email;
	private String password;
	private String direccion;
	private List<String> roles;

	public Usuari() {
		roles = Arrays.asList("ROLE_USER");
	}

	public Usuari(String n, String e, String p, String d, List<String> r) {
		nom = n;
		email = e;
		password = p;
		direccion = d;
		roles = r;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getId_persona() {
		return id_persona;
	}

	public void setId_persona(String _id) {
		this.id_persona = _id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public List<String> getRol() {
		return roles;
	}

	public void setRol(List<String> r) {
		this.roles = r;
	}

	public List<GrantedAuthority> getAutorizacion() {
		List<GrantedAuthority> authorityList = new ArrayList<>();
		for (String role : roles) {
			authorityList.add(new SimpleGrantedAuthority(role));
		}
		return authorityList;
	}
}
