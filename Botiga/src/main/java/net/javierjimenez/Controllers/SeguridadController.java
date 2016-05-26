package net.javierjimenez.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import net.javierjimenez.Models.XmlWrapper;
import net.javierjimenez.Repositories.CarritoRepositori;
import net.javierjimenez.Repositories.ProducteService;
import net.javierjimenez.Repositories.UsuariService;

@RestController
public class SeguridadController {

	ProducteService product;
	UsuariService user;
	CarritoRepositori cart;

	@Autowired
	public SeguridadController(ProducteService product, UsuariService user, CarritoRepositori cart) {
		this.product = product;
		this.user = user;
		this.cart = cart;
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/xmlProductos", method = RequestMethod.GET, produces = { "application/xml" })
	public @ResponseBody XmlWrapper xmlProductos() {

		XmlWrapper xml = new XmlWrapper();
		xml.setProductes(product.allProducts());

		return xml;
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/xmlClientes", method = RequestMethod.GET, produces = { "application/xml" })
	public @ResponseBody XmlWrapper xmlClientes() {

		XmlWrapper xml = new XmlWrapper();
		xml.setClientes(user.allClients());

		return xml;
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/xmlCompras", method = RequestMethod.GET, produces = { "application/xml" })
	public @ResponseBody XmlWrapper xmlCompras() {

		XmlWrapper xml = new XmlWrapper();
		xml.setCompras(cart.findAll());

		return xml;
	}
}
