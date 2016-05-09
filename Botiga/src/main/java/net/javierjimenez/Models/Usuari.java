package net.javierjimenez.Models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class Usuari {

	@Id
	private String id_persona;
	
	private String nombre;
	private String email;
	private String password;
	private String direccion;
	private Integer rol;
	
	public Usuari(){	
	}
		
	public Usuari(String n, String e, String p, String d, Integer r){
		nombre = n;
		email = e;
		password = p;
		direccion = d;
		rol = r;
	}

	public String getNom() {
		return nombre;
	}

	public void setNom(String nom) {
		this.nombre = nom;
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

	public Integer getRol() {
		return rol;
	}

	public void setRol(Integer r) {
		this.rol = r;
	}
}
