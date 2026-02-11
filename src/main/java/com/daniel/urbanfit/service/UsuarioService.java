package com.daniel.urbanfit.service;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.daniel.urbanfit.entity.Rol;
import com.daniel.urbanfit.entity.Usuario;
import com.daniel.urbanfit.repository.RolRepository;
import com.daniel.urbanfit.repository.UsuarioRepository;

@Service
public class UsuarioService {
	
	@Autowired
	private PasswordEncoder passwordEncoder; // Bean de seguridad para encriptar
	
	@Autowired
	private UsuarioRepository usuarioRepository; // Inyectamos repositorio para la BD
	
	@Autowired
	private RolRepository rolRepository; // Inyectamos repositorio para la BD
	
	/**
     * Guarda un usuario en la base de datos.
     * Valida que el DNI y el email no estén duplicados.
     * Encripta la contraseña antes de guardar.
     * 
     * @param usuario Objeto Usuario con los datos del formulario
     * @return Usuario guardado
     */
	
	public Usuario guardarUsuario(Usuario usuario) {
		
		// Comprobar si el DNI existe
		if(usuarioRepository.existsByDni(usuario.getDni())) {
			throw new IllegalArgumentException("Este documento ya está registrado"); // No se muestra al usuario, es para pruebas
		}
		
		// Comprobamos si el email existe
		if(usuarioRepository.existsByEmail(usuario.getEmail())) {
			throw new IllegalArgumentException("Este email ya está registrado");
		}
		
		// Encriptamos la contraseña antes de guardar
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        
        // Asignamos rol cliente (id = 3) por defecto.
        Rol rolCliente = rolRepository.findById(3L)
        		.orElseThrow(() -> new RuntimeException("Rol CLIENTE no encontrado"));
        usuario.setRol(rolCliente);
        
        // Guardamos el usuario en la Base de Datos
        System.out.println("Antes de guardar: " + usuario);
        return usuarioRepository.save(usuario);
	}
	
	
	// Método para actualizar un usuario existente
    public Usuario actualizarUsuario(Usuario usuario) {
        // Si el objeto tiene un ID, Hibernate hará UPDATE automáticamente
        return usuarioRepository.save(usuario);
    }
	
	
	// Metodo para obtener usuario por mail. El controller no puede acceder al repository
	// por eso ponemos este método en el Service que a su vez lo busca del Respository. Va por capas.
	
	public Usuario obtenerUsuarioPorEmail(String email) {
		return usuarioRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
	}	
	
	
	// Método para Capitalizar Nombre y Apellidos
	public String capitalizarNombreCompleto(String nombre, String apellidos) {
		
		// Combinamos nombre y apellidos
        String completo = nombre + " " + apellidos;
		
		// Separamos el String en palabras usando el espacio como separador
		return	Arrays.stream(completo.split(" "))
				
				// Filtramos palabras vacías que aparecen por espacios dobles o múltiples
	             // Así evitamos intentar procesar "" que causaría errores
				.filter(palabra -> !palabra.isEmpty())
				
				// Convertimos cada palabra a "Primera letra mayúscula + resto minúscula"
				.map(palabra -> palabra.substring(0,1).toUpperCase() + palabra.substring(1).toLowerCase())
				
				// Unimos todas las palabras de nuevo en un solo String separadas por espacios
	             // Resultado final: "Juan Carlos Perez"
				.collect(Collectors.joining(" "));
		
		
	}
	
	
	

}
