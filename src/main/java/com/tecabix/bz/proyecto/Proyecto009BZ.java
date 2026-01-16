package com.tecabix.bz.proyecto;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;

import com.tecabix.db.entity.ProyectoComentario;
import com.tecabix.db.entity.Sesion;
import com.tecabix.db.repository.ProyectoComentarioRepository;
import com.tecabix.res.b.RSB040;
import com.tecabix.sv.rq.RQSV046;

public class Proyecto009BZ {

	private final ProyectoComentarioRepository proyectoComentarioRepository;
	
	public Proyecto009BZ(ProyectoComentarioRepository proyectoComentarioRepository) {
		this.proyectoComentarioRepository = proyectoComentarioRepository;
	}

	public ResponseEntity<RSB040> actualizarComentario(RQSV046 rqsv046) {
		RSB040 response = rqsv046.getRsb040();
		Sesion sesion = rqsv046.getSesion();

		ProyectoComentario comentario = proyectoComentarioRepository.findByClave(rqsv046.getComentario()).orElse(null);
		if(comentario == null) {
			return response.notFound("Ni se encontro el comentario");
		}
		comentario.setFechaModificado(LocalDateTime.now());
		comentario.setIdUsuarioModificado(sesion.getUsuario().getId());
		proyectoComentarioRepository.save(comentario);
		return response.ok(comentario);
	}
}
