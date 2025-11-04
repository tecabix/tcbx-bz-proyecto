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
import com.tecabix.db.entity.Usuario;
import com.tecabix.db.repository.ProyectoComentarioRepository;
import com.tecabix.db.repository.ProyectoRepository;
import com.tecabix.db.repository.UsuarioRepository;
import com.tecabix.res.b.RSB035;
import com.tecabix.sv.rq.RQSV043;

/**
*
* @author Ramirez Urrutia Angel Abinadi
*/
public class Proyecto006BZ {

	private final UsuarioRepository usuarioRepository;
	private final ProyectoRepository proyectoRepository;
	private final ProyectoComentarioRepository proyectoComentarioRepository;
	private final CatalogoTipo prioridad;
	private final Usuario usuario;
	
	public Proyecto006BZ(UsuarioRepository usuarioRepository, ProyectoRepository proyectoRepository,
			ProyectoComentarioRepository proyectoComentarioRepository, CatalogoTipo prioridad, Usuario usuario) {
		this.usuarioRepository = usuarioRepository;
		this.proyectoRepository = proyectoRepository;
		this.proyectoComentarioRepository = proyectoComentarioRepository;
		this.prioridad = prioridad;
		this.usuario = usuario;
	}


	public ResponseEntity<RSB035> actulizar(final RQSV043 rqsv043) {
		RSB035 rsb035 = rqsv043.getRsb035();
		Sesion sesion = rqsv043.getSesion();
		Optional<Proyecto> proyectoOp = proyectoRepository.findByClave(rqsv043.getProyecto());
		if(proyectoOp.isEmpty()) {
			return rsb035.notFound("No se encontro el proyecto");
		}
		Proyecto proyecto = proyectoOp.get();
		StringBuilder builder = new StringBuilder("El usuario ["+sesion.getUsuario().getNombre()+"|"+sesion.getUsuario().getClave()+"]");
		StringBuilder cambio = new StringBuilder();
		if(!rqsv043.getDescripcion().equals(proyecto.getDescripcion())) {
			cambio.append(", cambio la descripcion");
			proyecto.setDescripcion(rqsv043.getDescripcion());
		}
		if(!rqsv043.getInicio().equals(proyecto.getInicio())) {
			cambio.append(", cambio la fecha de inicio");
			proyecto.setInicio(rqsv043.getInicio());
		}
		if(!rqsv043.getFin().equals(proyecto.getFin())) {
			cambio.append(", cambio la fecha de fin");
			proyecto.setFin(rqsv043.getFin());
		}
		if(!rqsv043.getNombre().equals(proyecto.getNombre())) {
			cambio.append(", cambio el nombre");
			proyecto.setNombre(rqsv043.getNombre());
		}
		if(!rqsv043.getPrioridad().equals(proyecto.getPrioridad().getClave())) {
			cambio.append(", cambio la prioridad");
			Optional<Catalogo> optional = prioridad.getCatalogos().stream().filter(x->x.getClave() == rqsv043.getPrioridad()).findAny();
			if(optional.isEmpty()) {
				return rsb035.notFound("No se encontro la prioridad a actualizar");
			}
			proyecto.setPrioridad(optional.get());
		}
		if(!rqsv043.getRevisor().equals(proyecto.getRevisor().getClave())) {
			cambio.append(", cambio el revisor");
			Optional<Usuario> optional = usuarioRepository.findByClave(rqsv043.getRevisor());
			if(optional.isEmpty()) {
				return rsb035.notFound("No se encontro el usuario a actualizar");
			}
			proyecto.setRevisor(optional.get());
		}
		if(cambio.isEmpty()) {
			return rsb035.badRequest("No hay cambios");
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
		comentario.setUsuarioCreador(sesion.getUsuario().getId());
		comentario.setUsuario(usuario);
		comentario.setIdProyecto(proyecto.getId());
		comentario.setEstatus(proyecto.getEstatus());
		proyectoComentarioRepository.save(comentario);
		return rsb035.ok(proyecto);
	}
}
