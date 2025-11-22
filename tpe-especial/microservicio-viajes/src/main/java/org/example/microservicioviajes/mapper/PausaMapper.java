package org.example.microservicioviajes.mapper;

import org.example.microservicioviajes.dto.PausaDTO;
import org.example.microservicioviajes.model.PausaModel;
import org.springframework.stereotype.Component;

@Component
public class PausaMapper {

    public PausaDTO toDTO(PausaModel p) {
        if (p == null) return null;

        PausaDTO dto = new PausaDTO();
        dto.setFechaInicio(p.getFechaInicio());
        dto.setFechaFin(p.getFechaFin());
        dto.setDuracionMinutos(p.getDuracionMinutos());

        return dto;
    }

    public PausaModel toEntity(PausaDTO dto) {
        if (dto == null) return null;

        PausaModel p = new PausaModel();
        p.setFechaInicio(dto.getFechaInicio());
        p.setFechaFin(dto.getFechaFin());
        p.setDuracionMinutos(dto.getDuracionMinutos());

        return p;
    }
}
