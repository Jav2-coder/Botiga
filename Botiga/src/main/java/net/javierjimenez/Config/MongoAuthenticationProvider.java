package net.javierjimenez.Config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import net.javierjimenez.Models.Usuari;
import net.javierjimenez.Repositories.UsuariService;

@Component
public class MongoAuthenticationProvider extends
		AbstractUserDetailsAuthenticationProvider {

	@Autowired 
	 UsuariService userService;
	
	@Override
	protected void additionalAuthenticationChecks(UserDetails arg0, UsernamePasswordAuthenticationToken arg1)
			throws AuthenticationException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Retorna un usuari a partir de les credencials que se li han 
	 * passat a través de Spring Security
	 */
	@Override
	protected UserDetails retrieveUser(String username,
			UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {

		// Obtenir el que cal per fer les comprovacions
		String password = (String) authentication.getCredentials();
		if (!StringUtils.hasText(password)) {
			
			throw new BadCredentialsException("No es permeten els usuaris sense contrasenya");
		}
		
		// Comprovar que l'usuari existeix i que té la contrasenya correcta
		Usuari user = userService.findByNom(username);
		if (user == null) {
			
			throw new UsernameNotFoundException("Invalid Login");
		}
		
		if (!password.equals(user.getPassword())) {
			
			throw new BadCredentialsException("Invalid Login");
		}
		
		final List<GrantedAuthority> auths = userService.getPermisos(user.getRol());	
		
		return new User(username, password, 
				true, // enabled
				true, // account not expired
				true, // credentials not expired
				true, // account not locked
				auths);
						
	}
}
