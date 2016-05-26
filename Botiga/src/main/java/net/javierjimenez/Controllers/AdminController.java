package net.javierjimenez.Controllers;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import au.com.bytecode.opencsv.CSVReader;
import net.javierjimenez.Models.Carrito;
import net.javierjimenez.Models.Producte;
import net.javierjimenez.Models.Usuari;
import net.javierjimenez.Repositories.CarritoRepositori;
import net.javierjimenez.Repositories.ProducteService;
import net.javierjimenez.Repositories.UsuariService;

@Controller
public class AdminController {

	@Autowired
	CarritoRepositori compra;

	@Autowired
	UsuariService u_service;

	@Autowired
	ProducteService p_service;

	@ModelAttribute("carrito")
	public Carrito getCarrito() {
		return new Carrito();
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping("/dashboard")
	public String dashboard(@RequestParam(required = false) String keyword,
			@RequestParam(required = false) Integer page, Model model) {

		List<Producte> productos = null;
		Page<Producte> pagina = null;

		model.addAttribute("generos", p_service.ordenarLista(p_service.listarAllProd("genero")));
		model.addAttribute("distribuidoras", p_service.ordenarLista(p_service.listarAllProd("distribuidora")));
		model.addAttribute("plataformas", p_service.ordenarLista(p_service.listarAllProd("plataforma")));

		if (page == null)
			page = 0;

		pagina = p_service.paginarProductos(new PageRequest(page, 4));
		productos = pagina.getContent();

		model.addAttribute("pagina", page);
		model.addAttribute("juegos", productos);

		return "dashboard";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/editProd", method = RequestMethod.POST)
	public String editar(@RequestParam("_hidden_id") String id, @RequestParam("name") String nombre,
			@RequestParam("price") String price, @RequestParam("total") String quantity,
			@RequestParam("generos") String genero, @RequestParam("distribuidoras") String distribuidora,
			@RequestParam("plataformas") String plataforma, @RequestParam("edad") String edad,
			@RequestParam("activar") String activar, Model model, final RedirectAttributes redirectAttributes) {

		Double precio = null;
		Integer cantidad = null;

		try {

			precio = Double.parseDouble(price);
			cantidad = Integer.parseInt(quantity);

		} catch (Exception e) {

			String error = "NOPE";
			redirectAttributes.addFlashAttribute("error_number", error);

			return "redirect:/dashboard";
		}

		Producte p = p_service.buscarProdId(id);
		p.setNom(nombre);
		p.setPrecio(precio);
		p.setCantidad(cantidad);
		p.setGenero(genero);
		p.setDistribuidora(distribuidora);
		p.setPlataforma(plataforma);
		p.setEdad(edad);
		p.setActivado(activar);

		p_service.editProd(p);

		return "redirect:/dashboard";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/delCompra/{id}", method = RequestMethod.POST)
	public String eliminarCompra(@PathVariable("id") String id) {

		Carrito delCarro = compra.findById(id);
		compra.delete(delCarro);

		return "redirect:/listCarts";
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	public String eliminar(@PathVariable("id") String id) {

		p_service.eliminarProd(id);

		return "redirect:/dashboard";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping("/listCarts")
	public String listCarts(@RequestParam(required = false) Integer page, Model model) {

		List<Carrito> compras = null;
		Page<Carrito> paginaCarritos = null;

		if (page == null)
			page = 0;

		paginaCarritos = compra.findAll(new PageRequest(page, 8));
		compras = paginaCarritos.getContent();

		model.addAttribute("pagina", page);
		model.addAttribute("carritos", compras);

		return "listCarts";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping("/listClients")
	public String listClients(@RequestParam(required = false) Integer page, Model model) {

		List<Usuari> usuarios = null;
		Page<Usuari> paginaClientes = null;

		if (page == null)
			page = 0;

		paginaClientes = u_service.esAdmin(false, new PageRequest(page, 8));
		usuarios = paginaClientes.getContent();

		model.addAttribute("pagina", page);
		model.addAttribute("usuarios", usuarios);

		return "listClients";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/delClient/{id}", method = RequestMethod.POST)
	public String delClient(@PathVariable("id") String id) {

		u_service.eliminarUsuari(id);

		return "redirect:/listClients";

	}

	@Secured("ROLE_ADMIN")
	@RequestMapping("/security")
	public String security() {

		return "security";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping("/addproduct")
	public String newProd(Model model) {

		model.addAttribute("generos", p_service.ordenarLista(p_service.listarAllProd("genero")));
		model.addAttribute("distribuidoras", p_service.ordenarLista(p_service.listarAllProd("distribuidora")));
		model.addAttribute("plataformas", p_service.ordenarLista(p_service.listarAllProd("plataforma")));

		return "new_product";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/uploadcsv", method = RequestMethod.POST)
	public String saveCSV(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes, Model model)
			throws IOException {

		if (!file.getOriginalFilename().matches(".+\\.csv$")) {

			return "redirect:/";
		}

		InputStream f = file.getInputStream();
		CSVReader reader = new CSVReader(new InputStreamReader(f), ',');

		String[] nextLine;
		while ((nextLine = reader.readNext()) != null) {

			if (nextLine.length > 12 || nextLine.length < 12) {

				reader.close();

				return "new_product";
			}

			String[] imagenes = { nextLine[8], nextLine[9], nextLine[10], nextLine[11] };

			Double precio = null;
			Integer cantidad = null;

			try {

				precio = Double.parseDouble(nextLine[7]);
				cantidad = Integer.parseInt(nextLine[5]);

			} catch (Exception e) {

				reader.close();
				String error = "NOPE";
				model.addAttribute("error_number", error);

				return "new_product";
			}

			for (String img : imagenes) {
				if (!img.contains(".")) {
					reader.close();
					String error = "NOPE";
					model.addAttribute("error_img", error);
					return "new_product";
				}
			}

			if (!nextLine[6].equals("Si") && !nextLine[6].equals("No")) {

				reader.close();
				return "new_product";
			}

			Producte newProd = p_service.crearProducte(nextLine[0], nextLine[1], nextLine[2], nextLine[3], nextLine[4],
					cantidad, precio, nextLine[6], imagenes, 0L);

			if (newProd == null) {
				reader.close();
				String error = "NOPE";
				model.addAttribute("error_product", error);
				return "new_product";
			}
		}

		reader.close();

		return "redirect:/dashboard";

	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/singleProd", method = RequestMethod.POST)
	public String saveProduct(@RequestParam("name") String nombre, @RequestParam("price") String price,
			@RequestParam("total") String quantity, @RequestParam("generos") String genero,
			@RequestParam("distribuidoras") String distribuidora, @RequestParam("plataformas") String plataforma,
			@RequestParam("edad") String edad, @RequestParam("caja") String caja, @RequestParam("juego") String juego,
			@RequestParam("escena1") String escena1, @RequestParam("escena2") String escena2,
			@RequestParam("activar") String activar, Model model) {

		model.addAttribute("generos", p_service.ordenarLista(p_service.listarAllProd("genero")));
		model.addAttribute("distribuidoras", p_service.ordenarLista(p_service.listarAllProd("distribuidora")));
		model.addAttribute("plataformas", p_service.ordenarLista(p_service.listarAllProd("plataforma")));

		String[] imagenes = { caja, juego, escena1, escena2 };

		Double precio = null;
		Integer cantidad = null;

		try {

			precio = Double.parseDouble(price);
			cantidad = Integer.parseInt(quantity);

		} catch (Exception e) {

			String error = "NOPE";
			model.addAttribute("error_number", error);
			return "new_product";
		}

		for (String img : imagenes) {
			if (!img.contains(".")) {
				String error = "NOPE";
				model.addAttribute("error_img", error);
				return "new_product";
			}
		}

		Producte newProd = p_service.crearProducte(nombre, genero, distribuidora, plataforma, edad, cantidad, precio,
				activar, imagenes, 0L);

		if (newProd == null) {
			String error = "NOPE";
			model.addAttribute("error_product", error);
			return "new_product";
		}

		return "redirect:/dashboard";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/listAdmin")
	public String newAdmin(@RequestParam(required = false) Integer page, Model model) {

		List<Usuari> admins = null;
		Page<Usuari> pagina = null;

		if (page == null)
			page = 0;

		pagina = u_service.esAdmin(true, new PageRequest(page, 5));
		admins = pagina.getContent();

		model.addAttribute("pagina", page);
		model.addAttribute("admins", admins);

		return "newAdmin";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/newAdmin", method = RequestMethod.POST)
	public String saveAdmin(@RequestParam("username") String name, @RequestParam("passwd") String password,
			@RequestParam("email") String email, Model model) {

		Usuari result = u_service.crearAdmin(name, password, email, true);

		if (result == null) {

			String error = "NOPE";

			model.addAttribute("error", error);
			return "newAdmin";
		}

		return "redirect:/dashboard";
	}
}