package com.tecabix.bz.proyecto.dto;

import com.tecabix.db.entity.Catalogo;
import com.tecabix.db.entity.CatalogoTipo;
import com.tecabix.db.repository.CatalogoRepository;
import com.tecabix.db.repository.ProyectoRepository;
import com.tecabix.db.repository.TrabajadorRepository;

public class Proyecto001BzDTO {
	private ProyectoRepository proyectoRepository;
	private CatalogoRepository catalogoRepository;
	private TrabajadorRepository trabajadorRepository;
	private Catalogo nuevo;
	private Catalogo porHacer;
	private CatalogoTipo tipoPrioridad;
	
	public ProyectoRepository getProyectoRepository() {
		return proyectoRepository;
	}
	public void setProyectoRepository(ProyectoRepository proyectoRepository) {
		this.proyectoRepository = proyectoRepository;
	}
	public CatalogoRepository getCatalogoRepository() {
		return catalogoRepository;
	}
	public void setCatalogoRepository(CatalogoRepository catalogoRepository) {
		this.catalogoRepository = catalogoRepository;
	}
	public TrabajadorRepository getTrabajadorRepository() {
		return trabajadorRepository;
	}
	public void setTrabajadorRepository(TrabajadorRepository trabajadorRepository) {
		this.trabajadorRepository = trabajadorRepository;
	}
	public Catalogo getNuevo() {
		return nuevo;
	}
	public void setNuevo(Catalogo nuevo) {
		this.nuevo = nuevo;
	}
	public Catalogo getPorHacer() {
		return porHacer;
	}
	public void setPorHacer(Catalogo porHacer) {
		this.porHacer = porHacer;
	}
	public CatalogoTipo getTipoPrioridad() {
		return tipoPrioridad;
	}
	public void setTipoPrioridad(CatalogoTipo tipoPrioridad) {
		this.tipoPrioridad = tipoPrioridad;
	}
}
