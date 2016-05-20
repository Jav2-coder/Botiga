package net.javierjimenez.Models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "carrito")
public class Carrito {

	@Id
	private String id;

	private Date fecha;
	private List<Sell> sells;
	private String username;
	private Double pago;
	private boolean tieneCosas;

	public Carrito() {
		fecha = new Date();
		sells = new ArrayList<Sell>();
		username = "anomymous";
		tieneCosas = false;
	}

	public void addProducto(Sell venta){
		tieneCosas = true;
		sells.add(venta);
	}
	
	public Carrito(String name) {
		this();
		username = name;
	}

	public Double getPago() {
		pago = 0.0;
		
		for (Sell venta : sells) {
			pago = pago + (venta.getCantidad() * venta.getProducte().getPrecio());
		}
		
		pago = pago + ((pago*20)/100);
		
		return pago;
	}

	public void setPago(Double pago) {
		this.pago = pago;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public List<Sell> getSells() {
		return sells;
	}

	public void setSells(List<Sell> sells) {
		this.sells = sells;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isTieneCosas() {
		return tieneCosas;
	}

	public void setTieneCosas(boolean tieneCosas) {
		this.tieneCosas = tieneCosas;
	}
}
