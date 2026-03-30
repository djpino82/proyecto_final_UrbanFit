package com.daniel.urbanfit.controllerTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import com.daniel.urbanfit.controller.ClasesController;

@WebMvcTest(ClasesController.class) // Indicamos que queremos probar ClasesController
@AutoConfigureMockMvc(addFilters = false) // Desactiva filtros de seguridad para que no nos bloqueen los tests
public class ClasesControllerTest {

    @Autowired
    private MockMvc mockMvc; // Simula peticiones HTTP al controller

    // =====================================================
    // Test GET /clases
    // =====================================================
    @Test
    public void verClases_retornaVistaClases() throws Exception {
        mockMvc.perform(get("/clases")) // Hacemos un GET a /clases
               .andExpect(status().isOk()) // Comprobamos que devuelve HTTP 200
               .andExpect(view().name("clases")); // La vista debe llamarse "clases"
    }

    // =====================================================
    // Test GET /clases/horarios
    // =====================================================
    @Test
    public void verHorarios_retornaVistaHorarios() throws Exception {
        mockMvc.perform(get("/clases/horarios")) // GET a /clases/horarios
               .andExpect(status().isOk()) // HTTP 200
               .andExpect(view().name("horarios")); // Vista "horarios"
    }

}
