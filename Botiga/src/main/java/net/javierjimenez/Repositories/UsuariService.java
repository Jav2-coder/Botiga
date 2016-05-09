package net.javierjimenez.Repositories;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import net.javierjimenez.Models.Usuari;

public interface UsuariService extends CrudRepository<Usuari, String> {

	public Usuari findByNom(String Nom);

	public default List<GrantedAuthority> getPermisos(Integer role) {
		List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();

		switch (role.intValue()) {

		case 0:
			authList.add(new SimpleGrantedAuthority("ROLE_USER"));
			break;
		case 1:
			authList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		}

		return authList;
	}
}
