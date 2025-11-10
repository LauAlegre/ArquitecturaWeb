package org.example.microserviciomonopatines.mapper;

import org.example.microserviciomonopatines.dto.MonopatinDTO;
import org.example.microserviciomonopatines.model.Monopatin;
import org.springframework.stereotype.Component;

@Component
public class MonopatinMapper {
    // --- Métodos auxiliares de mapeo ---

    public MonopatinDTO toDTO(Monopatin m) {
        return new MonopatinDTO(
                m.getId(),
                m.getEstado(),
                m.getLatitud(),
                m.getLongitud(),
                m.getTotalKm(),
                m.getTotalTiempoUso(),
                m.getParadaId());
    }

    public Monopatin toEntity(MonopatinDTO dto) {
        Monopatin m = new Monopatin();
        m.setEstado(dto.getEstado());
        m.setLatitud(dto.getLatitud());
        m.setLongitud(dto.getLongitud());
        m.setTotalKm(dto.getTotalKm());
        m.setTotalTiempoUso(dto.getTotalTiempoUso());
        m.setParadaId(dto.getParadaId());
        return m;
    }
}
