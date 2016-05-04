package net.javierjimenez.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import net.javierjimenez.Repositories.PersonaRepositori;

@Controller
public class BotigaController {

	PersonaRepositori mongo;
	
	@Autowired
	public BotigaController(PersonaRepositori mongo){
		this.mongo = mongo;
	}
}
