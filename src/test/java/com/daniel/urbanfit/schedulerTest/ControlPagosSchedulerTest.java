package com.daniel.urbanfit.schedulerTest;

import com.daniel.urbanfit.entity.Rol;
import com.daniel.urbanfit.entity.Usuario;
import com.daniel.urbanfit.scheduler.ControlPagosScheduler;
import com.daniel.urbanfit.service.UsuarioService;
import org.junit.jupiter.api.Test;


import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

public class ControlPagosSchedulerTest {

    @Test
    public void testDesactivarClientesImpagos() {
        // Creamos un mock del servicio
        UsuarioService usuarioService = mock(UsuarioService.class);

        // Creamos algunos usuarios de ejemplo
        Rol rolCliente = new Rol();
        rolCliente.setNombre("Cliente");

        Usuario clienteActivoVencido = new Usuario();
        clienteActivoVencido.setNombre("Juan");
        clienteActivoVencido.setApellidos("Perez");
        clienteActivoVencido.setRol(rolCliente);
        clienteActivoVencido.setEstado(true);
        clienteActivoVencido.setFechaPago(LocalDate.now().minusMonths(1).minusDays(1)); // ya vencido

        Usuario clienteActivoNoVencido = new Usuario();
        clienteActivoNoVencido.setNombre("Ana");
        clienteActivoNoVencido.setApellidos("Gomez");
        clienteActivoNoVencido.setRol(rolCliente);
        clienteActivoNoVencido.setEstado(true);
        clienteActivoNoVencido.setFechaPago(LocalDate.now().minusDays(10)); // no vencido

        Usuario clienteInactivo = new Usuario();
        clienteInactivo.setNombre("Luis");
        clienteInactivo.setApellidos("Lopez");
        clienteInactivo.setRol(rolCliente);
        clienteInactivo.setEstado(false);
        clienteInactivo.setFechaPago(LocalDate.now().minusMonths(2)); // ya inactivo

        List<Usuario> usuarios = Arrays.asList(clienteActivoVencido, clienteActivoNoVencido, clienteInactivo);

        // Cuando se llame a obtenerUsuarios, devolvemos nuestra lista
        when(usuarioService.obtenerUsuarios()).thenReturn(usuarios);

        // Creamos el scheduler
        ControlPagosScheduler scheduler = new ControlPagosScheduler(usuarioService);

        // Ejecutamos el método
        scheduler.desactivarClientesImpagos();

        // Verificamos: solo Juan fue desactivado y guardado
        verify(usuarioService, times(1)).guardarUsuario(clienteActivoVencido);
        verify(usuarioService, never()).guardarUsuario(clienteActivoNoVencido);
        verify(usuarioService, never()).guardarUsuario(clienteInactivo);

        // Opcional: también puedes imprimir algo para ver que funciona
        System.out.println("Estado Juan: " + clienteActivoVencido.isEstado()); // debe ser false
        System.out.println("Estado Ana: " + clienteActivoNoVencido.isEstado()); // sigue true
    }
}
