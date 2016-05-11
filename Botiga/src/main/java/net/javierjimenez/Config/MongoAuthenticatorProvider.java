package net.javierjimenez.Config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import net.javierjimenez.Models.Usuari;
import net.javierjimenez.Repositories.UsuariService;

@Component
public class MongoAuthenticatorProvider implements AuthenticationProvider {

	@Autowired
	UsuariService userService;

	public MongoAuthenticatorProvider() {
		super();
	}

	protected void additionalAuthenticationChecks(UserDetails arg0, UsernamePasswordAuthenticationToken arg1)
			throws AuthenticationException {
		// TODO Auto-generated method stub

	}

	@Override
	public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
		String nomUsuari = authentication.getName();
		String textContrasenya = authentication.getCredentials().toString();
		// No ha posat la contrasenya
		if (!StringUtils.hasText(textContrasenya)) {
			return null;
		}
		// Localitza l'usuari
		Usuari usuariIdentificat = userService.identifica(nomUsuari, textContrasenya);
		if (usuariIdentificat == null) {

			return null;
		}

		List<GrantedAuthority> grantedAuths = usuariIdentificat.getAutorizacion();

		Authentication auth = new UsernamePasswordAuthenticationToken(usuariIdentificat.getNom(),
				usuariIdentificat.getPassword(), grantedAuths);
		return auth;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}