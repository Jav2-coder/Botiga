package net.javierjimenez.Repositories;

import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.javierjimenez.Models.Producte;

@Service
public class ProducteService {

	@Autowired
	ProducteRepositori product;

	public Producte crearProducte(String n, String g, String d, String p, String e, Integer c) {

		if (product.findByNom(n) != null && product.findByPlataforma(p) != null)
			return null;

		Producte newProduct = new Producte();
		newProduct.setNom(n);
		newProduct.setGenero(g);
		newProduct.setDistribuidora(d);
		newProduct.setPlataforma(p);
		newProduct.setEdad(e);
		newProduct.setCantidad(c);

		return product.save(newProduct);

	}

	public Producte buscaProducte(String name) {
		return product.findByNom(name);
	}

	public List<Producte> allProducts() {
		return product.findAll();
	}

	public HashSet<String> listarAllProd(String x) {

		HashSet<String> listProducts = new HashSet<String>();
		List<Producte> products = product.findAll();

		if (x == "genero") {
			for (Producte p : products) {
				listProducts.add(p.getGenero());
			}
		} else if (x == "plataforma") {
			for (Producte p : products) {
				listProducts.add(p.getPlataforma());
			}
		} else {
			for (Producte p : products) {
				listProducts.add(p.getDistribuidora());
			}
		}
		return listProducts;
	}

	public HashSet<String> listarProductos(String x, String y) {

		HashSet<String> listProducts = new HashSet<String>();
		List<Producte> products = product.findAll();

		if (x == "genero") {
			for (Producte p : products) {
				if (p.getGenero() == y) {
					listProducts.add(p.getGenero());
				}
			}
		} else if (x == "plataforma") {
			for (Producte p : products) {
				if (p.getPlataforma() == y) {
					listProducts.add(p.getPlataforma());
				}
			}
		} else {
			for (Producte p : products) {
				if (p.getDistribuidora() == y) {
					listProducts.add(p.getDistribuidora());
				}
			}
		}
		return listProducts;
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