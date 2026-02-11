package com.daniel.urbanfit.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.daniel.urbanfit.entity.Reserva;
import com.daniel.urbanfit.entity.Usuario;
import com.daniel.urbanfit.service.ReservaService;
import com.daniel.urbanfit.service.UsuarioService;



@Controller
@RequestMapping("/cliente")
public class DashboardClienteController {
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private ReservaService reservaService;
	
	@GetMapping("/dashboard")
	public String dashboard(Model model, Principal principal) {
		
		// Principal representa al usuario autenticado (Spring Security)
        // getName() devuelve el username/email con el que inició sesión
		String emailUsuario = principal.getName(); // email o username conseguimos
		
		// Obtenemos el usuario completo desde la base de datos
		Usuario usuario = usuarioService.obtenerUsuarioPorEmail(emailUsuario);
		
		// Mensaje de Bienvenida
		model.addAttribute("usuarioNombre", usuarioService.capitalizarNombreCompleto(usuario.getNombre(), usuario.getApellidos()));
		
		// Solo agregamos "usuario" si no viene como flash attribute desde el post al hacer redirect
	    if (!model.containsAttribute("usuario")) {
	        model.addAttribute("usuario", usuario);
	    }

        // Indicamos la sección que queremos mostrar en el dashboard
        // Esto es útil si más adelante usamos el mismo dashboard para Clases, Reservas, Historial
        model.addAttribute("seccion", "perfil");

        // Retornamos la plantilla Thymeleaf principal
        return "dashboardCliente"; // Thymeleaf buscará dashboardCliente.html
	}
	
	// Post /Perfil para actualizar el perfil
	@PostMapping("/perfil")
	public String actualizarPerfil(Usuario usuarioForm, Principal principal, RedirectAttributes redirectAttributes) {
		
		//Obtenemos el usuario que está logueado desde la BD. El usuario real.
		Usuario usuario = usuarioService.obtenerUsuarioPorEmail(principal.getName());
		
		// Actualizamos solo los campos que permitimos modificar
	    // (no tocamos email, contraseña, estado ni rol)
		usuario.setNombre(usuarioForm.getNombre());
		usuario.setApellidos(usuarioForm.getApellidos());
	    usuario.setTelefono(usuarioForm.getTelefono());
	    usuario.setDireccion(usuarioForm.getDireccion());
	    usuario.setCodigoPostal(usuarioForm.getCodigoPostal());
	    usuario.setLocalidad(usuarioForm.getLocalidad());
	    usuario.setFechaNacimiento(usuarioForm.getFechaNacimiento());
	    
	    // Guardamos los datos en la BD
	    usuarioService.actualizarUsuario(usuario);

	    // Mantenemos la sección activa en el dashboard (perfil)
	    redirectAttributes.addFlashAttribute("mensaje", "Perfil actualizado correctamente");

	    // Devolvemos la plantilla del dashboard, Thymeleaf reemplazará el fragmento
	    return "redirect:/cliente/dashboard";
	}
	
	// Get /reservas para mostrar las reservas.
	
	@GetMapping("/reservas")
	public String verReservas (Model model, Principal principal) {
		
		Usuario usuario = usuarioService.obtenerUsuarioPorEmail(principal.getName()); // obtener usuario logueado usuando mail
		List<Reserva> reservas = reservaService.obtenerReservaPorUsuario(usuario.getId()); // obterner todas las reserva del usuario
		
		// Pasamos al Model
		model.addAttribute("usuario", usuario);
		model.addAttribute("reservas",reservas);
		
		// Indicamos sección activa
		model.addAttribute("seccion", "reservas"); // reservas se pone en el th:case="reservas" del fragmento de html
		
		return "dashboardCliente";
		
	}
	
	@PostMapping("/reservas")
	public String crearReservas

}
