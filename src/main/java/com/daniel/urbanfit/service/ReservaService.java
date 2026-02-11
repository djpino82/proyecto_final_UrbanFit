package com.daniel.urbanfit.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.daniel.urbanfit.entity.Horario;
import com.daniel.urbanfit.entity.Reserva;
import com.daniel.urbanfit.entity.Usuario;
import com.daniel.urbanfit.repository.HorarioRepository;
import com.daniel.urbanfit.repository.ReservaRepository;
import com.daniel.urbanfit.repository.UsuarioRepository;

@Service
public class ReservaService {
	
	@Autowired
	private ReservaRepository reservaRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private HorarioRepository horarioRepository;
	
	// Obtener reservas de un Usuario
	public List<Reserva> obtenerReservaPorUsuario (Long usuarioId){
		
		return reservaRepository.findByUsuarioId(usuarioId);
		
	}
	
	// Crear nueva reserva
	public Reserva crearReseva(Long usuarioId, Long horarioId, LocalDate fechaclase) {
		
		// Usuario y Horario desde la BD
		Usuario usuario = usuarioRepository.findById(horarioId).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
		
		Horario horario = horarioRepository.findById(horarioId).orElseThrow(() -> new IllegalArgumentException("Horario no encontrado"));
		
		// Comprobar si ya tien reserva para essa fecha y horario
		boolean existe = reservaRepository.existsByUsuarioAndHorariosAndFechaClase(usuario, horario, fechaclase);
		
		if (existe) {
			throw new IllegalArgumentException("Ya tienes reserva para este dia y hora");
			
		}
		
		// Creamos la reserva
		Reserva reserva = new Reserva();
		reserva.setUsuario(usuario);
		reserva.setHorario(horario);
		reserva.setFechaClase(fechaclase);
		reserva.setActiva(true);
		reserva.setAsistenciaConfirmada(false);
		
		// Guardamos en BD
		return reservaRepository.save(reserva);
		
		
	}
	
	

}
