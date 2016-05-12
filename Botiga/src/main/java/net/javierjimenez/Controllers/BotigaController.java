package net.javierjimenez.Controllers;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import net.javierjimenez.Models.Usuari;
import net.javierjimenez.Repositories.ProducteService;
import net.javierjimenez.Repositories.UsuariService;

@Controller
public class BotigaController {

	@Autowired
	UsuariService userservice;
	ProducteService productservice;

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	public String dashboard() {
		return "dashboard";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/listClients", method = RequestMethod.GET)
	public String listClients() {
		return "listClients";
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
	public String newAdmin() {
		return "newAdmin";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/newAdmin", method = RequestMethod.POST)
	public String saveAdmin(@RequestParam("username") String name, @RequestParam("passwd") String password,
			@RequestParam("email") String email, Model model) {

		Usuari result = userservice.crearAdmin(name, password, email);

		if (result == null) {

			// String error = "<div class='msg msg-error'><p><strong>Error: El
			// nombre/correo ya estan en uso!</strong></p></div>";
			/*
			 * https://github.com/MichalGasiorowski/java-spring-tutorial/blob/
			 * master/spring-tutorial-114/src/main/java/com/goose/spring/web/
			 * controllers/ErrorHandler.java
			 */

			String error = "NOPE";

			model.addAttribute("error", error);
			return "newAdmin";
		}
		return "redirect:/dashboard";
	}

	// Revisar estos enlaces https://www.youtube.com/watch?v=DRADZUzdFYQ -
	// http://www.baeldung.com/spring-security-registration -
	// https://www.youtube.com/watch?v=wKHL3gmhsBY

	@RequestMapping("/")
	public String home() throws UnsupportedEncodingException {

		return "home";
	}

	/**
	 * @RequestMapping("/saluda/{com}") public String
	 * saluda(@RequestParam("nom") String n, @PathVariable("com") String c) {
	 * 
	 * n = "prova";
	 * 
	 * System.out.println(c + " " + n); //mongo.findByNom(n));
	 * 
	 * return "index"; }
	 * 
	 * @throws UnsupportedEncodingException
	 **/

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String register() {
		return "register";
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String saveUser(@RequestParam("nombre") String name, @RequestParam("email") String mail,
			@RequestParam("passwd") String password, @RequestParam("direccion") String address, Model model) {

		Usuari result = userservice.crearUsuari(name, password, mail, address);

		if (result == null) {

			String error = "Error: El nombre/correo ya existe!";

			model.addAttribute("error", error);
			return "register";
		}
		return "redirect:/";
	}

	@Secured("ROLE_USER")
	@RequestMapping(value = "/account", method = RequestMethod.GET)
	public String account(Model model) {

		String nom = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		Usuari usuariRecuperat = userservice.buscaUsuari(nom);
		model.addAttribute("usuario", usuariRecuperat);

		return "account";
	}

	@RequestMapping(value = "/contact", method = RequestMethod.GET)
	public String contact() {
		return "contact";
	}

	@RequestMapping(value = "/about", method = RequestMethod.GET)
	public String about() {
		return "about";
	}

	@RequestMapping(value = "/product", method = RequestMethod.GET)
	public String product() {
		return "product";
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