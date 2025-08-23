package com.tecabix.bz.proyecto.dto;

import com.tecabix.db.entity.Catalogo;
import com.tecabix.db.entity.Usuario;
import com.tecabix.db.repository.CatalogoRepository;
import com.tecabix.db.repository.ProyectoComentarioRepository;
import com.tecabix.db.repository.ProyectoRepository;

public class Proyecto005BzDTO {

	private CatalogoRepository catalogoRepository;
	private ProyectoRepository proyectoRepository;
	private ProyectoComentarioRepository proyectoComentarioRepository;

	private Catalogo porHacer;
	private Catalogo enProceso;
	private Catalogo enRevision;
	private Catalogo listo;
	private Catalogo enPausa;
	private Catalogo bloqueado;
	private Catalogo conObservaciones;
	
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

	public Catalogo getPorHacer() {
		return porHacer;
	}

	public void setPorHacer(Catalogo porHacer) {
		this.porHacer = porHacer;
	}

	public Catalogo getEnProceso() {
		return enProceso;
	}

	public void setEnProceso(Catalogo enProceso) {
		this.enProceso = enProceso;
	}

	public Catalogo getEnRevision() {
		return enRevision;
	}

	public void setEnRevision(Catalogo enRevision) {
		this.enRevision = enRevision;
	}

	public Catalogo getListo() {
		return listo;
	}

	public void setListo(Catalogo listo) {
		this.listo = listo;
	}

	public Catalogo getEnPausa() {
		return enPausa;
	}

	public void setEnPausa(Catalogo enPausa) {
		this.enPausa = enPausa;
	}

	public Catalogo getBloqueado() {
		return bloqueado;
	}

	public void setBloqueado(Catalogo bloqueado) {
		this.bloqueado = bloqueado;
	}

	public Catalogo getConObservaciones() {
		return conObservaciones;
	}

	public void setConObservaciones(Catalogo conObservaciones) {
		this.conObservaciones = conObservaciones;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
