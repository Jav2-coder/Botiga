package net.javierjimenez.Repositories;

import java.nio.charset.Charset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Service;

import net.javierjimenez.Models.Usuari;

@Service
public class UsuariService {

	@Autowired
	UsuariRepositori user;

	public Usuari crearUsuari(String username, String password, String email, String address) {
		
		if (user.findByNom(username) != null) return null;
		
		if (user.findByEmail(email) != null) return null;

		Usuari newUser = new Usuari();
		newUser.setNom(username);
		newUser.setPassword(base64Encode(password));
		newUser.setEmail(email);
		newUser.setDireccion(base64Encode(address));
		
		return user.save(newUser);
	}

	public Usuari buscaUsuari(String nom) {
		return user.findByNom(nom);
	}

	public Usuari identifica(String n, String p) {

		Usuari userFound = user.findByNom(n);
		if (userFound == null) {
			return null;
		}

		if (!p.equals(base64Decode(userFound.getPassword()))) {
			return null;
		}

		return userFound;
	}

	public static String base64Encode(String token) {
		byte[] encodedBytes = Base64.encode(token.getBytes());
		return new String(encodedBytes, Charset.forName("UTF-8"));
	}

	public static String base64Decode(String token) {
		byte[] decodedBytes = Base64.decode(token.getBytes());
		return new String(decodedBytes, Charset.forName("UTF-8"));
	}
}