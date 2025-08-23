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

public class Proyecto005BZ {
	
	private CatalogoRepository catalogoRepository;
	private ProyectoRepository proyectoRepository;
	private ProyectoComentarioRepository proyectoComentarioRepository;

	private Catalogo POR_HACER;
	private Catalogo EN_PROCESO;
	private Catalogo EN_REVISION;
	private Catalogo LISTO;
	private Catalogo EN_PAUSA;
	private Catalogo BLOQUEADO;
	private Catalogo CON_OBSERVACIONES;
	
	private Usuario usuario;
	
	public ResponseEntity<RSB034> actulizarEstatus(final RQSV042 rqsv042) {
		RSB034 rsb032 = rqsv042.getRsb034();
		Sesion sesion = rqsv042.getSesion();
		
		Optional<Catalogo> estatusOp = catalogoRepository.findByClave(rqsv042.getEtapa());
		if(estatusOp.isEmpty()) {
			return rsb032.notFound("No se encontro la etapa");
		}
		Optional<Proyecto> proyectoOp = proyectoRepository.findByClave(rqsv042.getProyecto());
		if(proyectoOp.isEmpty()) {
			return rsb032.notFound("No se encontro el proyecto");
		}
		Catalogo estatus = estatusOp.get();
		Proyecto proyecto = proyectoOp.get();
		
		if(estatus.equals(POR_HACER)) {
			if(!proyecto.getEstatus().equals(EN_PROCESO)) {
				return rsb032.badRequest("No se pude cambiar la etapa.");
			}
		} else if(estatus.equals(EN_PROCESO)) {
			if(!proyecto.getEstatus().equals(POR_HACER) && !proyecto.getEtapa().equals(CON_OBSERVACIONES) && !proyecto.getEtapa().equals(EN_PAUSA) && !proyecto.getEtapa().equals(BLOQUEADO)) {
				return rsb032.badRequest("No se pude cambiar la etapa.");
			}
		} else if(estatus.equals(EN_REVISION)) {
			if(!proyecto.getEstatus().equals(EN_PROCESO)) {
				return rsb032.badRequest("No se pude cambiar la etapa.");
			}
		} else if(estatus.equals(LISTO)) {
			if(!proyecto.getEtapa().equals(EN_REVISION) || !sesion.getUsuario().equals(proyecto.getRevisor())) {
				return rsb032.badRequest("No se pude cambiar la etapa.");
			}
		} else if(estatus.equals(EN_PAUSA)) {
			if(!proyecto.getEtapa().equals(EN_PROCESO)) {
				return rsb032.badRequest("No se pude cambiar la etapa.");
			}
		} else if(estatus.equals(BLOQUEADO)) {
			if(!proyecto.getEtapa().equals(EN_PROCESO)) {
				return rsb032.badRequest("No se pude cambiar la etapa.");
			}
		} else if(estatus.equals(CON_OBSERVACIONES)) {
			if(!proyecto.getEtapa().equals(EN_REVISION) || !sesion.getUsuario().equals(proyecto.getRevisor())) {
				return rsb032.badRequest("No se pude cambiar la etapa.");
			}
		} else {
			return rsb032.badRequest("No se pude cambiar la etapa.");
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
		comentario.setIdProyecto(proyecto.getId());
		proyectoComentarioRepository.save(comentario);
		return rsb032.ok(proyecto);
	}
}
