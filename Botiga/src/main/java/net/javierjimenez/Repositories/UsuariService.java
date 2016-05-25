package net.javierjimenez.Repositories;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Service;

import net.javierjimenez.Models.Usuari;

@Service
public class UsuariService {

	@Autowired
	UsuariRepositori user;

	public Usuari crearUsuari(String username, String password, String email, String address, boolean esAdmin) {

		if (user.findByNom(username) != null)
			return null;
		if (user.findByEmail(email) != null)
			return null;

		Usuari newUser = new Usuari();
		newUser.setNom(username);
		newUser.setPassword(base64Encode(password));
		newUser.setEmail(email);
		newUser.setDireccion(base64Encode(address));
		newUser.setEsAdmin(esAdmin);

		return user.save(newUser);
	}

	public void eliminarUsuari(String id){
		
		Usuari del = user.findById(id);
		
		user.delete(del);
	}
	
	public Usuari crearAdmin(String username, String password, String email, boolean esAdmin) {

		if (user.findByNom(username) != null)
			return null;
		if (user.findByEmail(email) != null)
			return null;

		List<String> roles = Arrays.asList("ROLE_USER", "ROLE_ADMIN");
		Usuari newAdmin = new Usuari(username, email, base64Encode(password), roles);
		newAdmin.setEsAdmin(esAdmin);

		return user.save(newAdmin);
	}

	public Usuari buscaUsuari(String nom) {
		return user.findByNom(nom);
	}

	public List<Usuari> listUsuaris(String rol) {

		List<Usuari> usuarios = (List<Usuari>) user.findAll();
		Iterator<Usuari> userIterator = usuarios.iterator();
		
		if(rol == "ROLE_ADMIN"){
			while (userIterator.hasNext()) {
				if (!userIterator.next().getRoles().contains(rol)) {
					userIterator.remove();
				}
			}
		} else {
			while (userIterator.hasNext()) {		
				if (userIterator.next().getRoles().contains("ROLE_ADMIN")) {
					userIterator.remove();
				}
			}
		}
		return usuarios;
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

	public void editInfo(Usuari usuari){
		user.save(usuari);
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