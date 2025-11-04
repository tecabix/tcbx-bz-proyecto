package com.tecabix.bz.proyecto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;

import com.tecabix.db.repository.ProyectoRepository;
import com.tecabix.res.b.RSB031;
import com.tecabix.sv.rq.RQSV039;

/**
*
* @author Ramirez Urrutia Angel Abinadi
*/
public class Proyecto002BZ {

	private final ProyectoRepository proyectoRepository;
	
	public Proyecto002BZ(ProyectoRepository repository) {
		this.proyectoRepository = repository;
	}

	public ResponseEntity<RSB031> listar(final RQSV039 rqsv039) {
		RSB031 rsb031 = rqsv039.getRsb031();
		byte elementos = rqsv039.getElementos();
		short pagina = rqsv039.getPagina();
		
	    String texto = rqsv039.getTexto().orElse(null);
	    List<UUID> etapas = rqsv039.getEtapas();
	    List<UUID> estatus = rqsv039.getEstatus();
	    List<UUID> prioridad = rqsv039.getPrioridad();
	    LocalDate fechaMin = rqsv039.getFechaCreacionMin();
	    LocalDate fechaMax = rqsv039.getFechaCreacionMax();
	    UUID trabajador = rqsv039.getTrabajador().orElse(null);
	    if(texto == null) {
	    	texto = new String();
	    }
	    Sort sort = Sort.by(Sort.Direction.ASC, "id");
	    Pageable pageable = PageRequest.of(pagina, elementos, sort);
	    if(rqsv039.getTrabajador().isPresent()) {
	    	return rsb031.ok(proyectoRepository.findByFilterTrabajador(texto, etapas, estatus, prioridad, fechaMin, fechaMax, prioridad, trabajador, pageable).toList());
	    }
	    return rsb031.ok(proyectoRepository.findByFilter(texto, etapas, estatus, prioridad, fechaMin, fechaMax, prioridad, pageable).toList());
	}
}
