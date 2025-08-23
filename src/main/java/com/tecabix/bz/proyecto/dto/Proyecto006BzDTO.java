package com.tecabix.bz.proyecto.dto;

import com.tecabix.db.entity.CatalogoTipo;
import com.tecabix.db.entity.Usuario;
import com.tecabix.db.repository.ProyectoComentarioRepository;
import com.tecabix.db.repository.ProyectoRepository;
import com.tecabix.db.repository.UsuarioRepository;

public class Proyecto006BzDTO {

	private UsuarioRepository usuarioRepository;
	private ProyectoRepository proyectoRepository;
	private ProyectoComentarioRepository proyectoComentarioRepository;
	private CatalogoTipo prioridad;
	private Usuario usuario;
	
	public UsuarioRepository getUsuarioRepository() {
		return usuarioRepository;
	}
	public void setUsuarioRepository(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
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
	public CatalogoTipo getPrioridad() {
		return prioridad;
	}
	public void setPrioridad(CatalogoTipo prioridad) {
		this.prioridad = prioridad;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}
