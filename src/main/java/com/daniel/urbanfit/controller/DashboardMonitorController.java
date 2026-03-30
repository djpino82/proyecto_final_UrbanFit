package com.daniel.urbanfit.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.daniel.urbanfit.entity.Clase;
import com.daniel.urbanfit.entity.Reserva;
import com.daniel.urbanfit.entity.Usuario;
import com.daniel.urbanfit.service.ClaseService;
import com.daniel.urbanfit.service.ReservaService;
import com.daniel.urbanfit.service.UsuarioService;

@Controller
@RequestMapping("/monitor")
public class DashboardMonitorController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private ClaseService claseService;
    
    @Autowired
    private ReservaService reservaService;
    
    // Muestra el dashboard de Monitor
    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        Usuario monitor = usuarioService.obtenerUsuarioPorEmail(principal.getName());
        model.addAttribute("usuario", monitor);
        model.addAttribute("usuarioNombre", usuarioService.capitalizarNombreCompleto(monitor.getNombre(), monitor.getApellidos()));
        model.addAttribute("seccion", "perfil"); // Sección inicial
        return "dashboardMonitor";
    }
    
    @PostMapping("/perfil")
    public String actualizarPerfil(Usuario usuarioForm, Principal principal, RedirectAttributes redirectAttributes) {
        Usuario monitor = usuarioService.obtenerUsuarioPorEmail(principal.getName());
        monitor.setNombre(usuarioForm.getNombre());
        monitor.setApellidos(usuarioForm.getApellidos());
        monitor.setTelefono(usuarioForm.getTelefono());
        monitor.setDireccion(usuarioForm.getDireccion());
        monitor.setCodigoPostal(usuarioForm.getCodigoPostal());
        monitor.setLocalidad(usuarioForm.getLocalidad());
        monitor.setFechaNacimiento(usuarioForm.getFechaNacimiento());
        usuarioService.actualizarUsuario(monitor);
        redirectAttributes.addFlashAttribute("mensaje", "Perfil actualizado correctamente");
        return "redirect:/monitor/dashboard";
    }
    
    // Listado de clases asignadas al monitor
    @GetMapping("/clases")
    public String verClases(Model model, Principal principal) {
        Usuario monitor = usuarioService.obtenerUsuarioPorEmail(principal.getName());
        
        List<Reserva> agenda = reservaService.obtenerAgendaMonitor(monitor.getId());

        // Creamos un mapa con la cantidad de reservas por horario
        Map<Long, Integer> reservasPorHorario = new HashMap<>();
        for (Reserva r : agenda) {
            Long horarioId = r.getHorario().getId();
            reservasPorHorario.put(horarioId,
                reservasPorHorario.getOrDefault(horarioId, 0) + 1);
        }

        model.addAttribute("usuario", monitor);
        model.addAttribute("sesiones", agenda);
        model.addAttribute("reservasPorHorario", reservasPorHorario); // <-- aquí
        model.addAttribute("seccion", "clases");

        return "dashboardMonitor";
    }
    
    @GetMapping("/alumnos")
    public String verAlumnos(
            @RequestParam Long claseId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            Model model,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        Usuario monitor = usuarioService.obtenerUsuarioPorEmail(principal.getName());
        Clase clase = claseService.obtenerClasePorId(claseId);

        if (!clase.getMonitor().getId().equals(monitor.getId())) {
            redirectAttributes.addFlashAttribute("error", "No tienes permisos para ver esta clase");
            return "redirect:/monitor/clases";
        }

        LocalDate fechaBusqueda = (fecha != null) ? fecha : LocalDate.now();
        List<Reserva> reservas = reservaService.obtenerReservasPorClaseYFecha(claseId, fechaBusqueda);

        model.addAttribute("usuario", monitor);
        model.addAttribute("claseSeleccionada", clase);
        model.addAttribute("fechaSeleccionada", fechaBusqueda);
        model.addAttribute("reservas", reservas);
        model.addAttribute("seccion", "alumnos");

        return "dashboardMonitor";
    }
    
    @PostMapping("/asistencia")
    public String marcarAsistencia(
            @RequestParam Long reservaId,
            @RequestParam Boolean asistio,
            RedirectAttributes redirectAttributes) {

        try {
            reservaService.marcarAsistencia(reservaId, asistio);
            redirectAttributes.addFlashAttribute("mensaje",
                    "Asistencia actualizada correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "No se pudo actualizar la asistencia");
        }

        return "redirect:/monitor/clases";
    }
}