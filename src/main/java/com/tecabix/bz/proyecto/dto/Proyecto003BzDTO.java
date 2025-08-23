package com.tecabix.bz.proyecto.dto;

import com.tecabix.db.repository.PersonaFisicaRepository;
import com.tecabix.db.repository.ProyectoRepository;
import com.tecabix.db.repository.UsuarioRepository;

public class Proyecto003BzDTO {

	private ProyectoRepository proyectoRepository;
	private UsuarioRepository usuarioRepository;
	private PersonaFisicaRepository personaFisicaRepository;
	
	public ProyectoRepository getProyectoRepository() {
		return proyectoRepository;
	}
	public void setProyectoRepository(ProyectoRepository proyectoRepository) {
		this.proyectoRepository = proyectoRepository;
	}
	public UsuarioRepository getUsuarioRepository() {
		return usuarioRepository;
	}
	public void setUsuarioRepository(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}
	public PersonaFisicaRepository getPersonaFisicaRepository() {
		return personaFisicaRepository;
	}
	public void setPersonaFisicaRepository(PersonaFisicaRepository personaFisicaRepository) {
		this.personaFisicaRepository = personaFisicaRepository;
	}
}
