package com.tecabix.bz.proyecto;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.ResponseEntity;

import com.tecabix.db.entity.Catalogo;
import com.tecabix.db.entity.CatalogoTipo;
import com.tecabix.db.entity.Proyecto;
import com.tecabix.db.entity.Trabajador;
import com.tecabix.db.repository.CatalogoRepository;
import com.tecabix.db.repository.ProyectoRepository;
import com.tecabix.db.repository.TrabajadorRepository;
import com.tecabix.res.b.RSB030;
import com.tecabix.sv.rq.RQSV038;

/**
*
* @author Ramirez Urrutia Angel Abinadi
*/
public class Proyecto001BZ {

	private final ProyectoRepository proyectoRepository;
	private final CatalogoRepository catalogoRepository;
	private final TrabajadorRepository trabajadorRepository;
	private final Catalogo nuevo;
	private final Catalogo porHacer;
	private final Catalogo productBacklog;
	private final CatalogoTipo tipoPrioridad;
	

	public Proyecto001BZ(ProyectoRepository proyectoRepository, CatalogoRepository catalogoRepository,
			TrabajadorRepository trabajadorRepository, Catalogo nuevo, Catalogo porHacer, CatalogoTipo tipoPrioridad,
			Catalogo productBacklog) {
		this.proyectoRepository = proyectoRepository;
		this.catalogoRepository = catalogoRepository;
		this.trabajadorRepository = trabajadorRepository;
		this.nuevo = nuevo;
		this.porHacer = porHacer;
		this.tipoPrioridad = tipoPrioridad;
		this.productBacklog = productBacklog;
	}

	public ResponseEntity<RSB030> crear(final RQSV038 rqsv038) {
		RSB030 response = rqsv038.getRsb030();
		Proyecto proyecto = new Proyecto();
		Optional<Trabajador> trabajadorOP;
		if (rqsv038.getTrabajador() == null) {
		    trabajadorOP = trabajadorRepository.findByClaveUsuario(rqsv038.getSesion().getUsuario().getClave());
		} else {
		    trabajadorOP = trabajadorRepository.findByClave(rqsv038.getTrabajador());
		    if(trabajadorOP.isEmpty()) {
	            return response.notFound("No se encontro el trabajador");
	        }
		}

		proyecto.setTrabajador(trabajadorOP.get());
		
		Optional<Catalogo> catalogoOP = catalogoRepository.findByClave(rqsv038.getPrioridad());
		if(catalogoOP.isEmpty()) {
			return response.notFound("No se encontro la prioridad");
		}
		proyecto.setPrioridad(catalogoOP.get());
		if(!proyecto.getPrioridad().getCatalogoTipo().equals(tipoPrioridad)) {
			return response.notFound("La prioridad no es valida");
		}
		Optional<Trabajador> revisor= trabajadorRepository.findByClave(rqsv038.getRevisor());
		if(revisor.isEmpty()) {
			return response.notFound("No se encontro el usuario responsable");
		}
		proyecto.setRevisor(revisor.get());
		proyecto.setClave(UUID.randomUUID());
		proyecto.setDescripcion(rqsv038.getDescripcion());
		proyecto.setEstatus(porHacer);
		proyecto.setEtapa(nuevo);
		proyecto.setFechaCreacion(LocalDateTime.now());
		proyecto.setFechaModificado(LocalDateTime.now());
		proyecto.setFin(rqsv038.getFin());
		proyecto.setIdUsuarioModificado(rqsv038.getSesion().getUsuario().getId());
		proyecto.setInicio(rqsv038.getInicio());
		proyecto.setNombre(rqsv038.getNombre());
		proyecto.setUsuarioCreador(rqsv038.getSesion().getUsuario().getId());
		proyecto.setTipoBacklog(productBacklog);
		return response.ok(proyectoRepository.save(proyecto));
	}
}
