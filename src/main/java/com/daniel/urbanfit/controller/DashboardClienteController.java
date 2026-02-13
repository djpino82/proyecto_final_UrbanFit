package com.daniel.urbanfit.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.daniel.urbanfit.entity.Horario;
import com.daniel.urbanfit.entity.Reserva;
import com.daniel.urbanfit.entity.Usuario;
import com.daniel.urbanfit.service.HorarioService;
import com.daniel.urbanfit.service.ReservaService;
import com.daniel.urbanfit.service.UsuarioService;



@Controller
@RequestMapping("/cliente")
public class DashboardClienteController {
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private ReservaService reservaService;
	
	@Autowired
	private HorarioService horarioService;
	
	
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
	
	// Post(/Perfil) para actualizar el perfil
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
	
	// Get(/reservas) para mostrar las reservas.	
	@GetMapping("/reservas")
	public String verReservas ( @RequestParam(required = false) String fechaClase, Model model, Principal principal) {
		
		Usuario usuario = usuarioService.obtenerUsuarioPorEmail(principal.getName()); // obtener usuario logueado usuando mail
		List<Reserva> reservas = reservaService.obtenerReservaPorUsuario(usuario.getId()); // obterner todas las reserva del usuario
		
		// --- LÓGICA DE RESTRICCIÓN DE FECHAS (Semana vista) ---
        LocalDate hoy = LocalDate.now();
        LocalDate fechaLimite = hoy.plusDays(6); // Define el rango de 7 días (hoy + 6 próximos)
		
		// Filtrar si hay fecha de reserva o no
		List<Horario> horarios;
		
		if (fechaClase !=null && !fechaClase.isEmpty()) {
			
			// Si hay fecha seleccionada, convertirmos el String a LocalDate
			LocalDate fecha = LocalDate.parse(fechaClase);
			
			// Validación preventiva: Si el usuario manipula la URL con una fecha fuera de rango
            if (fecha.isBefore(hoy) || fecha.isAfter(fechaLimite)) {
                horarios = new ArrayList<>();
                model.addAttribute("error", "La fecha seleccionada está fuera del rango permitido.");
                
            } else {		
			
			
			// Llamamos al service para filtrar día de la semana
			horarios = horarioService.obtenerPorDiaSemana(fecha);
			
			// Enviamos el servicio al modelo para que Thymeleaf pueda ejecutar cálculos en tiempo real
			model.addAttribute("horarioService", horarioService); 
			
			// Enviamos la fecha como objeto LocalDate para que los métodos del service la reconozcan
		    model.addAttribute("fechaLD", fecha); 
		    
		    model.addAttribute("fechaSeleccionada", fechaClase);
            }
            
		} else {
			// Primera carga de la página no hay fecha seleccionada, se crear lista vacía.
			horarios = new ArrayList<>();
		}
		
		
		// Pasamos al Model
		model.addAttribute("usuario", usuario);
		model.addAttribute("reservas",reservas);
		model.addAttribute("horarios", horarios);
		
		// Indicamos sección activa
		model.addAttribute("seccion", "reservas"); // reservas se pone en el th:case="reservas" del fragmento de html
		
		return "dashboardCliente";
		
	}
	
	@PostMapping("/reservas")
	public String crearReserva(@RequestParam Long horarioId, @RequestParam String fechaClase, Principal principal, RedirectAttributes redirectAttributes ) {
		
		// Principal representa al usuario autenticado (Spring Security)
        // getName() devuelve el username/email con el que inició sesión
		String emailUsuario = principal.getName(); // email o username conseguimos
		
		// Obtenemos el usuario logueago
		Usuario usuario = usuarioService.obtenerUsuarioPorEmail(emailUsuario);
		
		// Pasamos la fecha de la reserva que está en String a Localdate
		LocalDate fecha = LocalDate.parse(fechaClase);
		
		// --- VALIDACIÓN DE SEGURIDAD EN SERVIDOR ---
        // Evita que usuarios avanzados se salten la restricción del HTML
        LocalDate hoy = LocalDate.now();
        LocalDate limite = hoy.plusDays(6);
        
        if (fecha.isBefore(hoy) || fecha.isAfter(limite)) {
            redirectAttributes.addFlashAttribute("error", "Operación no permitida: Solo se puede reservar a una semana vista.");
            return "redirect:/cliente/reservas";
        }
		
		try {
			
			// Creamos la reserva usando el service
			reservaService.crearReserva(usuario.getId(), horarioId, fecha);
			
			// Mensaje de éxito que desaparece al recargar (usando Flash Attribute)
			redirectAttributes.addFlashAttribute("mensaje", "Reserva realizada correctamente");
			
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "No se ha podido completar la reserva. Inténtalo de nuevo.");
		}
		
		return "redirect:/cliente/reservas";
		
	}
	
	
	// Método para ver mis reservas
	@GetMapping("/mis-reservas")
	public String verMisReservas(Model model, Principal principal) {
		
		// Identificar el usuario
		Usuario usuario = usuarioService.obtenerUsuarioPorEmail(principal.getName());
		
		// Obtener las reservas que ya tiene en la Base de datos. 
		List<Reserva> reservas = reservaService.obtenerReservaPorUsuario(usuario.getId());
		
		// Pasamos el model
		model.addAttribute("usuario", usuario);
	    model.addAttribute("usuarioNombre", usuario.getNombre());
	    model.addAttribute("reservas", reservas);
	    model.addAttribute("seccion", "misReservas"); // <--- Clave para el switch y el sidebar active
	    
	    return "dashboardCliente";
	}
	
	
	
	
	// Método para Cancelar Reservas
	@PostMapping("/cancelar")
	public String cancelarReserva(@RequestParam Long reservaId, RedirectAttributes redirectAttributes) {
		try {
			
			reservaService.eliminarReserva(reservaId);
			redirectAttributes.addFlashAttribute("mensaje", "La reserva ha sido cancelada con éxito.");
			
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "No se pudo cancelar la reserva: ");
		}
		
		//Redirigimos siempre a la lista de reservas para que vea los cambios
	    return "redirect:/cliente/mis-reservas";
	}
	
	

}
