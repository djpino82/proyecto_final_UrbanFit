package com.daniel.urbanfit.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daniel.urbanfit.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	
	// Para verificar duplicados antes de guardar el usuario
	boolean existsByDni(String dni);
	boolean existsByEmail(String email);
	
	// Busqueda por mail
	Optional<Usuario> findByEmail(String email);
	
	// Buscar por rol.nombre
    List<Usuario> findByRolNombre(String nombre);
    
    long countByRolNombre(String nombre);


}
