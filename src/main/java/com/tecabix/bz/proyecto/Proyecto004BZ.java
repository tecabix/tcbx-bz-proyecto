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

/**
*
* @author Ramirez Urrutia Angel Abinadi
*/
public class Proyecto004BZ {
	
	private final CatalogoRepository catalogoRepository;
	private final ProyectoRepository proyectoRepository;
	private final ProyectoComentarioRepository proyectoComentarioRepository;

	private final Catalogo nuevo;
	private final Catalogo analisis;
	private final Catalogo desarrollo;
	private final Catalogo construccion;
	private final Catalogo prueba;
	private final Catalogo calidad;
	private final Catalogo produccion;
	private final Catalogo liberado;
	private final Catalogo descartado;
	
	private final Catalogo porHacer;
	private final Catalogo listo;
	
	private final Usuario usuario;
	
	

	public Proyecto004BZ(CatalogoRepository catalogoRepository, ProyectoRepository proyectoRepository,
			ProyectoComentarioRepository proyectoComentarioRepository, Catalogo nuevo, Catalogo analisis,
			Catalogo desarrollo, Catalogo construccion, Catalogo prueba, Catalogo calidad, Catalogo produccion,
			Catalogo liberado, Catalogo descartado, Catalogo porHacer, Catalogo listo, Usuario usuario) {
		this.catalogoRepository = catalogoRepository;
		this.proyectoRepository = proyectoRepository;
		this.proyectoComentarioRepository = proyectoComentarioRepository;
		this.nuevo = nuevo;
		this.analisis = analisis;
		this.desarrollo = desarrollo;
		this.construccion = construccion;
		this.prueba = prueba;
		this.calidad = calidad;
		this.produccion = produccion;
		this.liberado = liberado;
		this.descartado = descartado;
		this.porHacer = porHacer;
		this.listo = listo;
		this.usuario = usuario;
	}



	public ResponseEntity<RSB033> actulizarEtapa(final RQSV041 rqsv041) {
		RSB033 rsb032 = rqsv041.getRsb033();
		Sesion sesion = rqsv041.getSesion();
		Optional<Catalogo> etapaOp = catalogoRepository.findByClave(rqsv041.getEtapa());
		if(etapaOp.isEmpty()) {
			return rsb032.notFound("No se encontro la etapa");
		}
		Optional<Proyecto> proyectoOp = proyectoRepository.findByClave(rqsv041.getProyecto());
		if(proyectoOp.isEmpty()) {
			return rsb032.notFound("No se encontro el proyecto");
		}
		Catalogo etapa = etapaOp.get();
		Proyecto proyecto = proyectoOp.get();
		
		if(etapa.equals(nuevo)) {
			if(!proyecto.getEtapa().equals(analisis)) {
				return rsb032.badRequest("No se pude cambiar la etapa.");
			}
		} else if(etapa.equals(analisis)) {
			if(!proyecto.getEtapa().equals(nuevo) && !proyecto.getEtapa().equals(desarrollo)) {
				return rsb032.badRequest("No se pude cambiar la etapa.");
			}
			if(proyecto.getEtapa().equals(nuevo) && !proyecto.getEstatus().equals(listo)) {
				return rsb032.badRequest("No se pude cambiar la etapa si el proyecto no esta en listo.");
			}
		} else if(etapa.equals(desarrollo)) {
			if(!proyecto.getEtapa().equals(analisis) && !proyecto.getEtapa().equals(construccion) && !proyecto.getEtapa().equals(prueba) && !proyecto.getEtapa().equals(calidad)) {
				return rsb032.badRequest("No se pude cambiar la etapa.");
			}
			if(proyecto.getEtapa().equals(analisis) && !proyecto.getEstatus().equals(listo)) {
				return rsb032.badRequest("No se pude cambiar la etapa si el proyecto no esta en listo.");
			}
		} else if(etapa.equals(construccion)) {
			if(!proyecto.getEtapa().equals(desarrollo) && !proyecto.getEtapa().equals(prueba)) {
				return rsb032.badRequest("No se pude cambiar la etapa.");
			}
			if(proyecto.getEtapa().equals(desarrollo) && !proyecto.getEstatus().equals(listo)) {
				return rsb032.badRequest("No se pude cambiar la etapa si el proyecto no esta en listo.");
			}
		} else if(etapa.equals(prueba)) {
			if(!proyecto.getEtapa().equals(construccion) && !proyecto.getEtapa().equals(calidad)) {
				return rsb032.badRequest("No se pude cambiar la etapa.");
			}
			if(proyecto.getEtapa().equals(construccion) && !proyecto.getEstatus().equals(listo)) {
				return rsb032.badRequest("No se pude cambiar la etapa si el proyecto no esta en listo.");
			}
		} else if(etapa.equals(calidad)) {
			if(!proyecto.getEtapa().equals(prueba) && !proyecto.getEtapa().equals(produccion)) {
				return rsb032.badRequest("No se pude cambiar la etapa.");
			}
			if(proyecto.getEtapa().equals(prueba) && !proyecto.getEstatus().equals(listo)) {
				return rsb032.badRequest("No se pude cambiar la etapa si el proyecto no esta en listo.");
			}
		} else if(etapa.equals(produccion)) {
			if(!proyecto.getEtapa().equals(calidad) && !proyecto.getEtapa().equals(liberado)) {
				return rsb032.badRequest("No se pude cambiar la etapa.");
			}
			if(proyecto.getEtapa().equals(calidad) && !proyecto.getEstatus().equals(listo)) {
				return rsb032.badRequest("No se pude cambiar la etapa si el proyecto no esta en listo.");
			}
		} else if(etapa.equals(liberado)) {
			if(!proyecto.getEtapa().equals(produccion) || !proyecto.getEstatus().equals(listo)) {
				return rsb032.badRequest("No se pude cambiar la etapa.");
			}
		} else if(!etapa.equals(descartado)) {
			return rsb032.badRequest("No se pude cambiar la etapa.");
		}
		if(!etapa.equals(descartado)) {
			proyecto.setEstatus(porHacer);
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
		comentario.setComentario("El usuario ["+sesion.getUsuario().getNombre()+"|"+sesion.getUsuario().getClave()+"] cambio la etapa de "+etapaVieja+" a "+etapaNueva+".");
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
