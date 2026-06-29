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
	private final Catalogo activo;

	public Proyecto006BZ(TrabajadorRepository trabajadorRepository, ProyectoRepository proyectoRepository,
			ProyectoComentarioRepository proyectoComentarioRepository, CatalogoTipo prioridad, Catalogo activo) {
		super();
		this.trabajadorRepository = trabajadorRepository;
		this.proyectoRepository = proyectoRepository;
		this.proyectoComentarioRepository = proyectoComentarioRepository;
		this.prioridad = prioridad;
	    this.activo = activo;
	}


	public ResponseEntity<RSB035> actulizar(final RQSV043 rqsv043) {
		RSB035 rsb035 = rqsv043.getRsb035();
		Sesion sesion = rqsv043.getSesion();
		Optional<Proyecto> proyectoOp = proyectoRepository.findByClave(rqsv043.getProyecto());
		if(proyectoOp.isEmpty()) {
			return rsb035.notFound("No se encontro el proyecto");
		}
		Proyecto proyecto = proyectoOp.get();
		
		
		StringBuilder comentarioCambio = new StringBuilder();
		
		if (rqsv043.getNombre() != null && !rqsv043.getNombre().equals(proyecto.getNombre())) {
            comentarioCambio.append("Se cambió el nombre de ")
                    .append("**"+proyecto.getNombre()+"**")
                    .append(" a ")
                    .append("**"+rqsv043.getNombre()+"**")
                    .append(".\n\n");

            proyecto.setNombre(rqsv043.getNombre());
        }
		
		if (rqsv043.getDescripcion() != null && !rqsv043.getDescripcion().equals(proyecto.getDescripcion())) {
            comentarioCambio.append("Se cambió la descripción de ")
                    .append("**"+proyecto.getDescripcion()+"**")
                    .append(" a ")
                    .append("**"+rqsv043.getDescripcion()+"**")
                    .append(".\n\n");

            proyecto.setDescripcion(rqsv043.getDescripcion());
        }
		
		if (rqsv043.getPrioridad() != null && !rqsv043.getPrioridad().equals(proyecto.getPrioridad().getClave())) {

            Optional<Catalogo> optional = prioridad.getCatalogos().stream()
                    .filter(x -> x.getClave().equals(rqsv043.getPrioridad()))
                    .findAny();

            if (optional.isEmpty()) {
                return rsb035.notFound("No se encontro la prioridad a actualizar");
            }

            Catalogo prioridadAnterior = proyecto.getPrioridad();
            Catalogo prioridadNueva = optional.get();

            comentarioCambio.append("Se cambió la prioridad de ")
                    .append("**"+prioridadAnterior.getNombre()+"**")
                    .append(" a ")
                    .append("**"+prioridadNueva.getNombre()+"**")
                    .append(".\n\n");

            proyecto.setPrioridad(prioridadNueva);
        }
		
		if (rqsv043.getTrabajador() != null && !rqsv043.getTrabajador().equals(proyecto.getTrabajador().getClave())) {

            Optional<Trabajador> optionalResponsable = trabajadorRepository.findByClave(rqsv043.getTrabajador());

            if (optionalResponsable.isEmpty()) {
                return rsb035.notFound("No se encontro el responsable a actualizar");
            }

            Trabajador responsableAnterior = proyecto.getTrabajador();
            Trabajador responsableNuevo = optionalResponsable.get();

            comentarioCambio.append("Se cambió el responsable de ")
                    .append("**"+responsableAnterior.getPersonaFisica().getNombre()+"**")
                    .append(" a ")
                    .append("**"+responsableNuevo.getPersonaFisica().getNombre()+"**")
                    .append(".\n\n");

            proyecto.setTrabajador(responsableNuevo);
        }

        if (rqsv043.getRevisor() != null && !rqsv043.getRevisor().equals(proyecto.getRevisor().getClave())) {

            Optional<Trabajador> optionalRevisor = trabajadorRepository.findByClave(rqsv043.getRevisor());

            if (optionalRevisor.isEmpty()) {
                return rsb035.notFound("No se encontro el revisor a actualizar");
            }

            Trabajador revisorAnterior = proyecto.getRevisor();
            Trabajador revisorNuevo = optionalRevisor.get();

            comentarioCambio.append("Se cambió el responsable de ")
                    .append("**"+revisorAnterior.getPersonaFisica().getNombre()+"**")
                    .append(" a ")
                    .append("**"+revisorNuevo.getPersonaFisica().getNombre()+"**")
                    .append(".\n\n");

            proyecto.setRevisor(revisorNuevo);
        }
		
		if (!rqsv043.getInicio().equals(proyecto.getInicio())) {
            comentarioCambio.append("Se cambió la fecha inicio de ")
                    .append("**"+proyecto.getInicio()+"**")
                    .append(" a ")
                    .append("**"+rqsv043.getInicio()+"**")
                    .append(".\n\n");

            proyecto.setInicio(rqsv043.getInicio());
        }

        if (!rqsv043.getFin().equals(proyecto.getFin())) {
            comentarioCambio.append("Se cambió la fecha fin de ")
                    .append("**"+proyecto.getFin()+"**")
                    .append(" a ")
                    .append("**"+rqsv043.getFin()+"**")
                    .append(".\n\n");

            proyecto.setFin(rqsv043.getFin());
        }
        
        if(proyecto.getTrabajador().equals(proyecto.getRevisor())) {
            return rsb035.badRequest("El revisor y responsable no pueden ser los mismos");
        }
        if (comentarioCambio.isEmpty()) {
            return rsb035.badRequest("No hay cambios");
        }


		Trabajador trabajador = trabajadorRepository.findByClaveUsuario(sesion.getUsuario().getClave()).orElse(null);
		if(trabajador == null) {
			return rsb035.notFound("No se encontro el trabjador.");
		}

		proyecto.setIdUsuarioModificado(sesion.getUsuario().getId());
		proyecto.setFechaModificado(LocalDateTime.now());
		proyectoRepository.save(proyecto);

		ProyectoComentario comentario = new ProyectoComentario();
		comentario.setFechaModificado(LocalDateTime.now());
		comentario.setUsuarioCreador(sesion.getUsuario().getId());
		comentario.setClave(UUID.randomUUID());
		comentario.setComentario(comentarioCambio.toString().trim());
		comentario.setFechaCreacion(LocalDateTime.now());
		comentario.setFechaModificado(LocalDateTime.now());
		comentario.setIdUsuarioModificado(sesion.getUsuario().getId());
		comentario.setTrabajador(trabajador);
		comentario.setProyecto(proyecto);
		comentario.setEstatus(activo);
		proyectoComentarioRepository.save(comentario);
		return rsb035.ok(proyecto);
	}
}
