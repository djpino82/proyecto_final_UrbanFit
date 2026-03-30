package com.daniel.urbanfit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.daniel.urbanfit.entity.DiaSemana;
import com.daniel.urbanfit.entity.Horario;

public interface HorarioRepository extends JpaRepository<Horario, Long> {
	
	// Busca todos los horarios que corresponden a un día de la semana específico.
	List<Horario> findByDiaSemana(DiaSemana diaSemana);
	
	// Trae todos los horarios de una clase por su id
    List<Horario> findByClaseId(Long claseId);
    
    @Query("SELECT h FROM Horario h WHERE h.clase.id = :claseId ORDER BY CASE h.diaSemana " +
    	       "WHEN 'Lunes' THEN 1 WHEN 'Martes' THEN 2 WHEN 'Miércoles' THEN 3 " +
    	       "WHEN 'Jueves' THEN 4 WHEN 'Viernes' THEN 5 WHEN 'Sábado' THEN 6 " +
    	       "WHEN 'Domingo' THEN 7 END, h.horarioInicio ASC")
    	List<Horario> findByClaseIdOrdenadoPorDiaYHora(@Param("claseId") Long claseId);

}
