package net.javierjimenez.Repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import net.javierjimenez.Models.Producte;

public interface ProducteRepositori extends PagingAndSortingRepository<Producte, String>{

	public Producte findByNom(String nom);
	
	public Page<Producte> findByNom(String nom, Pageable page);
	
	public Producte findById(String id);
	
	public List<Producte> findByPlataforma(String plataforma);
	
	public List<Producte> findByGenero(String genero);
	
	public List<Producte> findByDistribuidora(String distribuidora);
	
	
	
}
