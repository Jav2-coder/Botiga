package net.javierjimenez.Controllers;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import net.javierjimenez.Models.Client;
import net.javierjimenez.Repositories.PersonaRepositori;

@Controller
public class BotigaController {

	@Autowired
	PersonaRepositori mongo;

	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	public String admin() {
		return "admin";
	}

	// Revisar estos enlaces https://www.youtube.com/watch?v=DRADZUzdFYQ -
	// http://www.baeldung.com/spring-security-registration - https://www.youtube.com/watch?v=wKHL3gmhsBY

	@RequestMapping("/")
	public String home() throws UnsupportedEncodingException {

		/*
		 * mongo.save(new Persona("Pepe")); mongo.save(new Persona("Pep"));
		 * mongo.save(new Persona("Pepet"));
		 * 
		 * Persona p = mongo.findByNom("Pep");
		 * 
		 * System.out.println(p.getId_persona());
		 */

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
	 * @throws UnsupportedEncodingException 
	 **/

	@RequestMapping(value = "/success", method = RequestMethod.GET)
	public String saveUser(@RequestParam("nombre") String name, @RequestParam("email") String mail,
			@RequestParam("passwd") String password, @RequestParam("direccion") String adress) throws UnsupportedEncodingException {

		String passwdEncoded = Base64.getEncoder().encodeToString(password.getBytes("utf-8"));
		
		mongo.save(new Client(name, mail, passwdEncoded, adress));
		
		System.out.println(name + " " + mail + " " + passwdEncoded + " " + adress);
		
		return "/";
	}

	@RequestMapping(value = "/account", method = RequestMethod.GET)
	public String account() {
		return "account";
	}

	@RequestMapping(value = "/contact", method = RequestMethod.GET)
	public String contact() {
		return "contact";
	}

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String register() {
		return "register";
	}

	@RequestMapping(value = "/about", method = RequestMethod.GET)
	public String about() {
		return "about";
	}

	@RequestMapping(value = "/product", method = RequestMethod.GET)
	public String product() {
		return "product";
	}
}