package net.javierjimenez.Controllers;

/*import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;*/
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import net.javierjimenez.Models.Producte;
import net.javierjimenez.Models.Usuari;
import net.javierjimenez.Repositories.ProducteRepositori;
import net.javierjimenez.Repositories.ProducteService;
import net.javierjimenez.Repositories.UsuariRepositori;
import net.javierjimenez.Repositories.UsuariService;

@Controller
public class BotigaController {

	ProducteRepositori producteRepositori;
	UsuariRepositori usuarirepositori;

	@Autowired
	UsuariService u_service;

	@Autowired
	ProducteService p_service;

	@Secured("ROLE_ADMIN")
	@RequestMapping("/dashboard")
	public String dashboard(@RequestParam(required = false) String keyword,
			@RequestParam(required = false) Integer page, Model model) {

		/*
		 * List<Producte> productos = null; Page<Producte> pagina = null;
		 */

		model.addAttribute("juegos", p_service.allProducts());

		model.addAttribute("generos", p_service.ordenarLista(p_service.listarAllProd("genero")));
		model.addAttribute("distribuidoras", p_service.ordenarLista(p_service.listarAllProd("distribuidora")));
		model.addAttribute("plataformas", p_service.ordenarLista(p_service.listarAllProd("plataforma")));


		/*
		 * if (page == null) page = 0;
		 * 
		 * if (keyword == null) {
		 * 
		 * pagina = producteRepositori.findAll(new PageRequest(page, 5));
		 * 
		 * } else {
		 * 
		 * muere aqui -> pagina = producteRepositori.findByNom(keyword, new
		 * PageRequest(page, 5));
		 * 
		 * }
		 * 
		 * productos = pagina.getContent();
		 * 
		 * model.addAttribute("pagina", page); model.addAttribute("productos",
		 * productos);
		 */

		return "dashboard";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	public String eliminar(@PathVariable("id") String id) {

		p_service.eliminarProd(id);

		return "redirect:/dashboard";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/dashboard", method = RequestMethod.POST)
	public String editProduct(@RequestParam("price") String p, @RequestParam("name") String n,
			@RequestParam("total") String t, Model model) {

		model.addAttribute("generos", p_service.ordenarLista(p_service.listarAllProd("genero")));
		model.addAttribute("distribuidoras", p_service.ordenarLista(p_service.listarAllProd("distribuidora")));
		model.addAttribute("plataformas", p_service.ordenarLista(p_service.listarAllProd("plataforma")));

		if (ProducteService.isNumeric(t) && ProducteService.isNumeric(p)) {
			Integer tot = (int) Double.parseDouble(t);
			Double preu = round(Double.parseDouble(p), 2);
			System.out.println(n + " | " + tot + " | " + preu);
		} else {
			System.out.println("Mal: Valor no numérico");
		}

		return "redirect:/dashboard";

	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/listClients", method = RequestMethod.GET)
	public String listClients(Model model) {

		List<Usuari> clientes = u_service.listUsuaris("");

		model.addAttribute("clientes", clientes);

		return "listClients";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/listClients", method = RequestMethod.POST)
	public String delClient(@RequestParam("nom_user") String n) {

		u_service.eliminarUsuari(n);

		return "redirect:/listClients";

	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/addproduct", method = RequestMethod.GET)
	public String newProd() {
		return "new_product";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/addproduct", method = RequestMethod.POST)
	public String saveProduct(@RequestParam("name") String n, @RequestParam("total") String t) {

		if (ProducteService.isNumeric(t)) {
			Integer tot = Integer.parseInt(t);
			System.out.println(n + " | " + tot);
		} else {
			System.out.println("Mal: Valor no numérico");
		}

		return "redirect:/dashboard";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/newAdmin", method = RequestMethod.GET)
	public String newAdmin(Model model) {

		List<Usuari> admins = u_service.listUsuaris("ROLE_ADMIN");

		model.addAttribute("admins", admins);

		return "newAdmin";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/newAdmin", method = RequestMethod.POST)
	public String saveAdmin(@RequestParam("username") String name, @RequestParam("passwd") String password,
			@RequestParam("email") String email, Model model) {

		Usuari result = u_service.crearAdmin(name, password, email);

		if (result == null) {

			String error = "NOPE";

			model.addAttribute("error", error);
			return "newAdmin";
		}
		return "redirect:/dashboard";
	}

	@Secured("ROLE_USER")
	@RequestMapping(value = "/account", method = RequestMethod.GET)
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

		List<Producte> juegos = p_service.buscarProductosCat(category, cat_name);
		
		model.addAttribute("juegos", juegos);
		
		return "category";
	}

	@RequestMapping(value = "/producto/{product_nom}", method = RequestMethod.GET)
	public String product(@PathVariable String product_nom, Model model) {

		model.addAttribute("generos", p_service.ordenarLista(p_service.listarAllProd("genero")));
		model.addAttribute("distribuidoras", p_service.ordenarLista(p_service.listarAllProd("distribuidora")));
		model.addAttribute("plataformas", p_service.ordenarLista(p_service.listarAllProd("plataforma")));

		Producte product = p_service.buscaProducte(product_nom);

		model.addAttribute("product", product);

		return "product";
	}

	@RequestMapping("/")
	public String home(Model model) throws UnsupportedEncodingException {

		model.addAttribute("generos", p_service.ordenarLista(p_service.listarAllProd("genero")));
		model.addAttribute("distribuidoras", p_service.ordenarLista(p_service.listarAllProd("distribuidora")));
		model.addAttribute("plataformas", p_service.ordenarLista(p_service.listarAllProd("plataforma")));

		/*
		 * BufferedReader br = null;
		 * 
		 * String line = "";
		 * 
		 * try {
		 * 
		 * br = new BufferedReader(new FileReader(
		 * "C:\\Users\\Surrui\\Desktop\\fdghdfhdfghfhgfdhdghfhghfhdfh.csv"));
		 * 
		 * while ((line = br.readLine()) != null) {
		 * 
		 * System.out.println(line);
		 * 
		 * }
		 * 
		 * } catch (FileNotFoundException e) { e.printStackTrace(); } catch
		 * (IOException e) { e.printStackTrace(); } finally { if (br != null) {
		 * try { br.close(); } catch (IOException e) { e.printStackTrace(); } }
		 * }
		 */

		return "home";
	}

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String register() {
		return "register";
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String saveUser(@RequestParam("nombre") String name, @RequestParam("email") String mail,
			@RequestParam("passwd") String password, @RequestParam("direccion") String address, Model model) {

		Usuari result = u_service.crearUsuari(name, password, mail, address);

		if (result == null) {

			String error = "Error: El nombre/correo ya existe!";

			model.addAttribute("error", error);
			return "register";
		}
		return "redirect:/";
	}

	@RequestMapping(value = "/contact", method = RequestMethod.GET)
	public String contact(Model model) {

		model.addAttribute("generos", p_service.ordenarLista(p_service.listarAllProd("genero")));
		model.addAttribute("distribuidoras", p_service.ordenarLista(p_service.listarAllProd("distribuidora")));
		model.addAttribute("plataformas", p_service.ordenarLista(p_service.listarAllProd("plataforma")));

		return "contact";
	}

	@RequestMapping(value = "/about", method = RequestMethod.GET)
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

	private double round(double value, int places) {

		if (places < 0)
			throw new IllegalArgumentException();

		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);

		return bd.doubleValue();
	}
}