package net.javierjimenez.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import net.javierjimenez.Models.XmlWrapper;
import net.javierjimenez.Repositories.ProducteService;
import net.javierjimenez.Repositories.UsuariService;

@RestController
public class SeguridadController {

	ProducteService product;
	UsuariService user;

	@Autowired
	public SeguridadController(ProducteService product, UsuariService user) {
		this.product = product;
		this.user = user;
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/xmlProductos", produces = { "application/xml" })
	public @ResponseBody XmlWrapper xmlProductos() {
		
		XmlWrapper p = new XmlWrapper();
		p.setProductes(product.allProducts());
		
		return p;
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/xmlClientes", produces = { "application/xml" })
	public @ResponseBody XmlWrapper xmlClientes() {
		
		XmlWrapper p = new XmlWrapper();
		p.setClientes(user.allClients());
		
		return p;
	}
}
