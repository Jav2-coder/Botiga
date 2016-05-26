package net.javierjimenez.Models;

import javax.xml.bind.annotation.XmlAccessorType;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class XmlWrapper {

	@XmlElementWrapper
	@XmlElement(name = "producte", type = Producte.class)
	List<Producte> productes;
	
	@XmlElementWrapper
	@XmlElement(name = "usuario", type = Usuari.class)
	List<Usuari> clientes;
	
	@XmlElementWrapper
	@XmlElement(name = "carrito", type = Carrito.class)
	List<Carrito> compras;

	public XmlWrapper() {
	}

	public List<Producte> getProductes() {
		return productes;
	}

	public void setProductes(List<Producte> productes) {
		this.productes = productes;
	}

	public List<Usuari> getClientes() {
		return clientes;
	}

	public void setClientes(List<Usuari> clientes) {
		this.clientes = clientes;
	}

	public List<Carrito> getCompras() {
		return compras;
	}

	public void setCompras(List<Carrito> compras) {
		this.compras = compras;
	}
	
}
