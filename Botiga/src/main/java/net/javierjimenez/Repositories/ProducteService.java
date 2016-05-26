package net.javierjimenez.Repositories;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import net.javierjimenez.Models.Producte;

@Service
public class ProducteService {

	@Autowired
	ProducteRepositori product;

	public Producte crearProducte(String n, String g, String d, String p, String e, Integer c, Double m, String a,
			String[] i, Long v) {

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
		newProduct.setPortada(i[0]);
		newProduct.setImagenes(i);
		newProduct.setVentas(v);

		return product.save(newProduct);

	}

	public long countProductos(String nombre) {

		if (nombre != null) {
			return product.countByNomContainingIgnoreCase(nombre);
		} else {
			return product.count();
		}
	}

	public List<Producte> todosProductosPagina(int pagina, int i) {
		return null;
	}

	public List<Producte> buscarNombreProductoPagina(String nombre, int pagina, int productosPagina) {

		Page<Producte> pagProductos = product.findByNomContainingIgnoreCase(nombre,
				new PageRequest(pagina, productosPagina));

		return pagProductos.getContent();
	}

	public Page<Producte> paginarProductos(PageRequest pageRequest) {

		return product.findAll(pageRequest);
	}

	public Producte buscaProducte(String name) {
		return product.findByNom(name);
	}

	public Producte buscarProdId(String id) {
		return product.findById(id);
	}

	public List<Producte> prodMasVendidos() {

		List<Producte> p = product.findTop4ByOrderByVentasDesc();

		return p;

	}

	public void editProd(Producte p) {
		product.save(p);
	}

	public void eliminarProd(String id) {
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

	public List<Producte> buscarProductosCat(String category, String cat_name, String activo) {

		List<Producte> lista = null;

		if (category.equals("Genero")) {
			lista = product.findByGenero(cat_name);

			for (int i = lista.size() - 1; i >= 0; i--) {
				if (!lista.get(i).getActivado().equals(activo)) {
					lista.remove(i);
				}
			}
		} else if (category.equals("Plataforma")) {
			lista = product.findByPlataforma(cat_name);
			for (int i = lista.size() - 1; i >= 0; i--) {
				if (!lista.get(i).getActivado().equals(activo)) {
					lista.remove(i);
				}
			}
		} else {
			lista = product.findByDistribuidora(cat_name);
			for (int i = lista.size() - 1; i >= 0; i--) {
				if (!lista.get(i).getActivado().equals(activo)) {
					lista.remove(i);
				}
			}
		}

		return lista;
	}

	public List<String> ordenarLista(HashSet<String> juegos) {

		List<String> listaOrdenada = new ArrayList<String>();

		for (String juego : juegos) {
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