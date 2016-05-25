package net.javierjimenez.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import net.javierjimenez.Models.XmlWrapper;
import net.javierjimenez.Repositories.ProducteService;

@RestController
public class SeguridadController {

	ProducteService product;

	@Autowired
	public SeguridadController(ProducteService product) {
		this.product = product;
	}

	@RequestMapping(value = "/hello", produces = { "application/xml" })
	public @ResponseBody XmlWrapper generaColors() {
		//log.info("Algú demana colors");
		
		XmlWrapper p = new XmlWrapper();
		p.setProductes(product.allProducts());
		
		return p;
	}
}
