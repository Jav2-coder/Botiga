package net.javierjimenez.Repositories;

import org.springframework.data.repository.CrudRepository;

import net.javierjimenez.Models.Persona;

public interface PersonaRepositori extends CrudRepository<Persona, String>{

	public Persona findByNom(String Nom);
	
}
