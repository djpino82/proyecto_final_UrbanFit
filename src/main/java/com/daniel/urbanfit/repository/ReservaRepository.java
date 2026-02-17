package com.daniel.urbanfit.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daniel.urbanfit.entity.Horario;
import com.daniel.urbanfit.entity.Reserva;
import com.daniel.urbanfit.entity.Usuario;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
	
	// Buscar todas las reservas de un Usuario
	List<Reserva> findByUsuarioId(Long usarioId);
	
	// Buscar reservas por horario y fecha de clase para evitar reservas duplicadas (la validación se hace en el service)
	boolean existsByUsuarioAndHorarioAndFechaClase(Usuario usuario, Horario horario, LocalDate fechaclase);
	
	// Contar cuantas reservas activas hay para un horario y una fecha. 
	Long countByHorarioAndFechaClaseAndActivaTrue(Horario horario, LocalDate fechaClase);
	
	// Trae todas las reservas por id del usuario y fechas de clases anterior a esta fecha
	List<Reserva> findByUsuarioIdAndFechaClaseBefore(Long usuarioId, LocalDate fecha);
	
	// Todas las reservas de un usuario ordenadas por fechas mas recientes
	List<Reserva> findByUsuarioIdAndActivaOrderByFechaClaseDesc(Long usuarioId, boolean activa);

	// Todas las reservas de un usuario ordenadas por fechas mas antigua a mas reciente
	List<Reserva> findByUsuarioIdAndActivaOrderByFechaClaseAsc(Long usuarioId, boolean activa);
	
	// Todas las reservas activas futuras.
	List<Reserva> findByUsuarioIdAndActivaAndFechaClaseAfterOrderByFechaClaseAsc(Long usuarioId, boolean activa, LocalDate fechaActual);

	// Historial de clases asistidas (solo asistencia confirmada)
	List<Reserva> findByUsuarioIdAndActivaAndAsistenciaConfirmadaOrderByFechaClaseDesc(Long usuarioId, boolean activa, boolean asistenciaConfirmada);

	// Historial de clases canceladas o no asistidas
	List<Reserva> findByUsuarioIdAndActivaOrAsistenciaConfirmadaOrderByFechaClaseDesc(Long usuarioId, boolean activa, boolean asistenciaConfirmada);

	
}
