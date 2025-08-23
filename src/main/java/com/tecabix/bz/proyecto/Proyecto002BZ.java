package com.tecabix.bz.proyecto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
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

	private ProyectoRepository proyectoRepository;
	
	
	
	public Proyecto002BZ(ProyectoRepository repository) {
		this.proyectoRepository = repository;
	}

	public ResponseEntity<RSB031> listar(final RQSV039 rqsv039) {
		RSB031 rsb031 = rqsv039.getRsb031();
		byte elementos = rqsv039.getElementos();
		short pagina = rqsv039.getPagina();
		
	    Optional<String> texto = rqsv039.getTexto();
	    List<UUID> etapas = rqsv039.getEtapas().orElse(null);
	    List<UUID> estatus = rqsv039.getEstatus().orElse(null);
	    List<UUID> prioridad = rqsv039.getPrioridad().orElse(null);
	    LocalDate fechaMin = rqsv039.getFechaCreacionMin().orElse(null);
	    LocalDate fechaMax = rqsv039.getFechaCreacionMax().orElse(null);
	    UUID trabajador = rqsv039.getTrabajador().orElse(null);
	    
	    boolean presentTexto = texto.isPresent();

	    Sort sort = Sort.by(Sort.Direction.ASC, "id");
	    Pageable pageable = PageRequest.of(pagina, elementos, sort);
	    String nombre = presentTexto && !texto.get().isBlank() ? "%"+texto.get()+"%" : null;
	    
	    return rsb031.ok(proyectoRepository.findByFilter(nombre, etapas, estatus, prioridad, fechaMin, fechaMax, trabajador, pageable).toList());
	}
}
