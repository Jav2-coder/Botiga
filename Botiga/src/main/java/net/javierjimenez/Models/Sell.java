package net.javierjimenez.Models;

public class Sell {

	private Producte producte;
	private Integer cantidad;
	private String id;
	
	public Sell() {		
	}
	
	public Sell(Producte p, Integer quantity){
		producte = p;
		cantidad = quantity;	
		id = p.getId();
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
