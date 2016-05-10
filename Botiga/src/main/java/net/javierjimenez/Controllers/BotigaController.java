package net.javierjimenez.Controllers;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import net.javierjimenez.Models.Usuari;
import net.javierjimenez.Repositories.UsuariService;

@Controller
public class BotigaController {

	@Autowired
	UsuariService userservice;

	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	public String admin() {
		return "admin";
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
			@RequestParam("passwd") String password, @RequestParam("direccion") String address) {

		userservice.crearUsuari(name, password, mail, address);

		return "redirect:/";
	}

	@RequestMapping(value = "/account", method = RequestMethod.GET)
	public String account(Model model) {
		
		System.out.println("NOPE");
		
		if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
	       
			String nom = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
			Usuari usuariRecuperat = userservice.buscaUsuari(nom);
			model.addAttribute("usuario", usuariRecuperat);
			
			return "account";
	    }
		
		return "redirect:/login";
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
	
	@RequestMapping(value="/login", method=RequestMethod.GET)
	  public String loginRequest(@RequestParam(value="error",required=false) String error,
	      HttpServletRequest request) {

		if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
	      return "redirect:/";
	    }

	    return "login";
	  }
}