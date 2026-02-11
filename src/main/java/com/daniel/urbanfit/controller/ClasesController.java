package com.daniel.urbanfit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/clases")
public class ClasesController {
	
	// Vemos las página clases
	@GetMapping
	public String verClases() {
		return "clases";
		
	}
	
	// Vemos la página clases/horarios
	@GetMapping("/horarios")
	public String verHorarios() {
		return "horarios";
	}

}
