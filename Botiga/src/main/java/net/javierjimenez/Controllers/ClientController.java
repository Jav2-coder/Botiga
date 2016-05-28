package net.javierjimenez.Controllers;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.javierjimenez.Models.Carrito;
import net.javierjimenez.Models.Producte;
import net.javierjimenez.Models.Sell;
import net.javierjimenez.Models.Usuari;
import net.javierjimenez.Repositories.CarritoRepositori;
import net.javierjimenez.Repositories.ProducteService;
import net.javierjimenez.Repositories.UsuariService;

@Controller
@SessionAttributes({ "carrito" })
public class ClientController {

	@Autowired
	CarritoRepositori compra;

	@Autowired
	UsuariService userService;

	@Autowired
	ProducteService productService;

	@ModelAttribute("carrito")
	public Carrito getCarrito() {
		return new Carrito();
	}

	@RequestMapping("/account")
	public String account(Model model) {

		model.addAttribute("generos", productService.ordenarLista(productService.listarAllProd("genero")));
		model.addAttribute("distribuidoras",
				productService.ordenarLista(productService.listarAllProd("distribuidora")));
		model.addAttribute("plataformas", productService.ordenarLista(productService.listarAllProd("plataforma")));

		String nom = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		Usuari usuariRecuperat = userService.buscaUsuari(nom);
		model.addAttribute("usuario", usuariRecuperat);

		return "account";
	}

	@RequestMapping(value = "/categoria/{category}/{cat_name}")
	public String categoriaProd(@PathVariable String category, @PathVariable String cat_name, Model model) {

		model.addAttribute("generos", productService.ordenarLista(productService.listarAllProd("genero")));
		model.addAttribute("distribuidoras",
				productService.ordenarLista(productService.listarAllProd("distribuidora")));
		model.addAttribute("plataformas", productService.ordenarLista(productService.listarAllProd("plataforma")));

		List<Producte> juegos = productService.buscarProductosCat(category, cat_name, "Si");

		for (Producte p : juegos) {
			if (p.getNom().length() > 20) {
				p.setNom(p.getNom().substring(0, 20) + "...");
			}
		}

		model.addAttribute("vacio", juegos.isEmpty());
		model.addAttribute("juegos", juegos);

		return "category";
	}

	@RequestMapping(value = "/producto/{product_id}", method = RequestMethod.GET)
	public String product(@PathVariable String product_id, Model model) {

		model.addAttribute("generos", productService.ordenarLista(productService.listarAllProd("genero")));
		model.addAttribute("distribuidoras",
				productService.ordenarLista(productService.listarAllProd("distribuidora")));
		model.addAttribute("plataformas", productService.ordenarLista(productService.listarAllProd("plataforma")));

		Producte product = productService.buscarProdId(product_id);

		model.addAttribute("product", product);

		return "product";
	}

	@RequestMapping(value = "/imagenes/producto/{img:.+}", method = RequestMethod.GET)
	@ResponseBody
	public FileSystemResource getImatge(@PathVariable String img) {

		FileSystemResource resource;
		resource = new FileSystemResource("productos/" + img);

		return resource;
	}

	@RequestMapping(value = "/new_venta/{id}", method = RequestMethod.POST)
	public String addShopCart(@ModelAttribute("carrito") Carrito carrito, @PathVariable String id,
			@RequestParam("cantidad") String quantity, Model model) {

		Producte p = productService.buscarProdId(id);
		Integer cantidad = Integer.parseInt(quantity);

		carrito.addProducto(new Sell(p, cantidad));
		carrito.generateTotal();
		model.addAttribute("carrito", carrito);

		return "redirect:/producto/" + id;
	}

	@RequestMapping("/")
	public String home(Model model) throws UnsupportedEncodingException {

		model.addAttribute("generos", productService.ordenarLista(productService.listarAllProd("genero")));
		model.addAttribute("distribuidoras",
				productService.ordenarLista(productService.listarAllProd("distribuidora")));
		model.addAttribute("plataformas", productService.ordenarLista(productService.listarAllProd("plataforma")));

		List<Producte> masVendidos = productService.prodMasVendidos();
		
		for(Producte p : masVendidos){
			if (p.getNom().length() > 20) {
				p.setNom(p.getNom().substring(0, 20) + "...");
			}
		}

		model.addAttribute("juegos", masVendidos);

		return "home";
	}

	@RequestMapping(value = "/checkout", method = RequestMethod.POST)
	public String pedido(@ModelAttribute("carrito") Carrito carrito, SessionStatus status, Model model) {

		List<Sell> compra = carrito.getSells();
		
		for(int i = 0; i < compra.size(); i++){
			Producte p = compra.get(i).getProducte();
			if (p.getNom().length() > 20) {
				p.setNom(p.getNom().substring(0, 20) + "...");
			}
		}
		
		model.addAttribute("carrito", carrito);

		return "checkout";
	}

