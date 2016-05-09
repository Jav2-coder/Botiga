package net.javierjimenez.Controllers;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import net.javierjimenez.Models.Usuari;
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
	// http://www.baeldung.com/spring-security-registration -
	// https://www.youtube.com/watch?v=wKHL3gmhsBY

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
	 * 
	 * @throws UnsupportedEncodingException
	 **/

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String register() {
		return "register";
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String saveUser(@RequestParam("nombre") String name, @RequestParam("email") String mail,
			@RequestParam("passwd") String password, @RequestParam("direccion") String adress) {

		String p = base64Encode(password);
		String a = base64Encode(adress);

		mongo.save(new Usuari(name, mail, p, a, 0));

		System.out.println("Nombre: " + name + " | Email: " + mail + " | Contraseña: " + p + " | Dirección: " + a);

		return "home";
	}

	@RequestMapping(value = "/account", method = RequestMethod.GET)
	public String account() {
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
	
	@RequestMapping(value="/login", method=RequestMethod.GET)
	  public String loginRequest(@RequestParam(value="error",required=false) String error,
	      HttpServletRequest request) {

	    if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
	      return "redirect:/register";
	    }

	    return "login";
	  }


	public static String base64Encode(String token) {
		byte[] encodedBytes = Base64.encode(token.getBytes());
		return new String(encodedBytes, Charset.forName("UTF-8"));
	}

	public static String base64Decode(String token) {
		byte[] decodedBytes = Base64.decode(token.getBytes());
		return new String(decodedBytes, Charset.forName("UTF-8"));
	}
}