package net.javierjimenez.Models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "products")
public class Producte {

	@Id
	private String id;

	private String nom;
	private String descripcion;
	private String portada;
	private String [] imagenes;
	private String genero;
	private String distribuidora;
	private String plataforma;
	private String edad;
	private Integer cantidad;
	private Long ventas;
	private String activado;
	private Double precio;
	
	public Producte(){
		
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public String getPlataforma() {
		return plataforma;
	}

	public void setPlataforma(String plataforma) {
		this.plataforma = plataforma;
	}

	public String getEdad() {
		return edad;
	}

	public void setEdad(String edad) {
		this.edad = edad;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public String getActivado() {
		return activado;
	}

	public void setActivado(String activado) {
		this.activado = activado;
	}

	public String getDistribuidora() {
		return distribuidora;
	}

	public void setDistribuidora(String distribuidora) {
		this.distribuidora = distribuidora;
	}

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPortada (){
		return portada;
	}
	
	public void setPortada(String portada){
		this.portada = portada;
	}
	
	public String [] getImagenes() {
		return imagenes;
	}

	public void setImagenes(String [] imagenes) {
		this.imagenes = imagenes;
	}

	public Long getVentas() {
		return ventas;
	}

	public void setVentas(Long ventas) {
		this.ventas = ventas;
	}
	
}
