package net.javierjimenez.Controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

//import net.javierjimenez.Models.Persona;
import net.javierjimenez.Repositories.PersonaRepositori;

@Controller
public class BotigaController {

	@Autowired
	PersonaRepositori mongo;
	
	@RequestMapping(value="/dashboard", method=RequestMethod.GET)
	public String admin(){
		return "admin";
	}
	
	
	@RequestMapping("/")
	public String home(){
		
		/*mongo.save(new Persona("Pepe"));
		mongo.save(new Persona("Pep"));
		mongo.save(new Persona("Pepet"));
		
		Persona p = mongo.findByNom("Pep");
		
		System.out.println(p.getId_persona());*/
		
		return "home";
	}
	
	/**@RequestMapping("/saluda/{com}")
    public String saluda(@RequestParam("nom") String n, @PathVariable("com") String c) {
		
		n = "prova"; 
		
		System.out.println(c + " " + n); //mongo.findByNom(n));
		
        return "index";
    }**/
	
	@RequestMapping(value="/login", method=RequestMethod.GET)
	  public String register(@RequestParam(value="error",required=false) String error,
	      HttpServletRequest request) {

	    if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
	      return "redirect:/";
	    }

	    return "login";
	  }
	
	@RequestMapping(value="/account", method=RequestMethod.GET)
	public String account(){
		return "account";
	}
	
	@RequestMapping(value="/wishlist", method=RequestMethod.GET)
	public String wishlist(){
		return "wishlist";
	}
	
	@RequestMapping(value="/contact", method=RequestMethod.GET)
	public String contact(){
		return "contact";
	}
	
	@RequestMapping(value="/single", method=RequestMethod.GET)
	public String single(){
		return "single";
	}
	
}