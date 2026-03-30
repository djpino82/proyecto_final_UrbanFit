package com.daniel.urbanfit.controllerTest;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.daniel.urbanfit.controller.DashboardAdministradorController;
import com.daniel.urbanfit.service.ClaseService;
import com.daniel.urbanfit.service.HorarioService;
import com.daniel.urbanfit.service.RolService;
import com.daniel.urbanfit.service.TipoClaseService;
import com.daniel.urbanfit.service.UsuarioService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@WebMvcTest(DashboardAdministradorController.class)
@AutoConfigureMockMvc(addFilters = false) // Desactiva filtros de seguridad para que no nos bloqueen los tests
public class DashboardAdministradorControllerTest {

    @Autowired
    private MockMvc mockMvc; // Simula las peticiones HTTP

    // Mockeamos los servicios que usa el controller, así no toca la base de datos
    @MockBean
    private ClaseService claseService;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private TipoClaseService tipoClaseService;

    @MockBean
    private RolService rolService;

    @MockBean
    private HorarioService horarioService;

    // =============================================================
    // Test 1: GET /admin/dashboard
    // =============================================================
    @Test
    public void dashboard_get_ok() throws Exception {
        mockMvc.perform(get("/admin/dashboard"))
                .andExpect(status().isOk())                 // HTTP 200
                .andExpect(view().name("dashboardAdministrador")) // Debe cargar la vista correcta
                .andExpect(model().attributeExists("seccion"));  // Debe tener atributo "seccion"
    }

    // =============================================================
    // Test 2: GET /admin/clases
    // =============================================================
    @Test
    public void verClases_get_ok() throws Exception {
        mockMvc.perform(get("/admin/clases"))
                .andExpect(status().isOk())                 
                .andExpect(view().name("dashboardAdministrador")) 
                .andExpect(model().attributeExists("clases"))       // Debe existir lista de clases
                .andExpect(model().attributeExists("tiposClase"))   // Debe existir lista de tipos de clase
                .andExpect(model().attributeExists("monitores"))    // Debe existir lista de monitores
                .andExpect(model().attributeExists("claseForm"))    // Debe existir objeto para el formulario
                .andExpect(model().attributeExists("seccion"));     
    }

    // =============================================================
    // Test 3: GET /admin/usuarios
    // =============================================================
    @Test
    public void verUsuarios_get_ok() throws Exception {
        mockMvc.perform(get("/admin/usuarios"))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboardAdministrador"))
                .andExpect(model().attributeExists("usuarios"))      // Debe existir lista de usuarios
                .andExpect(model().attributeExists("usuarioForm"))   // Objeto para formulario
                .andExpect(model().attributeExists("roles"))         // Lista de roles
                .andExpect(model().attributeExists("seccion"));      
    }
}
