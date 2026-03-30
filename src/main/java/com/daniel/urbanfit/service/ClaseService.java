package com.daniel.urbanfit.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.daniel.urbanfit.entity.Clase;

import com.daniel.urbanfit.entity.Usuario;
import com.daniel.urbanfit.repository.ClaseRepository;


@Service
public class ClaseService {
    
    @Autowired
    private ClaseRepository claseRepository;
    

    
    // Listar todas las clases
    public List<Clase> obtenerTodasClases() {
        return claseRepository.findAll();
    }
    
    // Guardar Crear o actualizar clase
    public Clase guardarClase(Clase clase) {

        if (clase.getId() != null) {
            // Si es una clase existente, traemos la clase original de la base de datos
            Clase claseExistente = claseRepository.findById(clase.getId())
                .orElseThrow(() -> new RuntimeException("La clase con ID " + clase.getId() + " no existe"));

            // Actualizamos solo los campos que se pueden cambiar
            claseExistente.setNombre(clase.getNombre());
            claseExistente.setCapacidad(clase.getCapacidad());
            claseExistente.setMonitor(clase.getMonitor());
            claseExistente.setTipoClase(clase.getTipoClase());

            // IMPORTANTE: conservar los horarios existentes para no perderlos
            claseExistente.setHorarios(claseExistente.getHorarios());

            return claseRepository.save(claseExistente);
        }

        // Si es nueva clase, se guarda normalmente
        return claseRepository.save(clase);
    }
    
    // Eliminar clase
    public void eliminarClase(Long id) {
        if (!claseRepository.existsById(id)) {
            throw new RuntimeException("La clase con ID " + id + " no existe");
        }
        claseRepository.deleteById(id);
    }
    
    // Trae todas las clases de un monitor
    public List<Clase> obtenerClasesPorMonitor(Long monitorId) {
        return claseRepository.findByMonitorId(monitorId);
    }

    // Trae una clase por su ID
    public Clase obtenerClasePorId(Long claseId) {
        return claseRepository.findById(claseId)
            .orElseThrow(() -> new RuntimeException("La clase con ID " + claseId + " no existe"));
    }
    
    // Contar todas las clases (para estadísticas)
    public long contarClases() {
        return claseRepository.count();
    }

    // Asignar monitor a una clase
    public void asignarMonitor(Long claseId, Long monitorId) {
        Clase clase = claseRepository.findById(claseId)
            .orElseThrow(() -> new RuntimeException("Clase con ID " + claseId + " no existe"));
        
        // Creamos un usuario con solo el ID para no hacer consulta extra
        Usuario monitor = new Usuario();
        monitor.setId(monitorId);
        
        clase.setMonitor(monitor); 
        claseRepository.save(clase);
    }
}