	@RequestMapping(value = "/delCart/{id}", method = RequestMethod.POST)
	public String delSell(@PathVariable String id, @ModelAttribute("carrito") Carrito carrito) {

		for (int i = carrito.getSells().size() - 1; i >= 0; i--) {
			if (carrito.getSells().get(i).getId().equals(id)) {
				carrito.getSells().remove(i);
			}
		}

		if (carrito.getSells().size() == 0) {

			carrito.setTieneCosas(false);

			return "redirect:/";
		}

		return "checkout";
	}

	@RequestMapping(value = "/buyCart", method = RequestMethod.POST)
	public String buyCart(@ModelAttribute("carrito") Carrito carrito, SessionStatus status, @RequestParam("envio") String envio) {

		String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

		carrito.setPago(carrito.getPago() + (carrito.getPago() * Integer.parseInt(envio)/100));
		
		carrito.setUsername(username);

		compra.save(carrito);

		for (Sell s : carrito.getSells()) {

			Integer restarStock = s.getCantidad();

			Producte p = s.getProducte();
			p.setCantidad(p.getCantidad() - restarStock);
			p.setVentas(p.getVentas() + restarStock);

			if (p.getCantidad() == 0) {
				p.setActivado("No");
			}
			productService.editProd(p);
		}

		carrito.setTieneCosas(false);
		status.setComplete();

		return "redirect:/compraPagada";
	}

	@RequestMapping("/compraPagada")
	public String compraPagada() {

		return "compra_realizada";
	}

	@RequestMapping("/products")
	public String llistaProductes(@RequestParam(required = false) String keyword,
			@RequestParam(required = false) Integer page, Model model) {

		model.addAttribute("generos", productService.ordenarLista(productService.listarAllProd("genero")));
		model.addAttribute("distribuidoras",
				productService.ordenarLista(productService.listarAllProd("distribuidora")));
		model.addAttribute("plataformas", productService.ordenarLista(productService.listarAllProd("plataforma")));

		List<Producte> listaProductos;
		long totalProductos;

		int pagina = 0;

		if (page != null) {
			pagina = page;
		}

		if (keyword == null) {
			model.addAttribute("url", "");
			listaProductos = productService.todosProductosPagina(pagina, 8);

		} else {

			model.addAttribute("url", "&keyword=" + keyword);
			listaProductos = productService.buscarNombreProductoPagina(keyword, pagina, 8);
		}

		for (Producte p : listaProductos) {
			if (p.getNom().length() > 20) {
				p.setNom(p.getNom().substring(0, 20) + "...");
			}
		}

		totalProductos = productService.countProductos(keyword);

		model.addAttribute("vacio", listaProductos.isEmpty());
		model.addAttribute("pagines", (totalProductos / 8) + 1);
		model.addAttribute("pagina", pagina);
		model.addAttribute("juegos", listaProductos);
		return "products";
	}

	@RequestMapping("/register")
	public String register() {
		return "register";
	}

	@RequestMapping(value = "/newregister", method = RequestMethod.POST)
	public String saveUser(@RequestParam("nombre") String name, @RequestParam("email") String mail,
			@RequestParam("passwd") String password, @RequestParam("direccion") String address,
			final RedirectAttributes redirectAttributes) {

		Usuari result = userService.crearUsuari(name, password, mail, address, false);

		if (result == null) {

			String error = "Error: El nombre/correo ya existe!";

			redirectAttributes.addFlashAttribute("error", error);
			return "redirect:/register";
		}
		return "redirect:/";
	}

	@RequestMapping("/contact")
	public String contact(Model model) {

		model.addAttribute("generos", productService.ordenarLista(productService.listarAllProd("genero")));
		model.addAttribute("distribuidoras",
				productService.ordenarLista(productService.listarAllProd("distribuidora")));
		model.addAttribute("plataformas", productService.ordenarLista(productService.listarAllProd("plataforma")));

		return "contact";
	}

	@RequestMapping("/about")
	public String about(Model model) {

		model.addAttribute("generos", productService.ordenarLista(productService.listarAllProd("genero")));
		model.addAttribute("distribuidoras",
				productService.ordenarLista(productService.listarAllProd("distribuidora")));
		model.addAttribute("plataformas", productService.ordenarLista(productService.listarAllProd("plataforma")));

		return "about";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginRequest(@RequestParam(value = "error", required = false) String error,
			HttpServletRequest request) {

		if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
			return "redirect:/account";
		}

		return "login";
	}

}
