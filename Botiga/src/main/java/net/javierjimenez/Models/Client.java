package net.javierjimenez.Models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "clients")
public class Client {

	@Id
	private String id_persona;
	
	private String nombre;
	private String email;
	private String password;
	private String direccion;
	
	public Client(){
		
	}
		
	public Client(String n, String e, String p, String d){
		nombre = n;
		email = e;
		password = p;
		direccion = d;
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
}
