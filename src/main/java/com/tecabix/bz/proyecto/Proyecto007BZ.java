package com.tecabix.bz.proyecto;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.ResponseEntity;

import com.tecabix.db.entity.Catalogo;
import com.tecabix.db.entity.Proyecto;
import com.tecabix.db.entity.ProyectoComentario;
import com.tecabix.db.entity.Sesion;
import com.tecabix.db.repository.ProyectoComentarioRepository;
import com.tecabix.db.repository.ProyectoRepository;
import com.tecabix.res.b.RSB038;
import com.tecabix.sv.rq.RQSV044;

public class Proyecto007BZ {

	private final ProyectoComentarioRepository proyectoComentarioRepository;
	private final ProyectoRepository proyectoRepository;
	private final Catalogo activo;

	public Proyecto007BZ(ProyectoComentarioRepository proyectoComentarioRepository,
			ProyectoRepository proyectoRepository, Catalogo activo) {
		super();
		this.proyectoComentarioRepository = proyectoComentarioRepository;
		this.proyectoRepository = proyectoRepository;
		this.activo = activo;
	}

	public ResponseEntity<RSB038> crearComentario(RQSV044 rqsv043) {
		RSB038 response = rqsv043.getRsb038();
		Sesion sesion = rqsv043.getSesion();
		ProyectoComentario comentario = new ProyectoComentario();
		comentario.setComentario(rqsv043.getComentario());
		Optional<Proyecto> op = proyectoRepository.findByClave(rqsv043.getProyecto());
		op.ifPresent(x->comentario.setProyecto(x));
		if(comentario.getProyecto() == null) {
			return response.notFound("NO SE ENCONTRO EL PROYECTO");
		}
		comentario.setUsuario(sesion.getUsuario());
		comentario.setUsuarioCreador(sesion.getUsuario().getId());
		comentario.setClave(UUID.randomUUID());
		comentario.setEstatus(activo);
		comentario.setFechaCreacion(LocalDateTime.now());
		comentario.setFechaModificado(LocalDateTime.now());
		comentario.setIdUsuarioModificado(sesion.getUsuario().getId());
		proyectoComentarioRepository.save(comentario);
		return response.ok(comentario);
	}
}
