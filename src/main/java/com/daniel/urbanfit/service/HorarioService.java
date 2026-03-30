package com.daniel.urbanfit.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.daniel.urbanfit.entity.Clase;
import com.daniel.urbanfit.entity.DiaSemana;
import com.daniel.urbanfit.entity.Horario;
import com.daniel.urbanfit.repository.ClaseRepository;
import com.daniel.urbanfit.repository.HorarioRepository;
import com.daniel.urbanfit.repository.ReservaRepository;

@Service
public class HorarioService {
	
	@Autowired
	private HorarioRepository horarioRepository;
	
	@Autowired
	private ReservaRepository reservaRepository;
	
	@Autowired
	private ClaseRepository claseRepository; 
	
	public List<Horario> obtenerTodos(){
		return horarioRepository.findAll();
	}
	
	public Horario obtenerPorId(Long Id) {
		return horarioRepository.findById(Id).orElseThrow(() -> new IllegalArgumentException("Horario no encontrado"));
	}
	
	// Filtra los horarios disponibles según el día de la semana de una fecha dada.
	public List<Horario> obtenerPorDiaSemana(LocalDate fecha) {
		DayOfWeek diaJava = fecha.getDayOfWeek();
		DiaSemana diaEnum = convertirDiaSemana(diaJava);
		return horarioRepository.findByDiaSemana(diaEnum);
	}
	
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
	
	public Long contarReservas(Horario horario, LocalDate fecha) {
		return reservaRepository.countByHorarioAndFechaClaseAndActivaTrue(horario, fecha);
	}
	
	public boolean contarClase(Horario horario, LocalDate fecha) {
		Long reservasActuales = contarReservas(horario, fecha);
		int capacidad = horario.getClase().getCapacidad();
		return reservasActuales >= capacidad;
	}
	
	public Long plazasLibres(Horario horario, LocalDate fecha) {
		Long reservasActuales = contarReservas(horario, fecha);
		int capacidad = horario.getClase().getCapacidad();
		return capacidad - reservasActuales;
	}
	
	public List<Horario> obtenerPorClase(Long claseId) {
	    return horarioRepository.findByClaseId(claseId);
	}
	
	public List<Horario> obtenerPorClaseOrdenados(Long claseId) {
	    return horarioRepository.findByClaseIdOrdenadoPorDiaYHora(claseId);
	}
	
	// -----------------------------
	// 🔹 MODIFICADO: guardarHorario
	// -----------------------------
	public Horario guardarHorario(Horario horarioForm) {
	    if (horarioForm.getId() != null && horarioRepository.existsById(horarioForm.getId())) {
	        // Editar horario existente
	        Horario horarioBD = horarioRepository.findById(horarioForm.getId()).get();
	        horarioBD.setDiaSemana(horarioForm.getDiaSemana());
	        horarioBD.setHorarioInicio(horarioForm.getHorarioInicio());
	        horarioBD.setHorarioFin(horarioForm.getHorarioFin());
	        return horarioRepository.save(horarioBD);
	    } else {
	        // Nuevo horario → crear
	        Clase claseBD = claseRepository.findById(horarioForm.getClase().getId())
	                .orElseThrow(() -> new IllegalArgumentException("Clase no encontrada"));

	        // ⚡ Guardar primero el horario
	        horarioForm.setClase(claseBD);
	        Horario horarioGuardado = horarioRepository.save(horarioForm);

	        // ⚡ Luego mantener relación bidireccional
	        claseBD.getHorarios().add(horarioGuardado);
	        claseRepository.save(claseBD); // asegura que la colección se sincroniza con la BD

	        return horarioGuardado;
	    }
	}
	
	
	public void eliminarHorario(Long id) {
	    horarioRepository.deleteById(id);
	}
	
}