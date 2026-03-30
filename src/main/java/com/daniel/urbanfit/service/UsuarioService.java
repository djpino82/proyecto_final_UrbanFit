package com.daniel.urbanfit.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.daniel.urbanfit.entity.Clase;
import com.daniel.urbanfit.entity.Rol;
import com.daniel.urbanfit.entity.Usuario;
import com.daniel.urbanfit.repository.ClaseRepository;
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
	
	@Autowired
	private ClaseRepository claseRepository;
	
	/**
     * Guarda un usuario en la base de datos.
     * Valida que el DNI y el email no estén duplicados.
     * Encripta la contraseña antes de guardar.
     * 
     * @param usuario Objeto Usuario con los datos del formulario
     * @return Usuario guardado
     */
	
	public Usuario guardarUsuario(Usuario usuario) {

	    // Traer usuario existente si tiene ID (edición)
	    Usuario usuarioExistente = null;
	    if (usuario.getId() != null) {
	        usuarioExistente = usuarioRepository.findById(usuario.getId())
	                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
	    }

	    // Validar email solo si se está cambiando
	    Usuario usuarioPorEmail = usuarioRepository.findByEmail(usuario.getEmail()).orElse(null);
	    if (usuarioPorEmail != null 
	            && (usuarioExistente == null || !usuarioPorEmail.getId().equals(usuario.getId()))) {
	        throw new IllegalArgumentException("Este email ya está registrado");
	    }

	    // Si es edición, mantener campos que admin no puede tocar
	    if (usuarioExistente != null) {
	        usuario.setDni(usuarioExistente.getDni());             // DNI no editable
	        usuario.setPassword(usuarioExistente.getPassword());   // Mantener contraseña si no se cambia
	        if (usuario.getRol() == null) {
	            usuario.setRol(usuarioExistente.getRol());
	        }
	    } else {
	        // Si es nuevo usuario, encriptar contraseña y asignar rol por defecto
	        if (usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
	            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
	        }
	        if (usuario.getRol() == null) {
	            Rol rolCliente = rolRepository.findById(3L)
	                    .orElseThrow(() -> new RuntimeException("Rol CLIENTE no encontrado"));
	            usuario.setRol(rolCliente);
	        }
	    }

	    // Guardar en la base de datos
	    return usuarioRepository.save(usuario);
	}
	
	// Obtener todos los usuarios
    public List<Usuario> obtenerUsuarios() {
        return usuarioRepository.findAll();
    }

    // Obtener todos los monitores
    public List<Usuario> obtenerMonitores() {
        return usuarioRepository.findByRolNombre("MONITOR");
    }
	
	
	// Método para actualizar un usuario existente
    public Usuario actualizarUsuario(Usuario usuario) {
        // Si el objeto tiene un ID, Hibernate hará UPDATE automáticamente
        return usuarioRepository.save(usuario);
    }
    
  //Eliminar usuario (solo si no tiene clases asignadas)
    public void eliminarUsuario(Long id) {
        // Buscamos todas las clases donde el usuario sea monitor
        List<Clase> clasesAsignadas = claseRepository.findByMonitorId(id);

        if (!clasesAsignadas.isEmpty()) {
            throw new RuntimeException("No puedes eliminar este monitor porque tiene " + clasesAsignadas.size() + " clases asignadas.");
        }

        usuarioRepository.deleteById(id);
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
	
	// Contar todos los usuarios (para estadísticas)
	public long contarUsuarios() {
	    return usuarioRepository.count();
	}

	// Contar solo los monitores (para estadísticas)
	public long contarMonitores() {
	    return usuarioRepository.countByRolNombre("MONITOR");
	}
	
	// Traer usuario por ID
	public Usuario obtenerUsuarioPorId(Long id) {
	    return usuarioRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Usuario con ID " + id + " no encontrado"));
	}
	
	// Obtener rol por ID
	public Rol obtenerRolPorId(Long id) {
	    return rolRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Rol con ID " + id + " no encontrado"));
	}
	
	
	

}
