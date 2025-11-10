package org.example.microserviciomonopatines.mapper;

import org.springframework.stereotype.Component;
import org.example.microserviciomonopatines.dto.ParadaDTO;
import org.example.microserviciomonopatines.model.Parada;

@Component
public class ParadaMapper {
    // --- Métodos auxiliares de mapeo ---

    public ParadaDTO toDTO(Parada p) {
        return new ParadaDTO(
                p.getId(),
                p.getNombre(),
                p.getLatitud(),
                p.getLongitud(),
                p.getCapacidad());
    }

    public Parada toEntity(ParadaDTO dto) {
        Parada p = new Parada();
        p.setNombre(dto.getNombre());
        p.setLatitud(dto.getLatitud());
        p.setLongitud(dto.getLongitud());
        p.setCapacidad(dto.getCapacidad());
        return p;
    }
}
