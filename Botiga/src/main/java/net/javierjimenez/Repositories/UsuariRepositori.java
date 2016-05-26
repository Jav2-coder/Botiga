package net.javierjimenez.Repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import net.javierjimenez.Models.Usuari;

public interface UsuariRepositori extends PagingAndSortingRepository<Usuari, String>{

	public Usuari findByNom(String Nom);
	
	public List<Usuari> findByEsAdmin(boolean esAdmin);
	
	public Usuari findById(String id);
	
	public Page<Usuari> findByNom(String nom, Pageable page);
	
	public Page<Usuari> findByEsAdmin(boolean admin, Pageable page);
	
	public Usuari findByEmail(String Email);
	
}
