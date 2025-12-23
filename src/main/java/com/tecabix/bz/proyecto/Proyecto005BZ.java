package com.tecabix.bz.proyecto;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.ResponseEntity;

import com.tecabix.db.entity.Catalogo;
import com.tecabix.db.entity.Proyecto;
import com.tecabix.db.entity.ProyectoComentario;
import com.tecabix.db.entity.Sesion;
import com.tecabix.db.entity.Usuario;
import com.tecabix.db.repository.CatalogoRepository;
import com.tecabix.db.repository.ProyectoComentarioRepository;
import com.tecabix.db.repository.ProyectoRepository;
import com.tecabix.res.b.RSB034;
import com.tecabix.sv.rq.RQSV042;

/**
*
* @author Ramirez Urrutia Angel Abinadi
*/
public class Proyecto005BZ {
	
	private final CatalogoRepository catalogoRepository;
	private final ProyectoRepository proyectoRepository;
	private final ProyectoComentarioRepository proyectoComentarioRepository;

	private final Catalogo porHacer;
	private final Catalogo enProceso;
	private final Catalogo enRevision;
	private final Catalogo listo;
	private final Catalogo enPausa;
	private final Catalogo bloqueado;
	private final Catalogo conObservaciones;
	
	private Usuario usuario;
	
	public Proyecto005BZ(CatalogoRepository catalogoRepository, ProyectoRepository proyectoRepository,
			ProyectoComentarioRepository proyectoComentarioRepository, Catalogo porHacer, Catalogo enProceso,
			Catalogo enRevision, Catalogo listo, Catalogo enPausa, Catalogo bloqueado, Catalogo conObservaciones,
			Usuario usuario) {
		super();
		this.catalogoRepository = catalogoRepository;
		this.proyectoRepository = proyectoRepository;
		this.proyectoComentarioRepository = proyectoComentarioRepository;
		this.porHacer = porHacer;
		this.enProceso = enProceso;
		this.enRevision = enRevision;
		this.listo = listo;
		this.enPausa = enPausa;
		this.bloqueado = bloqueado;
		this.conObservaciones = conObservaciones;
		this.usuario = usuario;
	}

	public ResponseEntity<RSB034> actualizarEstatus(final RQSV042 rqsv042) {
		RSB034 rsb034 = rqsv042.getRsb034();
		Sesion sesion = rqsv042.getSesion();
		
		Optional<Catalogo> estatusOp = catalogoRepository.findByClave(rqsv042.getEtapa());
		if(estatusOp.isEmpty()) {
			return rsb034.notFound("No se encontro la etapa");
		}
		Optional<Proyecto> proyectoOp = proyectoRepository.findByClave(rqsv042.getProyecto());
		if(proyectoOp.isEmpty()) {
			return rsb034.notFound("No se encontro el proyecto");
		}
		Catalogo estatus = estatusOp.get();
		Proyecto proyecto = proyectoOp.get();

		if(estatus.equals(porHacer)) {
			if(!proyecto.getEstatus().equals(enProceso)) {
				return rsb034.badRequest("No se puede cambiar el estatus.");
			}
		} else if(estatus.equals(enProceso)) {
			if(!proyecto.getEstatus().equals(porHacer) && !proyecto.getEtapa().equals(conObservaciones) && !proyecto.getEtapa().equals(enPausa) && !proyecto.getEtapa().equals(bloqueado)) {
				return rsb034.badRequest("No se puede cambiar el estatus.");
			}
		} else if(estatus.equals(enRevision)) {
			if(!proyecto.getEstatus().equals(enProceso)) {
				return rsb034.badRequest("No se puede cambiar el estatus.");
			}
		} else if(estatus.equals(listo)) {
			if(!proyecto.getEtapa().equals(enRevision) || !sesion.getUsuario().equals(proyecto.getRevisor())) {
				return rsb034.badRequest("No se puede cambiar el estatus.");
			}
		} else if(estatus.equals(enPausa)) {
			if(!proyecto.getEstatus().equals(enProceso)) {
				return rsb034.badRequest("No se puede cambiar el estatus.");
			}
		} else if(estatus.equals(bloqueado)) {
			if(!proyecto.getEstatus().equals(enProceso)) {
				return rsb034.badRequest("No se puede cambiar el estatus.");
			}
		} else if(estatus.equals(conObservaciones)) {
			if(!proyecto.getEtapa().equals(enRevision) || !sesion.getUsuario().equals(proyecto.getRevisor())) {
				return rsb034.badRequest("No se puede cambiar el estatus.");
			}
		} else {
			return rsb034.badRequest("No se puede cambiar el estatus.");
		}
		
		String estatusViejo = proyecto.getEtapa().getNombre();
		
		proyecto.setEstatus(estatus);
		String estatusNuevo = proyecto.getEtapa().getNombre();
		proyecto.setIdUsuarioModificado(sesion.getUsuario().getId());
		proyecto.setFechaModificado(LocalDateTime.now());
		proyectoRepository.save(proyecto);
		ProyectoComentario comentario = new ProyectoComentario();
		comentario.setFechaModificado(LocalDateTime.now());
		comentario.setUsuarioCreador(sesion.getUsuario().getId());
		comentario.setClave(UUID.randomUUID());
		comentario.setComentario("El usuario ["+sesion.getUsuario().getNombre()+"|"+sesion.getUsuario().getClave()+"] cambio el estatus de "+estatusViejo+" a "+estatusNuevo+".");
		comentario.setFechaCreacion(LocalDateTime.now());
		comentario.setFechaModificado(LocalDateTime.now());
		comentario.setIdUsuarioModificado(sesion.getUsuario().getId());
		comentario.setUsuarioCreador(sesion.getUsuario().getId());
		comentario.setUsuario(usuario);
		comentario.setProyecto(proyecto);
		comentario.setEstatus(proyecto.getEstatus());
		proyectoComentarioRepository.save(comentario);
		return rsb034.ok(proyecto);
	}
}
