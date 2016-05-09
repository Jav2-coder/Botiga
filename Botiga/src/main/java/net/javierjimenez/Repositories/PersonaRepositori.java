package net.javierjimenez.Repositories;

import org.springframework.data.repository.CrudRepository;

import net.javierjimenez.Models.Client;

public interface PersonaRepositori extends CrudRepository<Client, String>{

	public Client findByNom(String Nom);
	
}
