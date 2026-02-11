package com.daniel.urbanfit.configuration;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.daniel.urbanfit.entity.Usuario;
import com.daniel.urbanfit.repository.UsuarioRepository;

// Es la clase que Spring Security llama cuando alguien hace login para obtenero los datos de la BD.

@Service // Inversión de Control: Crea un objeto de esta clase y lo guarda para usarla posteriormente.
public class UsuarioDetailsService implements UserDetailsService{
	
	@Autowired 
	private UsuarioRepository usuarioRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException { // Si no encuentra el mail, lanzamos excepción
		
		// Busca el usuario en la BD por mail
		Usuario usuario = usuarioRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
		
		// Convertimos su rol en formato que Spring entienda
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().getNombre());
		
		// Devolvemos un objeto User de la interfaz UserDetails que usa Spring para:
		//  - Verificar que el email y la contraseña coinciden con los datos almacenados.
		//  - Comprobar que el usuario tiene el rol adecuado para acceder a ciertas partes de la aplicación.
		return new User (
				usuario.getEmail(),
				usuario.getPassword(),
				Collections.singleton(authority) // Colección con un solo elemento que es la autoridad/rol del usuario.
				
				);
	}

}
