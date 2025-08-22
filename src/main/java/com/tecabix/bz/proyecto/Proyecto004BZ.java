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
import com.tecabix.res.b.RSB033;
import com.tecabix.sv.rq.RQSV041;

public class Proyecto004BZ {
	
	private CatalogoRepository catalogoRepository;
	private ProyectoRepository proyectoRepository;
	private ProyectoComentarioRepository proyectoComentarioRepository;

	private Catalogo POR_HACER;
	private Catalogo ANALISIS;
	private Catalogo DESARROLLO;
	private Catalogo CONSTRUCCION;
	private Catalogo PRUEBA;
	private Catalogo QA;
	private Catalogo PRODUCCION;
	private Catalogo LISTO;
	private Catalogo DESCARTADO;
	
	private Usuario usuario;
	
	public ResponseEntity<RSB033> actulizarEtapa(final RQSV041 rqsv040) {
		RSB033 rsb032 = rqsv040.getRsb033();
		Sesion sesion = rqsv040.getSesion();
		Optional<Catalogo> etapaOp = catalogoRepository.findByClave(rqsv040.getEtapa());
		if(etapaOp.isEmpty()) {
			return rsb032.notFound("No se encontro la etapa");
		}
		Optional<Proyecto> proyectoOp = proyectoRepository.findByClave(rqsv040.getProyecto());
		if(proyectoOp.isEmpty()) {
			return rsb032.notFound("No se encontro el proyecto");
		}
		Catalogo etapa = etapaOp.get();
		Proyecto proyecto = proyectoOp.get();
		
		if(etapa.equals(POR_HACER)) {
			if(!proyecto.getEtapa().equals(ANALISIS)) {
				return rsb032.badRequest("No se pude cambiar el estatus.");
			}
		} else if(etapa.equals(ANALISIS)) {
			if(!proyecto.getEtapa().equals(POR_HACER) || !proyecto.getEtapa().equals(DESARROLLO)) {
				return rsb032.badRequest("No se pude cambiar el estatus.");
			}
		} else if(etapa.equals(DESARROLLO)) {
			if(!proyecto.getEtapa().equals(ANALISIS) || !proyecto.getEtapa().equals(CONSTRUCCION)) {
				return rsb032.badRequest("No se pude cambiar el estatus.");
			}
		} else if(etapa.equals(CONSTRUCCION)) {
			if(!proyecto.getEtapa().equals(DESARROLLO) || !proyecto.getEtapa().equals(PRUEBA)) {
				return rsb032.badRequest("No se pude cambiar el estatus.");
			}
		} else if(etapa.equals(PRUEBA)) {
			if(!proyecto.getEtapa().equals(CONSTRUCCION) || !proyecto.getEtapa().equals(QA)) {
				return rsb032.badRequest("No se pude cambiar el estatus.");
			}
		} else if(etapa.equals(QA)) {
			if(!proyecto.getEtapa().equals(PRUEBA) || !proyecto.getEtapa().equals(PRODUCCION)) {
				return rsb032.badRequest("No se pude cambiar el estatus.");
			}
		} else if(etapa.equals(PRODUCCION)) {
			if(!proyecto.getEtapa().equals(QA) || !proyecto.getEtapa().equals(LISTO)) {
				return rsb032.badRequest("No se pude cambiar el estatus.");
			}
		} else if(etapa.equals(LISTO)) {
			if(!proyecto.getEtapa().equals(PRODUCCION)) {
				return rsb032.badRequest("No se pude cambiar el estatus.");
			}
		} else if(!etapa.equals(DESCARTADO)) {
			return rsb032.badRequest("No se pude cambiar el estatus.");
		}
		String etapaVieja = proyecto.getEtapa().getNombre();
		
		proyecto.setEtapa(etapa);
		String etapaNueva = proyecto.getEtapa().getNombre();
		proyecto.setIdUsuarioModificado(sesion.getUsuario().getId());
		proyecto.setFechaModificado(LocalDateTime.now());
		proyectoRepository.save(proyecto);
		ProyectoComentario comentario = new ProyectoComentario();
		comentario.setFechaModificado(LocalDateTime.now());
		comentario.setUsuarioCreador(sesion.getUsuario().getId());
		comentario.setClave(UUID.randomUUID());
		comentario.setComentario("El usuario ["+sesion.getUsuario().getNombre()+"|"+sesion.getUsuario().getClave()+"] cambio el estatus de "+etapaVieja+" a "+etapaNueva+".");
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
