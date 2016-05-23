package net.javierjimenez.Models;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.codec.Base64;

@Document(collection = "users")
public class Usuari{

	@Id
	private String id_persona;

	private String nom;
	private String email;
	private String password;
	private String direccion;
	private List<String> roles;
	private List<Carrito> compras;

	public Usuari() {
		roles = Arrays.asList("ROLE_USER");
	}

	public Usuari(String n, String e, String p, List<String> r) {
		nom = n;
		email = e;
		password = p;
		direccion = "";
		roles = r;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getId_persona() {
		return id_persona;
	}

	public void setId_persona(String _id) {
		this.id_persona = _id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDireccion() {
		
		byte[] decodedBytes = Base64.decode(direccion.getBytes());
		return new String(decodedBytes, Charset.forName("UTF-8"));
	}

	public void setDireccion(String direccion) {
		
		this.direccion = direccion;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public List<Carrito> getCompras() {
		return compras;
	}

	public void setCompras(List<Carrito> compras) {
		this.compras = compras;
	}
	
	public void afegirCarrito(Carrito compra){
		this.compras.add(compra);
	}

	public List<GrantedAuthority> getAutorizacion() {
		List<GrantedAuthority> authorityList = new ArrayList<>();
		for (String role : roles) {
			authorityList.add(new SimpleGrantedAuthority(role));
		}
		return authorityList;
	}
}
