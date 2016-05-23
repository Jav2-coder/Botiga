package net.javierjimenez.Repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import net.javierjimenez.Models.Usuari;

public interface UsuariRepositori extends PagingAndSortingRepository<Usuari, String>{

	public Usuari findByNom(String Nom);
	
	public Page<Usuari> findByNom(String nom, Pageable page);
	
	public Page<Usuari> findByEsAdmin(boolean admin, Pageable page);
	
	public Usuari findByEmail(String Email);
	
}
