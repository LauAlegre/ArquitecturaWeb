package com.monopatines.viajes.mapper;

import com.monopatines.viajes.dto.ViajeDTO;
import com.monopatines.viajes.model.ViajeModel;
import org.springframework.stereotype.Component;

@Component
public class ViajeMapper {
    public ViajeDTO toDTO(ViajeModel v) {
        if (v == null) return null;
        ViajeDTO dto = new ViajeDTO();
        dto.setId(v.getId());
        dto.setFechaInicio(v.getFechaInicio());
        dto.setFechaFin(v.getFechaFin());
        dto.setKmRecorridos(v.getKmRecorridos());
        dto.setCuentaId(v.getCuentaId());
        dto.setMonopatinId(v.getMonopatinId());
        dto.setUsuarioId(v.getUsuarioId());
        return dto;
    }
    public ViajeModel toEntity(ViajeDTO dto) {
        if (dto == null) return null;
        ViajeModel v = new ViajeModel();
        v.setId(dto.getId());
        v.setFechaInicio(dto.getFechaInicio());
        v.setFechaFin(dto.getFechaFin());
        v.setKmRecorridos(dto.getKmRecorridos());
        v.setCuentaId(dto.getCuentaId());
        v.setMonopatinId(dto.getMonopatinId());
        v.setUsuarioId(dto.getUsuarioId());
        return v;
    }
}
