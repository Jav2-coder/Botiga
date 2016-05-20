package net.javierjimenez.Models;

public class Sell {

	private Producte producte;
	private Integer cantidad;
	
	public Sell(Producte p, Integer quantity){
		producte = p;
		cantidad = quantity;	
	}

	public Producte getProducte() {
		return producte;
	}

	public void setProducte(Producte producte) {
		this.producte = producte;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
}
