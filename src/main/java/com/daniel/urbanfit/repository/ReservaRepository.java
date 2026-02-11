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
	boolean existsByUsuarioAndHorariosAndFechaClase(Usuario usuario, Horario horario, LocalDate fechaclase);

}
