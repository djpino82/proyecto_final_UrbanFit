package com.daniel.urbanfit.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SeguridadConfig {
	
	// Creamos un bean de PasswordEncoder para poder inyectarlo en cualquier Service
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(); // Encriptar password
		
	}
	
	// Configuración de seguridad web
	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
            	.requestMatchers("/", "/clases", "/clases/horarios", "/contacto", "/registro", "/login", "/css/**", "/images/**").permitAll() // Rutas públicas
            	// Rutas privadas según rol
            	.requestMatchers("/admin/**").hasRole("ADMINISTRADOR")
            	.requestMatchers("/monitor/**").hasRole("MONITOR")
            	.requestMatchers("/cliente/**").hasRole("CLIENTE")
                .anyRequest().authenticated() // El resto necesita autenticación.
            )
            
         // 🔹 Configuración del login
            .formLogin(login -> login
                .loginPage("/login") // Página de login.html
                
                // Redirección según rol al iniciar sesión
                .successHandler((request, response, authentication) -> {
                	
                    // Obtenemos el rol del usuario logueado
                    String role = authentication.getAuthorities().iterator().next().getAuthority();

                    // Redirigimos a dashboard correspondiente
                    if(role.equals("ROLE_ADMINISTRADOR")) {
                        response.sendRedirect("/admin/dashboard");
                    } else if(role.equals("ROLE_MONITOR")) {
                        response.sendRedirect("/monitor/dashboard");
                    } else if(role.equals("ROLE_CLIENTE")) {
                        response.sendRedirect("/cliente/dashboard");
                    } else {
                        response.sendRedirect("/"); 
                    }
                })

                // Si hay error de login, redirige con parámetro que luego se usa en el html
                .failureUrl("/login?error=true")
                
                // Pueden ver la página login todos sin estar logueados
                .permitAll()
            )
            
            // Configuración de logout
            .logout(logout -> logout
                .logoutUrl("/logout")                     // URL para cerrar sesión
                .logoutSuccessUrl("/login?logout=true")   // Redirige tras logout
                .permitAll()
            )
            
            
            .csrf(csrf -> csrf.disable()); //  desactiva CSRF (para formularios simples)

        return http.build();
    }
	

}
