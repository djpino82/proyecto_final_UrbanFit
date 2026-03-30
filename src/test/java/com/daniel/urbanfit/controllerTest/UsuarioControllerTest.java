package com.daniel.urbanfit.controllerTest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import com.daniel.urbanfit.controller.UsuarioController;
import com.daniel.urbanfit.service.UsuarioService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
@AutoConfigureMockMvc(addFilters = false) // Desactiva filtros de seguridad para que no nos bloqueen los tests
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc; // Simula peticiones web

    @MockBean
    private UsuarioService usuarioService; // Creamos un "falso" servicio para no tocar la base de datos

    // ===============================
    // TEST 1: GET formulario de registro
    // ===============================
    @Test
    public void testMostrarFormulario() throws Exception {
        mockMvc.perform(get("/registro"))
               .andExpect(status().isOk()) // Comprueba que devuelve HTTP 200
               .andExpect(view().name("registro")); // Comprueba que la vista es "registro"
    }

    // ===============================
    // TEST 2: POST registro correcto
    // ===============================
    @Test
    public void testRegistroCorrecto() throws Exception {
        mockMvc.perform(post("/registro")
                .param("nombre", "Ana")
                .param("apellidos", "Lopez")
                .param("email", "ana@example.com")
                .param("dni", "12345678B")
                .param("password", "123456")
                .param("telefono", "987654321"))
               .andExpect(status().is3xxRedirection()); // Comprueba que redirige
    }

    // ===============================
    // TEST 3: POST registro con error
    // ===============================
    @Test
    public void testRegistroError() throws Exception {
        mockMvc.perform(post("/registro")
                .param("nombre", "") // Dejamos el nombre vacío para que de error
                .param("apellidos", "Lopez")
                .param("email", "ana@example.com")
                .param("dni", "12345678B")
                .param("password", "123456")
                .param("telefono", "987654321"))
               .andExpect(status().isOk()); // No redirige, sigue en la misma página
    }
}