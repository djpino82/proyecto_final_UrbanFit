package com.daniel.urbanfit.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.daniel.urbanfit.repository.TipoClaseRepository;
import com.daniel.urbanfit.entity.TipoClase;

@Service
public class TipoClaseService {
	
	@Autowired
    private TipoClaseRepository tipoClaseRepository;

    // Obtener todos los tipos de clase
    public List<TipoClase> obtenerTiposClase() {
        return tipoClaseRepository.findAll();
    }

    // Obtener un tipo de clase por ID (opcional)
    public TipoClase obtenerTipoClasePorId(Long id) {
        return tipoClaseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("El tipo de clase con ID " + id + " no existe"));
    }

    // Crear o actualizar tipo de clase (opcional)
    public TipoClase guardarTipoClase(TipoClase tipoClase) {
        return tipoClaseRepository.save(tipoClase);
    }

    // Eliminar tipo de clase (opcional)
    public void eliminarTipoClase(Long id) {
        if (!tipoClaseRepository.existsById(id)) {
            throw new RuntimeException("El tipo de clase con ID " + id + " no existe");
        }
        tipoClaseRepository.deleteById(id);
    }
    
    public void guardar(TipoClase tipo) {
        tipoClaseRepository.save(tipo);
    }

}
