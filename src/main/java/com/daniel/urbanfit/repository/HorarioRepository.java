package com.daniel.urbanfit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daniel.urbanfit.entity.DiaSemana;
import com.daniel.urbanfit.entity.Horario;

public interface HorarioRepository extends JpaRepository<Horario, Long> {
	
	// Busca todos los horarios que corresponden a un día de la semana específico.
	List<Horario> findByDiaSemana(DiaSemana diaSemana);

}
