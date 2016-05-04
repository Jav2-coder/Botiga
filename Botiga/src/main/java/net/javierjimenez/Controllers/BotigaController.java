package net.javierjimenez.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import net.javierjimenez.Repositories.PersonaRepositori;

@Controller
public class BotigaController {

	@Autowired
	PersonaRepositori mongo;
	
	@RequestMapping("/saluda/{com}")
    public String saluda(@RequestParam("nom") String n, @PathVariable("com") String c) {
		System.out.println(c + " " + mongo.findByNom(n));
		
        return "index";
    }
}