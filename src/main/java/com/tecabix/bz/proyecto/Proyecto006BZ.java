package com.tecabix.bz.proyecto;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.ResponseEntity;

import com.tecabix.db.entity.Catalogo;
import com.tecabix.db.entity.CatalogoTipo;
import com.tecabix.db.entity.Proyecto;
import com.tecabix.db.entity.ProyectoComentario;
import com.tecabix.db.entity.Sesion;
import com.tecabix.db.entity.Trabajador;
import com.tecabix.db.repository.ProyectoComentarioRepository;
import com.tecabix.db.repository.ProyectoRepository;
import com.tecabix.db.repository.TrabajadorRepository;
import com.tecabix.res.b.RSB035;
import com.tecabix.sv.rq.RQSV043;

/**
*
* @author Ramirez Urrutia Angel Abinadi
*/
public class Proyecto006BZ {

	private final TrabajadorRepository trabajadorRepository;
	private final ProyectoRepository proyectoRepository;
	private final ProyectoComentarioRepository proyectoComentarioRepository;
	private final CatalogoTipo prioridad;

	public Proyecto006BZ(TrabajadorRepository trabajadorRepository, ProyectoRepository proyectoRepository,
			ProyectoComentarioRepository proyectoComentarioRepository, CatalogoTipo prioridad) {
		super();
		this.trabajadorRepository = trabajadorRepository;
		this.proyectoRepository = proyectoRepository;
		this.proyectoComentarioRepository = proyectoComentarioRepository;
		this.prioridad = prioridad;
	}


	public ResponseEntity<RSB035> actulizar(final RQSV043 rqsv043) {
		RSB035 rsb035 = rqsv043.getRsb035();
		Sesion sesion = rqsv043.getSesion();
		Optional<Proyecto> proyectoOp = proyectoRepository.findByClave(rqsv043.getProyecto());
		if(proyectoOp.isEmpty()) {
			return rsb035.notFound("No se encontro el proyecto");
		}
		Proyecto proyecto = proyectoOp.get();
		StringBuilder builder = new StringBuilder("Se a cambiado: ");
		boolean esElPrimerCambio = false;
		StringBuilder cambio = new StringBuilder();
		if(!rqsv043.getDescripcion().equals(proyecto.getDescripcion())) {
			cambio.append("descripcion");
			proyecto.setDescripcion(rqsv043.getDescripcion());
			esElPrimerCambio = true;
		}
		if(!rqsv043.getInicio().equals(proyecto.getInicio())) {
			if(!esElPrimerCambio) {
				cambio.append(", ");
			}
			esElPrimerCambio = true;
			cambio.append("fecha de inicio");
			proyecto.setInicio(rqsv043.getInicio());
		}
		if(!rqsv043.getFin().equals(proyecto.getFin())) {
			if(!esElPrimerCambio) {
				cambio.append(", ");
			}
			esElPrimerCambio = true;
			cambio.append("fecha de fin");
			proyecto.setFin(rqsv043.getFin());
		}
		if(!rqsv043.getNombre().equals(proyecto.getNombre())) {
			if(!esElPrimerCambio) {
				cambio.append(", ");
			}
			esElPrimerCambio = true;
			cambio.append("nombre");
			proyecto.setNombre(rqsv043.getNombre());
		}
		if(!rqsv043.getPrioridad().equals(proyecto.getPrioridad().getClave())) {
			if(!esElPrimerCambio) {
				cambio.append(", ");
			}
			esElPrimerCambio = true;
			cambio.append("prioridad");
			Optional<Catalogo> optional = prioridad.getCatalogos().stream().filter(x->x.getClave().equals(rqsv043.getPrioridad())).findAny();
			if(optional.isEmpty()) {
				return rsb035.notFound("No se encontro la prioridad a actualizar");
			}
			proyecto.setPrioridad(optional.get());
		}

        if(!rqsv043.getRevisor().equals(proyecto.getRevisor().getClave())) {
        	if(!esElPrimerCambio) {
				cambio.append(", ");
			}
        	esElPrimerCambio = true;
            cambio.append("revisor");
            Optional<Trabajador> optional = trabajadorRepository.findByClave(rqsv043.getRevisor());
            if(optional.isEmpty()) {
                return rsb035.notFound("No se encontro el revisor a actualizar");
            }
            proyecto.setRevisor(optional.get());
        }
        
        if(!rqsv043.getTrabajador().equals(proyecto.getTrabajador().getClave())) {
        	if(!esElPrimerCambio) {
				cambio.append(", ");
			}
        	esElPrimerCambio = true;
            cambio.append("responsable");
            Optional<Trabajador> optionalResponsable = trabajadorRepository.findByClave(rqsv043.getTrabajador());
            if(optionalResponsable.isEmpty()) {
                return rsb035.notFound("No se encontro el responsable a actualizar");
            }
            proyecto.setTrabajador(optionalResponsable.get());
        }
        
        if(proyecto.getTrabajador().equals(proyecto.getRevisor())) {
            return rsb035.badRequest("El revisor y responsable no pueden ser los mismos");
        }

        if(!esElPrimerCambio) {
			return rsb035.badRequest("No hay cambios");
		}
		
		Trabajador trabajador = trabajadorRepository.findByClaveUsuario(sesion.getUsuario().getClave()).orElse(null);
		if(trabajador == null) {
			return rsb035.notFound("No se encontro el trabjador.");
		}
		
		builder.append(cambio);
		proyecto.setIdUsuarioModificado(sesion.getUsuario().getId());
		proyecto.setFechaModificado(LocalDateTime.now());
		proyectoRepository.save(proyecto);

		ProyectoComentario comentario = new ProyectoComentario();
		comentario.setFechaModificado(LocalDateTime.now());
		comentario.setUsuarioCreador(sesion.getUsuario().getId());
		comentario.setClave(UUID.randomUUID());
		comentario.setComentario(builder.toString());
		comentario.setFechaCreacion(LocalDateTime.now());
		comentario.setFechaModificado(LocalDateTime.now());
		comentario.setIdUsuarioModificado(sesion.getUsuario().getId());
		comentario.setTrabajador(trabajador);
		comentario.setProyecto(proyecto);
		comentario.setEstatus(proyecto.getEstatus());
		proyectoComentarioRepository.save(comentario);
		return rsb035.ok(proyecto);
	}
}
