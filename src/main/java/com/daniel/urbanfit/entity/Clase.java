package com.daniel.urbanfit.entity;


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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "clases")
public class Clase {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "El nombre es obligatorio")
	@Size(max = 100)
	private String nombre;
	
	private int capacidad;
	
	@Column(name = "fecha_creacion", updatable = false)
	private LocalDateTime fechaCreacion;
	
	@Column(name = "fecha_modificacion")
	private LocalDateTime fechaModificacion;
	
	// Relaciones
		
	@ManyToOne
	@JoinColumn(name = "monitor_id", nullable = false) // No puede ser null
	private Usuario monitor; // Si en MySQL hay un FK, esto en Java es un objeto. No se hace private String monitor.
	
	@ManyToOne
	@JoinColumn(name = "tipos_clase_id", nullable = false)
	private TipoClase tipoClase;
	
	@PrePersist
    public void prePersist() {
        fechaCreacion = LocalDateTime.now();
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

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getCapacidad() {
		return capacidad;
	}

	public void setCapacidad(int capacidad) {
		this.capacidad = capacidad;
	}

	public LocalDateTime getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(LocalDateTime fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public LocalDateTime getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(LocalDateTime fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public Usuario getMonitor() {
		return monitor;
	}

	public void setMonitor(Usuario monitor) {
		this.monitor = monitor;
	}

	public TipoClase getTipoClase() {
		return tipoClase;
	}

	public void setTipoClase(TipoClase tipoClase) {
		this.tipoClase = tipoClase;
	}
	

	
	

}
