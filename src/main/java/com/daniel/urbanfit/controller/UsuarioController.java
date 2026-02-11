package com.daniel.urbanfit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.daniel.urbanfit.entity.Usuario;
import com.daniel.urbanfit.service.UsuarioService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/registro") // Todas las rutas de este controller empezarán por /resgistro
public class UsuarioController {
	
	@Autowired
	private UsuarioService usuarioService; // Inyectamos el servicio que se encarga de la lógica para usarlo
	
	// Get para mostrar el formularios de registro
	@GetMapping
	public String mostrarFormulario(Model model) {
		model.addAttribute("usuario", new Usuario()); // Creamos un objeto Usuario vacío
		return "registro";
		
	}
	
	// Post para enviar el formulario y guardar los datos en la BBDD. Se ejecuta al pulsar el botón de Registrarse
	
	@PostMapping // Este método responde a los POST enviados al endpoint /registro
	public String procesarFormulario(
	        @Valid @ModelAttribute("usuario") Usuario usuario, // Spring crea un objeto Usuario con los datos del formulario y aplica validaciones de la entity
	        BindingResult result, // Aquí se almacenan los errores de validación si los hay, los errores que aparecen en @NotBlank
	        Model model) { // Permite enviar datos adicionales a la vista Thymeleaf

	    // Comprobamos si hay errores de validación (NotBlank, Email, Pattern, etc.)
	    if (result.hasErrors()) {
	        // Si hay errores, regresamos al formulario de registro
	        // Los mensajes de error se mostrarán automáticamente en el HTML en el <p th:if="${#fields.hasErrors('nombre')}" th:errors="*{nombre}"></p>
	        return "registro"; // el nombre corresponde al template Thymeleaf registro.html
	    }

	    try {
	    	    	
	        // Llamamos al service para guardar el usuario
	        // El service valida duplicados de email y DNI, encripta la contraseña y guarda en la BD
	        usuarioService.guardarUsuario(usuario);

	        //Si todo va bien, redirigimos al login
	        return "redirect:/login?registroExitoso=true";

	    } catch (IllegalArgumentException e) {
	        // Capturamos las excepciones que lanza el service si DNI o email ya existen
	        // Enviamos el mensaje de error al HTML para mostrarlo al usuario. Son erroes de la logica de negocio que aparecen en el service NO LOS DE VALIDACIÓN.
	        model.addAttribute("errorRegistro", e.getMessage());

	        // Volvemos al formulario con el mensaje de error
	        return "registro";
	    }
	}


}
