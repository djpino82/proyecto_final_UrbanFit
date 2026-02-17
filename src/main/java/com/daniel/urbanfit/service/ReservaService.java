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
	public Reserva crearReserva(Long usuarioId, Long horarioId, LocalDate fechaclase) {
		
		// Usuario y Horario desde la BD
		Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
		
		Horario horario = horarioRepository.findById(horarioId).orElseThrow(() -> new IllegalArgumentException("Horario no encontrado"));
		
		// Comprobar si ya tien reserva para essa fecha y horario
		boolean existe = reservaRepository.existsByUsuarioAndHorarioAndFechaClase(usuario, horario, fechaclase);
		
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
	
	// Marcar una reseva como cancelada (NO nos interesa borrarla de la BD para poder disponer de estos datos)
	public void cancelarReserva(Long reservaId) {
		
		Reserva reserva = reservaRepository.findById(reservaId).orElseThrow(() -> new IllegalArgumentException("No se encontró la reserva por ID"));
		
		reserva.setActiva(false); // marcamos como cancelada
		reservaRepository.save(reserva);
			
		
		
	}
	
	// Obtener historial de reservas por usuario
	public List<Reserva> obtenerHistorialPorUsuario(Long usuarioId){
		return reservaRepository.findByUsuarioIdAndFechaClaseBefore(usuarioId, LocalDate.now());
	}
	
	// Obtener reservas Activas ordenadas por fechas de mas actual a menos
	public List<Reserva> obtenerReservasActivasPorUsuario(Long usuarioId) {
		return reservaRepository.findByUsuarioIdAndActivaOrderByFechaClaseDesc(usuarioId, true);
	}
	
	// Obtener reservas Canceladas ordenadas por fechas de mas actual a menos
	public List<Reserva> obtenerReservasCanceladasPorUsuario(Long usuarioId) {
		return reservaRepository.findByUsuarioIdAndActivaOrderByFechaClaseDesc(usuarioId, false);
	}
		
	// Obtener reservas Activas ordenadas por fechas de mas antigua a mas actual. La usamos para listar las reservas de un usuario.
	public List<Reserva> obtenerReservasMasProximaPorUsuario(Long usuarioId) {
		return reservaRepository.findByUsuarioIdAndActivaOrderByFechaClaseAsc(usuarioId, true);
	}
	
	//Esto filtra automáticamente las reservas para que no se muestren las que ya pasaron ni las canceladas.
	public List<Reserva> obtenerReservasFuturasPorUsuario(Long usuarioId) {
		 LocalDate hoy = LocalDate.now();
		 return reservaRepository.findByUsuarioIdAndActivaAndFechaClaseAfterOrderByFechaClaseAsc(usuarioId, true, hoy);
		}
	
	// Obtener historial de clases asistidas (solo asistenciaConfirmada = true)
	public List<Reserva> obtenerClasesAsistidasPorUsuario(Long usuarioId) {
	    return reservaRepository.findByUsuarioIdAndActivaAndAsistenciaConfirmadaOrderByFechaClaseDesc(usuarioId, true, true);
	}

	// Obtener historial de clases canceladas o no asistidas
	public List<Reserva> obtenerClasesNoAsistidasOCanceladas(Long usuarioId) {
	    return reservaRepository.findByUsuarioIdAndActivaAndAsistenciaConfirmadaOrderByFechaClaseDesc(usuarioId, false, false);
	}


	

}
