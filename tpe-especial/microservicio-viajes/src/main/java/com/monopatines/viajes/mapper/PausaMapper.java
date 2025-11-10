package com.monopatines.viajes.mapper;

import com.monopatines.viajes.dto.PausaDTO;
import com.monopatines.viajes.model.PausaModel;
import org.springframework.stereotype.Component;

@Component
public class PausaMapper {
    public PausaDTO toDTO(PausaModel p) {
        if (p == null) return null;
        PausaDTO dto = new PausaDTO();
        dto.setId(p.getId());
        dto.setFechaInicio(p.getFechaInicio());
        dto.setFechaFin(p.getFechaFin());
        dto.setDuracionMinutos(p.getDuracionMinutos());
        if (p.getViaje() != null) dto.setViajeId(p.getViaje().getId());
        return dto;
    }
    public PausaModel toEntity(PausaDTO dto) {
        if (dto == null) return null;
        PausaModel p = new PausaModel();
        p.setId(dto.getId());
        p.setFechaInicio(dto.getFechaInicio());
        p.setFechaFin(dto.getFechaFin());
        p.setDuracionMinutos(dto.getDuracionMinutos());
        // viaje se setea en el servicio para evitar referencias incompletas
        return p;
    }
}