package com.daniel.urbanfit.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.daniel.urbanfit.entity.Clase;
import com.daniel.urbanfit.entity.Horario;
import com.daniel.urbanfit.entity.TipoClase;
import com.daniel.urbanfit.entity.Usuario;
import com.daniel.urbanfit.service.ClaseService;
import com.daniel.urbanfit.service.HorarioService;
import com.daniel.urbanfit.service.RolService;
import com.daniel.urbanfit.service.TipoClaseService;
import com.daniel.urbanfit.service.UsuarioService;
import java.util.Map;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;


@Controller
@RequestMapping("/admin")
public class DashboardAdministradorController {
	
	@Autowired
    private ClaseService claseService;

    @Autowired
    private UsuarioService usuarioService; // para monitores

    @Autowired
    private TipoClaseService tipoClaseService; // para tipos de clase
    
    @Autowired
    private RolService rolService;
    
    @Autowired
    private HorarioService horarioService;
	
	
 // =========================================================
    // SECCIÓN: DASHBOARD (inicio)
    // =========================================================

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("seccion", "inicio");
        return "dashboardAdministrador";
    }


    // =========================================================
    // SECCIÓN: CLASES
    // =========================================================

    @GetMapping("/clases")
    public String verClases(Model model) {
    	List<Clase> clases = claseService.obtenerTodasClases();
        model.addAttribute("clases", claseService.obtenerTodasClases());
        model.addAttribute("tiposClase", tipoClaseService.obtenerTiposClase());
        model.addAttribute("monitores", usuarioService.obtenerMonitores());
        model.addAttribute("claseForm", new Clase()); // objeto vacío para el formulario crear
        model.addAttribute("seccion", "clases");
        
     // Horarios ordenados por clase
        Map<Long, List<Horario>> horariosPorClase = new LinkedHashMap<>();
        for (Clase c : clases) {
            horariosPorClase.put(c.getId(), horarioService.obtenerPorClaseOrdenados(c.getId()));
        }
        model.addAttribute("horariosPorClase", horariosPorClase);
        
        
        return "dashboardAdministrador";
    }

    @PostMapping("/clases")
    public String crearClase(
            @ModelAttribute Clase clase,
            @RequestParam(required = false) String nuevoTipoNombre,
            @RequestParam(required = false) String nuevoTipoDescripcion
    ) {

        // Si el usuario escribe un nuevo tipo, se crea
        if (nuevoTipoNombre != null && !nuevoTipoNombre.isEmpty()) {
            TipoClase tipo = new TipoClase();
            tipo.setNombre(nuevoTipoNombre);
            tipo.setDescripcion(nuevoTipoDescripcion);

            tipoClaseService.guardar(tipo);
            clase.setTipoClase(tipo);
        }

        // Guardar la clase (con tipo nuevo o existente)
        claseService.guardarClase(clase);

        return "redirect:/admin/clases";
    }

    @PostMapping("/clases/{id}/editar")
    public String editarClase(@PathVariable Long id, @ModelAttribute Clase clase) {
        clase.setId(id);
        claseService.guardarClase(clase);
        return "redirect:/admin/clases";
    }

    @PostMapping("/clases/{id}/eliminar")
    public String eliminarClase(@PathVariable Long id) {
        claseService.eliminarClase(id);
        return "redirect:/admin/clases";
    }
    
    // =========================================================
    // SECCIÓN: CLASES HORARIOS
    // =========================================================
    
    @PostMapping("/clases/{id}/horarios")
    public String crearHorario(@PathVariable Long id, @ModelAttribute Horario horario) {
        Clase claseBD = claseService.obtenerClasePorId(id);
        horario.setClase(claseBD);
        horarioService.guardarHorario(horario);
        return "redirect:/admin/clases";
    }
    
    @PostMapping("/clases/{claseId}/horarios/{horarioId}/editar")
    public String editarHorario(@PathVariable Long claseId, @PathVariable Long horarioId,
                                 @ModelAttribute Horario horario) {
        Clase claseBD = claseService.obtenerClasePorId(claseId);
        horario.setClase(claseBD);
        horario.setId(horarioId);
        horarioService.guardarHorario(horario);
        return "redirect:/admin/clases";
    }

    @PostMapping("/clases/{claseId}/horarios/{horarioId}/eliminar")
    public String eliminarHorario(@PathVariable Long claseId, @PathVariable Long horarioId) {
        horarioService.eliminarHorario(horarioId);
        return "redirect:/admin/clases";
    }
     


    // =========================================================
    // SECCIÓN: USUARIOS
    // =========================================================

    @GetMapping("/usuarios")
    public String verUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.obtenerUsuarios());
        model.addAttribute("usuarioForm", new Usuario()); // objeto vacío para el formulario crear
        model.addAttribute("roles", rolService.obtenerRoles());
        model.addAttribute("seccion", "usuarios");
        return "dashboardAdministrador";
    }

    @PostMapping("/usuarios")
    public String crearUsuario(@ModelAttribute Usuario usuario) {
        usuarioService.guardarUsuario(usuario);
        return "redirect:/admin/usuarios";
    }

    @PostMapping("/usuarios/{id}/editar")
    public String editarUsuario(@PathVariable Long id,
                                @RequestParam String nombre,
                                @RequestParam String apellidos,
                                @RequestParam String email,
                                @RequestParam(required = false) String telefono,
                                @RequestParam Long rolId,
                                @RequestParam Boolean estado,
                                @RequestParam(required = false) @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd") LocalDate fechaPago,
                                RedirectAttributes redirectAttributes) {
        try {
            // Traer el usuario existente
            Usuario usuario = usuarioService.obtenerUsuarioPorId(id);

            // Actualizar campos
            usuario.setNombre(nombre);
            usuario.setApellidos(apellidos);
            usuario.setEmail(email);
            usuario.setTelefono(telefono);
            usuario.setRol(usuarioService.obtenerRolPorId(rolId));
            usuario.setEstado(estado);
            usuario.setFechaPago(fechaPago); // <- Aquí guardamos la fecha

            // Guardar cambios
            usuarioService.guardarUsuario(usuario);

            redirectAttributes.addFlashAttribute("mensaje", "Usuario actualizado correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar usuario: " + e.getMessage());
        }

        return "redirect:/admin/usuarios";
    }
    
    @PostMapping("/usuarios/{id}/eliminar")
    public String eliminarUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.eliminarUsuario(id);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario eliminado correctamente");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar usuario: " + e.getMessage());
        }
        return "redirect:/admin/usuarios";
    }
    

    // =========================================================
    // SECCIÓN: MONITORES
    // =========================================================

    @GetMapping("/monitores")
    public String verMonitores(Model model) {
        model.addAttribute("monitores", usuarioService.obtenerMonitores());
        model.addAttribute("clases", claseService.obtenerTodasClases());
        model.addAttribute("seccion", "monitores");
        return "dashboardAdministrador";
    }

    @PostMapping("/monitores/asignar")
    public String asignarMonitor(@RequestParam(required = false) Long monitorId,
                                 @RequestParam Long claseId) {

        // Traemos la clase usando el servicio
        Clase clase = claseService.obtenerClasePorId(claseId);

        if (monitorId == null) {
            // Desasignar monitor
            clase.setMonitor(null);
        } else {
            // Traemos el monitor usando el servicio
            Usuario monitor = usuarioService.obtenerUsuarioPorId(monitorId);
            clase.setMonitor(monitor);
        }

        // Guardamos la clase con el monitor asignado o desasignado
        claseService.guardarClase(clase);

        return "redirect:/admin/monitores?mensaje=Monitor actualizado";
    }



    // =========================================================
    // SECCIÓN: ESTADÍSTICAS
    // =========================================================

    @GetMapping("/estadisticas")
    public String verEstadisticas(Model model) {
        model.addAttribute("totalUsuarios", usuarioService.contarUsuarios());
        model.addAttribute("totalClases", claseService.contarClases());
        model.addAttribute("totalMonitores", usuarioService.contarMonitores());
        model.addAttribute("seccion", "estadisticas");
        return "dashboardAdministrador";
    }


  

	
}