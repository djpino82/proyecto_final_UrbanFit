package com.daniel.urbanfit.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

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
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "usuarios")
public class Usuario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Indica que id lo genera MySQL usando Auto_Increment.
	private Long id; // Se pone Long porque admite Null.
	
	@NotBlank(message = "El nombre es obligatorio") // Campo obligatorio
	@Size(max = 100)
	private String nombre;
	
	@NotBlank(message = "Los apellidos son obligatorios")
	@Size(max = 100)
	private String apellidos;
	
	@NotBlank(message = "El DNI es obligatorio")
	@Size(max = 20)
	@Pattern(regexp = "\\d{8}[A-Za-z]", message = "DNI o tarjeta de residencia inválido")
	@Column(unique = true) // Sirve para que JPA sepa que es único.
	private String dni;
	
	@NotBlank(message = "El email es obligatorio")
	@Email(message = "El email no es válido") // Valida email
	@Size(max = 150)
	@Column(unique = true) // Sirve para que JPA sepa que es único.
	private String email;
	
	@NotBlank(message = "La contraseña es obligatoria")
	@Size (max = 150)
	private String password;
	
	@NotBlank(message = "El teléfono es obligatorio")
	@Size (max = 20)
	private String telefono;
	
	@Size(max = 250)
	private String direccion;
	
	@Column(name = "codigo_postal")
	@Size(max = 10)
	private String codigoPostal;
	
	@Size (max = 100)
	private String localidad;
	
	@Column(name = "fecha_nacimiento")
	@DateTimeFormat(pattern = "yyyy-MM-dd") // <- Esto indica el formato que Thymeleaf espera
	private LocalDate fechaNacimiento;
	
	// false = inactivo, true = activo
	private boolean estado = false;
	
	@Column(name = "fecha_pago")
	private LocalDate fechaPago;
	
	@Column(name = "fecha_registro", updatable = false) // updatable no actualiza la tabla porque tiene Current_Timestamp y no cambia nunca.
	private LocalDateTime fechaRegistro;
	
	@Column(name = "ultimo_acceso")
	private LocalDateTime ultimoAcceso;
	
	@Column(name = "fecha_modificacion")
	private LocalDateTime fechaModificacion;
	
	@ManyToOne // Muchos usuarios tienen un rol
	@JoinColumn(name = "roles_id", nullable = false) // FK que apunta a roles.id y no puede ser null
	private Rol rol; // el objeto completo de la entidad Rol, no solo el id de rol.

	@PrePersist
    public void prePersist() {
        fechaRegistro = LocalDateTime.now();
        fechaModificacion = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        fechaModificacion = LocalDateTime.now();
    }
	
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

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getCodigoPostal() {
		return codigoPostal;
	}

	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}

	public String getLocalidad() {
		return localidad;
	}

	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}

	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(LocalDate fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	public LocalDate getFechaPago() {
		return fechaPago;
	}

	public void setFechaPago(LocalDate fechaPago) {
		this.fechaPago = fechaPago;
	}

	public LocalDateTime getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(LocalDateTime fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public LocalDateTime getUltimoAcceso() {
		return ultimoAcceso;
	}

	public void setUltimoAcceso(LocalDateTime ultimoAcceso) {
		this.ultimoAcceso = ultimoAcceso;
	}

	public LocalDateTime getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(LocalDateTime fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}
	
	
	

}
