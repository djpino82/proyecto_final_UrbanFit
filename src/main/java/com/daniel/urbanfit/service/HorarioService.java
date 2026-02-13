package com.daniel.urbanfit.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.daniel.urbanfit.entity.DiaSemana;
import com.daniel.urbanfit.entity.Horario;
import com.daniel.urbanfit.repository.HorarioRepository;
import com.daniel.urbanfit.repository.ReservaRepository;

@Service
public class HorarioService {
	
	@Autowired
	private HorarioRepository horarioRepository;
	
	@Autowired
	private ReservaRepository reservaRepository;
	
	public List<Horario> obtenerTodos(){
		return horarioRepository.findAll();
	}
	
	public Horario obtenerPorId(Long Id) {
		return horarioRepository.findById(Id).orElseThrow(() -> new IllegalArgumentException("Horario no encontrado"));
	}
	
	// Filtra los horarios disponibles según el día de la semana de una fecha dada.
    // Convierte automáticamente la fecha al día de la semana correspondiente
	// @param fecha Fecha seleccionada por el usuario (ej: 2026-02-17)
    // @return Lista de horarios que corresponden a ese día de la semana
	public List<Horario> obtenerPorDiaSemana(LocalDate fecha) {
		
		// Extraer el día de la semana de la fecha (MONDAY, TUESDAY, etc.)
		DayOfWeek diaJava = fecha.getDayOfWeek();
		
		// Convertir el enum de Java al enum personalizado de la aplicación
		DiaSemana diaEnum = convertirDiaSemana(diaJava);
		
		// Buscar en la base de datos todos los horarios de ese día
        return horarioRepository.findByDiaSemana(diaEnum);
		
	}
	
	//Convierte el enum DayOfWeek de Java al enum DiaSemana personalizado.
    //Necesario porque la BD guarda los días en español con formato específico.
    
    // @param diaJava Día de la semana en formato Java (MONDAY, TUESDAY, etc.)
    // @return Día de la semana en formato personalizado (Lunes, Martes, etc.)
    // @throws IllegalArgumentException si el día no es válido
	private DiaSemana convertirDiaSemana (DayOfWeek diaJava) {
		switch (diaJava) {
			case MONDAY:    return DiaSemana.Lunes;
	        case TUESDAY:   return DiaSemana.Martes;
	        case WEDNESDAY: return DiaSemana.Miércoles;
	        case THURSDAY:  return DiaSemana.Jueves;
	        case FRIDAY:    return DiaSemana.Viernes;
	        case SATURDAY:  return DiaSemana.Sábado;
	        case SUNDAY:    return DiaSemana.Domingo;
		
		
		default:
			throw new IllegalArgumentException("Dia de la semana no válido: " + diaJava);
		}
	}
	
	// Cuenta cuántas reservas activas tiene un horario en una fecha específica
	// Ejemplo: Spinning del martes tiene capacidad 15, Ya hay 12 reservas activas,  Este método devuelve: 12
	public Long contarReservas(Horario horario, LocalDate fecha) {
		return reservaRepository.countByHorarioAndFechaClaseAndActivaTrue(horario, fecha);
		
	}
	
	// Verifica si una clase está completa (sin plazas disponibles).
    // Ejemplo: Spinning tiene capacidad 15,  Ya hay 15 reservas, Este método devuelve: true (está completo)
	public boolean contarClase(Horario horario, LocalDate fecha) {
		Long reservasActuales = contarReservas(horario, fecha);
		int capacidad = horario.getClase().getCapacidad();
		return reservasActuales >= capacidad;
	}
	
	// Calcular cuantas plazas quedan libres
	// Ejemplo: Spinning tiene capacidad de 15, si ya hay 12 reservas, el método devuelve 3 plazas
	public Long plazasLibres(Horario horario, LocalDate fecha) {
		Long reservasActuales = contarReservas(horario, fecha);
		int capacidad = horario.getClase().getCapacidad();
		return capacidad - reservasActuales;
	}
	
	
}
