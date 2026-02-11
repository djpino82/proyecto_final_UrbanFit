package com.daniel.urbanfit.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "reservas")
public class Reserva {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "fecha_registro", updatable = false)
	private LocalDateTime fechaRegistro;
	
	@Column(name = "asistencia_confirmada", columnDefinition = "bit(1)")
	private boolean asistenciaConfirmada = false;
	
	@Column(name = "fecha_clase", nullable = false)
	private LocalDate fechaClase;
	
	@Column(name = "fecha_modificacion")
	private LocalDateTime fechaModificacion;
	
	@Column(name = "activa", columnDefinition = "bit(1)")
	private boolean activa = true;
	
	@ManyToOne
	@JoinColumn(name = "usuarios_id", nullable = false)
	private Usuario usuario;
	
	@ManyToOne
	@JoinColumn(name = "horarios_id", nullable = false)
	private Horario horario;
	
	@PrePersist
    public void prePersist() {
        fechaRegistro = LocalDateTime.now();
        fechaModificacion = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        fechaModificacion = LocalDateTime.now();
    }
	
	// Getters y Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(LocalDateTime fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public boolean isAsistenciaConfirmada() {
		return asistenciaConfirmada;
	}

	public void setAsistenciaConfirmada(boolean asistenciaConfirmada) {
		this.asistenciaConfirmada = asistenciaConfirmada;
	}
	
	public LocalDate getFechaClase() {
		return fechaClase;
	}

	public void setFechaClase(LocalDate fechaClase) {
		this.fechaClase = fechaClase;
	}

	public LocalDateTime getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(LocalDateTime fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public boolean isActiva() {
		return activa;
	}

	public void setActiva(boolean activa) {
		this.activa = activa;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Horario getHorario() {
		return horario;
	}

	public void setHorario(Horario horario) {
		this.horario = horario;
	}

	
	
	
}
