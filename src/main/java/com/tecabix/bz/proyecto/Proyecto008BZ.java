package com.tecabix.bz.proyecto;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;

import com.tecabix.db.repository.ProyectoComentarioRepository;
import com.tecabix.res.b.RSB039;
import com.tecabix.sv.rq.RQSV045;

public class Proyecto008BZ {

	private final ProyectoComentarioRepository proyectoComentarioRepository;
	
	public Proyecto008BZ(ProyectoComentarioRepository proyectoComentarioRepository) {
		this.proyectoComentarioRepository = proyectoComentarioRepository;
	}

	public ResponseEntity<RSB039> listarComentario(RQSV045 rqsv045) {
		RSB039 respose = rqsv045.getRsb039();
		Sort sort = Sort.by(Sort.Direction.ASC, "id");
	    Pageable pageable = PageRequest.of(rqsv045.getPagina(), rqsv045.getElementos(), sort);
		return respose.ok(proyectoComentarioRepository.findByProyecto(rqsv045.getIdProyecto(),pageable));
	}
}
