package net.javierjimenez.Repositories;

import org.springframework.beans.factory.annotation.Autowired;

import net.javierjimenez.Models.Producte;

public class ProducteService {
	
	@Autowired
	ProducteRepositori product;

	public Producte crearProducte(String n, String g, String d, String p, String e, Integer c){
		
		if(product.findByNom(n) != null && product.findByPlataforma(p) != null) return null;
		
		Producte newProduct = new Producte();
		newProduct.setNom(n);
		newProduct.setGenero(g);
		newProduct.setDistribuidor(d);
		newProduct.setPlataforma(p);
		newProduct.setEdad(e);
		newProduct.setCantidad(c);
		
		return product.save(newProduct);
		
	}
	
	public Producte buscaProducte(String nom) {
		return product.findByNom(nom);
	}
	
	public static boolean isNumeric(String cadena){
		try {
			Integer.parseInt(cadena);
			return true;
		} catch (NumberFormatException nfe){
			return false;
		}
	}
}