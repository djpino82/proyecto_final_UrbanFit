package com.daniel.urbanfit.scheduler;



import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.daniel.urbanfit.entity.Usuario;
import com.daniel.urbanfit.service.UsuarioService;

import java.time.LocalDate;
import java.util.List;

@Component
public class ControlPagosScheduler {

    private final UsuarioService usuarioService;

    public ControlPagosScheduler(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Se ejecuta todos los días a las 00:01
     * Desactiva automáticamente a los clientes cuyo pago ya cumplió un mes
     */
    @Scheduled(cron = "0 1 0 * * ?") // segundos, minutos, horas, día del mes, mes, día de la semana
    public void desactivarClientesImpagos() {
        LocalDate hoy = LocalDate.now();

        // Traemos todos los usuarios
        List<Usuario> usuarios = usuarioService.obtenerUsuarios();

        for (Usuario u : usuarios) {
            // Solo clientes activos con fecha de pago
            if (u.getRol() != null && "Cliente".equals(u.getRol().getNombre())
                && u.isEstado() && u.getFechaPago() != null) {

                // Fecha de vencimiento = fechaPago + 1 mes
                LocalDate fechaVencimiento = u.getFechaPago().plusMonths(1);

                // Si hoy es igual o después de la fecha de vencimiento, desactivar
                if (!hoy.isBefore(fechaVencimiento)) {
                    u.setEstado(false);
                    usuarioService.guardarUsuario(u);
                    System.out.println("Cliente desactivado automáticamente: " + u.getNombre() + " " + u.getApellidos() +
                                       " | FechaPago: " + u.getFechaPago() +
                                       " | FechaVencimiento: " + fechaVencimiento);
                }
            }
        }
    }
}