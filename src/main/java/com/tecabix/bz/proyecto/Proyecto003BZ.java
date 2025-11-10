package com.tecabix.bz.proyecto;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

import com.tecabix.db.entity.PersonaFisica;
import com.tecabix.db.entity.Proyecto;
import com.tecabix.db.entity.Usuario;
import com.tecabix.db.repository.PersonaFisicaRepository;
import com.tecabix.db.repository.ProyectoRepository;
import com.tecabix.db.repository.UsuarioRepository;
import com.tecabix.res.a.RSA026;
import com.tecabix.res.b.RSB032;
import com.tecabix.sv.rq.RQSV040;

/**
*
* @author Ramirez Urrutia Angel Abinadi
*/
public class Proyecto003BZ {

	private final ProyectoRepository proyectoRepository;
	private final UsuarioRepository usuarioRepository;
	private final PersonaFisicaRepository personaFisicaRepository;


	public Proyecto003BZ(ProyectoRepository proyectoRepository, UsuarioRepository usuarioRepository,
			PersonaFisicaRepository personaFisicaRepository) {
		this.proyectoRepository = proyectoRepository;
		this.usuarioRepository = usuarioRepository;
		this.personaFisicaRepository = personaFisicaRepository;
	}


	public ResponseEntity<RSB032> detalle(final RQSV040 rqsv040) {
		RSB032 respose = rqsv040.getRsb032();
		String ticket = rqsv040.getTicket();
		long id = Long.parseLong(ticket.replace("DEV-", ""));
		Optional<Proyecto> proyectoOp = proyectoRepository.findById(id);
		if(proyectoOp.isEmpty()) {
			return respose.notFound("No se encontro el registro");
		}
		Proyecto proyecto = proyectoOp.get();
		Map<Byte, String> nombres = new HashMap<Byte, String>();
		PersonaFisica persona = proyecto.getTrabajador().getPersonaFisica();
		String nombre = persona.getNombre();
		if(persona.getApellidoPaterno() != null && persona.getApellidoMaterno() != null) {
			if(!persona.getApellidoPaterno().isBlank() && !persona.getApellidoMaterno().isBlank()) {
				nombre = nombre + " " + persona.getApellidoPaterno() + " " + persona.getApellidoMaterno();
			}
		}
		nombres.put(RSA026.RESPONSABLE, nombre);
		
		Optional<Usuario> usuarioOptional = usuarioRepository.findById(proyecto.getUsuarioCreador());
		if(usuarioOptional.isEmpty()) {
			return respose.notFound("No se encontro el usuario que creo el ticket");
		}
		
		Optional<PersonaFisica> personaFisicaOP = personaFisicaRepository.findByPersona(usuarioOptional.get().getUsuarioPersona().getPersona().getId());
		if(personaFisicaOP.isEmpty()) {
			return respose.notFound("No se encontro la persona que creo el ticket");
		}
		persona = personaFisicaOP.get();
		nombre = persona.getNombre();
		if(persona.getApellidoPaterno() != null && persona.getApellidoMaterno() != null) {
			if(!persona.getApellidoPaterno().isBlank() && !persona.getApellidoMaterno().isBlank()) {
				nombre = nombre + " " + persona.getApellidoPaterno() + " " + persona.getApellidoMaterno();
			}
		}
		nombres.put(RSA026.CREADOR, nombre);
		
		usuarioOptional = usuarioRepository.findById(proyecto.getIdUsuarioModificado());
		if(usuarioOptional.isEmpty()) {
			return respose.notFound("No se encontro el usuario que modifico el ticket");
		}
		
		personaFisicaOP = personaFisicaRepository.findByPersona(usuarioOptional.get().getUsuarioPersona().getPersona().getId());
		if(personaFisicaOP.isEmpty()) {
			return respose.notFound("No se encontro la persona que modifico el ticket");
		}
		persona = personaFisicaOP.get();
		nombre = persona.getNombre();
		if(persona.getApellidoPaterno() != null && persona.getApellidoMaterno() != null) {
			if(!persona.getApellidoPaterno().isBlank() && !persona.getApellidoMaterno().isBlank()) {
				nombre = nombre + " " + persona.getApellidoPaterno() + " " + persona.getApellidoMaterno();
			}
		}
		nombres.put(RSA026.MODIFICADOR, nombre);
		
		persona = proyecto.getRevisor().getPersonaFisica();
		nombre = persona.getNombre();
		if(persona.getApellidoPaterno() != null && persona.getApellidoMaterno() != null) {
			if(!persona.getApellidoPaterno().isBlank() && !persona.getApellidoMaterno().isBlank()) {
				nombre = nombre + " " + persona.getApellidoPaterno() + " " + persona.getApellidoMaterno();
			}
		}
		nombres.put(RSA026.REVISOR, nombre);
		return respose.ok(proyecto, nombres);
	}
}
