package com.tecabix.bz.proyecto.dto;

import com.tecabix.db.entity.Catalogo;
import com.tecabix.db.entity.Usuario;
import com.tecabix.db.repository.CatalogoRepository;
import com.tecabix.db.repository.ProyectoComentarioRepository;
import com.tecabix.db.repository.ProyectoRepository;

public class Proyecto004BzDTO {

	private CatalogoRepository catalogoRepository;
	private ProyectoRepository proyectoRepository;
	private ProyectoComentarioRepository proyectoComentarioRepository;

	private Catalogo nuevo;
	private Catalogo analisis;
	private Catalogo desarrollo;
	private Catalogo construccion;
	private Catalogo prueba;
	private Catalogo calidad;
	private Catalogo produccion;
	private Catalogo liberado;
	private Catalogo descartado;
	
	private Catalogo porHacer;
	private Catalogo listo;
	
	private Usuario usuario;

	public CatalogoRepository getCatalogoRepository() {
		return catalogoRepository;
	}

	public void setCatalogoRepository(CatalogoRepository catalogoRepository) {
		this.catalogoRepository = catalogoRepository;
	}

	public ProyectoRepository getProyectoRepository() {
		return proyectoRepository;
	}

	public void setProyectoRepository(ProyectoRepository proyectoRepository) {
		this.proyectoRepository = proyectoRepository;
	}

	public ProyectoComentarioRepository getProyectoComentarioRepository() {
		return proyectoComentarioRepository;
	}

	public void setProyectoComentarioRepository(ProyectoComentarioRepository proyectoComentarioRepository) {
		this.proyectoComentarioRepository = proyectoComentarioRepository;
	}

	public Catalogo getNuevo() {
		return nuevo;
	}

	public void setNuevo(Catalogo nuevo) {
		this.nuevo = nuevo;
	}

	public Catalogo getAnalisis() {
		return analisis;
	}

	public void setAnalisis(Catalogo analisis) {
		this.analisis = analisis;
	}

	public Catalogo getDesarrollo() {
		return desarrollo;
	}

	public void setDesarrollo(Catalogo desarrollo) {
		this.desarrollo = desarrollo;
	}

	public Catalogo getConstruccion() {
		return construccion;
	}

	public void setConstruccion(Catalogo construccion) {
		this.construccion = construccion;
	}

	public Catalogo getPrueba() {
		return prueba;
	}

	public void setPrueba(Catalogo prueba) {
		this.prueba = prueba;
	}

	public Catalogo getCalidad() {
		return calidad;
	}

	public void setCalidad(Catalogo calidad) {
		this.calidad = calidad;
	}

	public Catalogo getProduccion() {
		return produccion;
	}

	public void setProduccion(Catalogo produccion) {
		this.produccion = produccion;
	}

	public Catalogo getLiberado() {
		return liberado;
	}

	public void setLiberado(Catalogo liberado) {
		this.liberado = liberado;
	}

	public Catalogo getDescartado() {
		return descartado;
	}

	public void setDescartado(Catalogo descartado) {
		this.descartado = descartado;
	}

	public Catalogo getPorHacer() {
		return porHacer;
	}

	public void setPorHacer(Catalogo porHacer) {
		this.porHacer = porHacer;
	}

	public Catalogo getListo() {
		return listo;
	}

	public void setListo(Catalogo listo) {
		this.listo = listo;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
