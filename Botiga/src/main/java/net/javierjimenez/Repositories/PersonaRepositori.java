package net.javierjimenez.Repositories;

import org.springframework.data.repository.CrudRepository;

import net.javierjimenez.Models.Usuari;

public interface PersonaRepositori extends CrudRepository<Usuari, String>{

	public Usuari findByNom(String Nom);
	
}
