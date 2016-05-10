package net.javierjimenez.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import net.javierjimenez.Models.Usuari;

public interface UsuariRepositori extends MongoRepository<Usuari, String>{

	public Usuari findByNom(String Nom);
	
	public Usuari findByEmail(String Email);
	
}
