package com.daniel.urbanfit.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
	@Query("""
			   SELECT r FROM Reserva r
			   WHERE r.usuario.id = :usuarioId
			   AND (
			        r.activa = false
			        OR
			        (r.activa = true AND r.asistenciaConfirmada = false AND r.fechaClase < CURRENT_DATE)
			   )
			   ORDER BY r.fechaClase DESC
			""")
			List<Reserva> obtenerClasesCanceladasONoAsistidas(@Param("usuarioId") Long usuarioId);


	// Trae todas las reservas activas
	List<Reserva> findByHorario_Clase_IdAndActivaTrue(Long claseId);
	
	// Cuenta el número de reservas ACTIVAS asociadas a una clase concreta.
	Long countByHorarioClaseIdAndActivaTrue(Long claseId);
	
	// Obtiene las reservas de las clases de un monitor (sirve para ver la agenda)
    // El JOIN hace: Reserva -> Horario -> Clase -> Monitor
    @Query("SELECT r FROM Reserva r WHERE r.horario.clase.monitor.id = :monitorId AND r.activa = true ORDER BY r.fechaClase ASC, r.horario.horarioInicio ASC")
    List<Reserva> findReservasPorMonitor(@Param("monitorId") Long monitorId);

    // Cuenta cuántos alumnos hay para un horario específico en una fecha específica
    // (Esto es lo que hará que el 01/14 sea real para cada fila)
    Long countByHorarioIdAndFechaClaseAndActivaTrue(Long horarioId, LocalDate fechaClase);
    
    // Este método busca alumnos filtrando por:
    // 1. El ID de la clase (entrando desde Reserva -> Horario -> Clase)
    // 2. La fecha exacta de la sesión (para que no salgan alumnos de otros días)
    // 3. Que la reserva esté activa (no cancelada)
    List<Reserva> findByHorario_Clase_IdAndFechaClaseAndActivaTrue(Long claseId, LocalDate fecha);
    
    boolean existsByUsuarioIdAndHorarioIdAndFechaClaseAndActivaTrue(
    	    Long usuarioId,
    	    Long horarioId,
    	    LocalDate fechaClase
    	);
	
	
}
