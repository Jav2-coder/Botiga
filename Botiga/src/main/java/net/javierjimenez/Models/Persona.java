package net.javierjimenez.Models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "prova")
public class Persona {

	@Id
	private String id_persona;
	
	public String nom;
	
	public Persona(){
		
	}
		
	public Persona(String n){
		this.nom = n;
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
}
