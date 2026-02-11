package com.daniel.urbanfit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
	
	// Muestra la página Login
	@GetMapping("/login")
	public String mostrarLogin() {	
		return "login";
	}
	
	// El Login se hace en Seguridad Config y UsuarioDetailsService

}
