package net.javierjimenez.Repositories;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.javierjimenez.Models.Producte;

@Service
public class ProducteService {

	@Autowired
	ProducteRepositori product;

	public Producte crearProducte(String n, String g, String d, String p, String e, Integer c, Double m, String a, String i) {

		if (product.findByNom(n) != null && product.findByPlataforma(p) != null)
			return null;

		Producte newProduct = new Producte();
		newProduct.setNom(n);
		newProduct.setGenero(g);
		newProduct.setDistribuidora(d);
		newProduct.setPlataforma(p);
		newProduct.setEdad(e);
		newProduct.setCantidad(c);
		newProduct.setActivado(a);
		newProduct.setPrecio(m);
		newProduct.setImagen(i);

		return product.save(newProduct);

	}

	public Producte buscaProducte(String name) {
		return product.findByNom(name);
	}

	public Producte buscarProdId(String id) {
		return product.findById(id);
	}
	
	public void editarProd(String id, String nombre, Double precio, Integer cantidad, String genero,
			String distribuidora, String plataforma, String edad, String activar) {
		
		Producte p = product.findById(id);
		
		p.setNom(nombre);
		p.setPrecio(precio);
		p.setCantidad(cantidad);
		p.setGenero(genero);
		p.setDistribuidora(distribuidora);
		p.setPlataforma(plataforma);
		p.setEdad(edad);
		p.setActivado(activar);
		
		product.save(p);
		
	}
	
	public void eliminarProd(String id){
		Producte p = product.findById(id);
		product.delete(p);
	}
	
	public List<Producte> allProducts() {
		return (List<Producte>) product.findAll();
	}

	public HashSet<String> listarAllProd(String x) {

		List<Producte> products = (List<Producte>) product.findAll();
		HashSet<String> listaOrdenada = new HashSet<String>();

		if (x == "genero") {
			for (Producte p : products) {
				listaOrdenada.add(p.getGenero());
			}
		} else if (x == "plataforma") {
			for (Producte p : products) {
				listaOrdenada.add(p.getPlataforma());
			}
		} else {
			for (Producte p : products) {
				listaOrdenada.add(p.getDistribuidora());
			}
		}
		
		return listaOrdenada;
	}

	public List<Producte> buscarProductosCat(String category, String cat_name) {

		List<Producte> lista = null;

		if (category.equals("Genero")) {	
			lista = product.findByGenero(cat_name);
		} else if (category.equals("Plataforma")) {
			lista = product.findByPlataforma(cat_name);
		} else {
			lista = product.findByDistribuidora(cat_name);
		}
		
		return lista;
	}
	
	public List<String> ordenarLista(HashSet<String> juegos){
		
		List<String> listaOrdenada = new ArrayList<String>();
		
		for(String juego : juegos){
			listaOrdenada.add(juego);
		}
		
		Collections.sort(listaOrdenada);
		
		return listaOrdenada;
		
	}

	public static boolean isNumeric(String cadena) {
		try {
			Double.parseDouble(cadena);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}
}