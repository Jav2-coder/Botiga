package net.javierjimenez.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import net.javierjimenez.Models.Producte;

public interface ProducteRepositori extends MongoRepository<Producte, String>{

	public Producte findByNom(String nom);
	
	public Producte findByPlataforma(String plataforma);
	
}
