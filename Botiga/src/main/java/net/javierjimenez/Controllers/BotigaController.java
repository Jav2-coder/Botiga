package net.javierjimenez.Controllers;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import net.javierjimenez.Models.Carrito;
import net.javierjimenez.Models.Producte;
import net.javierjimenez.Models.Sell;
import net.javierjimenez.Models.Usuari;
import net.javierjimenez.Repositories.CarritoRepositori;
import net.javierjimenez.Repositories.ProducteRepositori;
import net.javierjimenez.Repositories.ProducteService;
import net.javierjimenez.Repositories.UsuariRepositori;
import net.javierjimenez.Repositories.UsuariService;

@Controller
@SessionAttributes({ "carrito" })
public class BotigaController {

	@Autowired
	ProducteRepositori producteRepositori;

	@Autowired
	UsuariRepositori usuariRepositori;

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

		pagina = producteRepositori.findAll(new PageRequest(page, 4));

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
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	public String eliminar(@PathVariable("id") String id) {

		p_service.eliminarProd(id);

		return "redirect:/dashboard";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping("/listCarts")
	public String listCarts(@RequestParam(required = false) Integer page, Model model) {

		List<Carrito> carritos = null;
		Page<Carrito> paginaCarritos = null;

		if (page == null)
			page = 0;

		paginaCarritos = compra.findAll(new PageRequest(page, 8));
		carritos = paginaCarritos.getContent();

		model.addAttribute("pagina", page);
		model.addAttribute("carritos", carritos);

		return "listCarts";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping("/listClients")
	public String listClients(@RequestParam(required = false) Integer page, Model model) {

		List<Usuari> usuarios = null;
		Page<Usuari> paginaClientes = null;

		if (page == null)
			page = 0;

		paginaClientes = usuariRepositori.findByEsAdmin(false, new PageRequest(page, 8));
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
	@RequestMapping(value = "/downloadProdDB", method = RequestMethod.POST)
	public String downloadProducts() throws IOException {

		CSVWriter writer = new CSVWriter(new FileWriter("productos.csv"), ',');

		List<Producte> todos = p_service.allProducts();

		for (Producte p : todos) {
			String[] lineaCSV = { p.getNom(), p.getGenero(), p.getDistribuidora(), p.getPlataforma(), p.getEdad(),
					Integer.toString(p.getCantidad()), p.getActivado(), Double.toString(p.getPrecio()),
					p.getPortada(), p.getImagenes()[1], p.getImagenes()[2], p.getImagenes()[3]};
			writer.writeNext(lineaCSV);
		}

		writer.close();
		
		return "redirect:/security";
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

		// http://opencsv.sourceforge.net/

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

			String[] imagenes = { "/images/products/" + nextLine[8], "/images/products/" + nextLine[9],
					"/images/products/" + nextLine[10], "/images/products/" + nextLine[11] };

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
					cantidad, precio, nextLine[6], imagenes, 0);

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

		caja = "/images/products/" + caja;
		juego = "/images/products/" + juego;
		escena1 = "/images/products/" + escena1;
		escena2 = "/images/products/" + escena2;

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
				activar, imagenes, 0);

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

		pagina = usuariRepositori.findByEsAdmin(true, new PageRequest(page, 5));

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

	@RequestMapping("/account")
	public String account(Model model) {

		model.addAttribute("generos", p_service.ordenarLista(p_service.listarAllProd("genero")));
		model.addAttribute("distribuidoras", p_service.ordenarLista(p_service.listarAllProd("distribuidora")));
		model.addAttribute("plataformas", p_service.ordenarLista(p_service.listarAllProd("plataforma")));

		String nom = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		Usuari usuariRecuperat = u_service.buscaUsuari(nom);
		model.addAttribute("usuario", usuariRecuperat);

		return "account";
	}

	@RequestMapping(value = "/categoria/{category}/{cat_name}")
	public String categoriaProd(@PathVariable String category, @PathVariable String cat_name, Model model) {

		model.addAttribute("generos", p_service.ordenarLista(p_service.listarAllProd("genero")));
		model.addAttribute("distribuidoras", p_service.ordenarLista(p_service.listarAllProd("distribuidora")));
		model.addAttribute("plataformas", p_service.ordenarLista(p_service.listarAllProd("plataforma")));

		List<Producte> juegos = p_service.buscarProductosCat(category, cat_name, "Si");

		for (Producte p : juegos) {
			if (p.getNom().length() > 20) {
				p.setNom(p.getNom().substring(0, 20) + "...");
			}
		}

		model.addAttribute("vacio", juegos.isEmpty());
		model.addAttribute("juegos", juegos);

		return "category";
	}

	@RequestMapping(value = "/producto/{product_id}", method = RequestMethod.GET)
	public String product(@PathVariable String product_id, Model model) {

		model.addAttribute("generos", p_service.ordenarLista(p_service.listarAllProd("genero")));
		model.addAttribute("distribuidoras", p_service.ordenarLista(p_service.listarAllProd("distribuidora")));
		model.addAttribute("plataformas", p_service.ordenarLista(p_service.listarAllProd("plataforma")));

		Producte product = p_service.buscarProdId(product_id);

		model.addAttribute("product", product);

		return "product";
	}

	@RequestMapping(value = "/new_venta/{id}", method = RequestMethod.POST)
	public String addShopCart(@ModelAttribute("carrito") Carrito carrito, @PathVariable String id,
			@RequestParam("cantidad") String quantity, Model model) {

		Producte p = p_service.buscarProdId(id);
		Integer cantidad = Integer.parseInt(quantity);

		carrito.addProducto(new Sell(p, cantidad));
		model.addAttribute("carrito", carrito);

		return "redirect:/producto/" + id;
	}

	@RequestMapping("/")
	public String home(Model model) throws UnsupportedEncodingException {

		model.addAttribute("generos", p_service.ordenarLista(p_service.listarAllProd("genero")));
		model.addAttribute("distribuidoras", p_service.ordenarLista(p_service.listarAllProd("distribuidora")));
		model.addAttribute("plataformas", p_service.ordenarLista(p_service.listarAllProd("plataforma")));

		List<Producte> masVendidos = p_service.prodMasVendidos();

		model.addAttribute("juegos", masVendidos);

		return "home";
	}

	@RequestMapping(value = "/checkout", method = RequestMethod.POST)
	public String pedido(@ModelAttribute("carrito") Carrito carrito, SessionStatus status, Model model) {

		model.addAttribute("carrito", carrito);

		return "checkout";
	}

	@RequestMapping(value = "/delCart/{id}", method = RequestMethod.POST)
	public String delSell(@PathVariable String id, @ModelAttribute("carrito") Carrito carrito) {

		for (int i = carrito.getSells().size() - 1; i >= 0; i--) {
			if (carrito.getSells().get(i).getId().equals(id)) {
				carrito.getSells().remove(i);
			}
		}

		if (carrito.getSells().size() == 0) {

			carrito.setTieneCosas(false);

			return "redirect:/";
		}

		return "checkout";
	}

	@RequestMapping(value = "/buyCart", method = RequestMethod.POST)
	public String buyCart(@ModelAttribute("carrito") Carrito carrito) {

		String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

		carrito.setUsername(username);

		compra.save(carrito);

		for (Sell s : carrito.getSells()) {

			Integer restarStock = s.getCantidad();

			Producte p = s.getProducte();
			p.setCantidad(p.getCantidad() - restarStock);
			p.setVentas(p.getVentas() + restarStock);

			if (p.getCantidad() == 0) {
				p.setActivado("No");
			}
			p_service.editProd(p);
		}

		carrito.setTieneCosas(false);

		return "redirect:/compraPagada";
	}

	@RequestMapping("/compraPagada")
	public String compraPagada() {

		return "compra_realizada";
	}

	@RequestMapping("/register")
	public String register() {
		return "register";
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String saveUser(@RequestParam("nombre") String name, @RequestParam("email") String mail,
			@RequestParam("passwd") String password, @RequestParam("direccion") String address, Model model) {

		Usuari result = u_service.crearUsuari(name, password, mail, address, false);

		if (result == null) {

			String error = "Error: El nombre/correo ya existe!";

			model.addAttribute("error", error);
			return "register";
		}
		return "redirect:/";
	}

	@RequestMapping("/contact")
	public String contact(Model model) {

		model.addAttribute("generos", p_service.ordenarLista(p_service.listarAllProd("genero")));
		model.addAttribute("distribuidoras", p_service.ordenarLista(p_service.listarAllProd("distribuidora")));
		model.addAttribute("plataformas", p_service.ordenarLista(p_service.listarAllProd("plataforma")));

		return "contact";
	}

	@RequestMapping("/about")
	public String about(Model model) {

		model.addAttribute("generos", p_service.ordenarLista(p_service.listarAllProd("genero")));
		model.addAttribute("distribuidoras", p_service.ordenarLista(p_service.listarAllProd("distribuidora")));
		model.addAttribute("plataformas", p_service.ordenarLista(p_service.listarAllProd("plataforma")));

		return "about";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginRequest(@RequestParam(value = "error", required = false) String error,
			HttpServletRequest request) {

		if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
			return "redirect:/account";
		}

		return "login";
	}
}