package net.javierjimenez.Controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import net.javierjimenez.Repositories.PersonaRepositori;

@Controller
public class BotigaController {

	@Autowired
	PersonaRepositori mongo;
	
	@RequestMapping("/")
	public String saluda(){
		
		String n = "Home"; 
		
		System.out.println(n);
		
		return "home";
	}
	
	/**@RequestMapping("/saluda/{com}")
    public String saluda(@RequestParam("nom") String n, @PathVariable("com") String c) {
		
		n = "prova"; 
		
		System.out.println(c + " " + n); //mongo.findByNom(n));
		
        return "index";
    }**/
	
	@RequestMapping(value="/register", method=RequestMethod.GET)
	  public String login(@RequestParam(value="error",required=false) String error,
	      HttpServletRequest request) {

	    if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
	      return "redirect:/";
	    }

	    return "register";
	  }
	
	@RequestMapping(value="/account")
	public String account(){
		return "account";
	}
}