package org.example.microservicioviajes.mapper;

import org.example.microservicioviajes.dto.ViajeDTO;
import org.example.microservicioviajes.model.ViajeModel;
import org.springframework.stereotype.Component;

@Component
public class ViajeMapper {

    public ViajeDTO toDTO(ViajeModel v) {
        if (v == null)
            return null;

        ViajeDTO dto = new ViajeDTO();

        dto.setId(v.getId());
        dto.setFechaInicio(v.getFechaInicio());
        dto.setFechaFin(v.getFechaFin());
        dto.setKmRecorridos(v.getKmRecorridos());
        dto.setCuentaId(v.getCuentaId());
        dto.setMonopatinId(v.getMonopatinId());
        dto.setUsuarioId(v.getUsuarioId());

        // Si querés incluir pausas en el DTO podés mapearlas aquí

        return dto;
    }

    public ViajeModel toEntity(ViajeDTO dto) {
        if (dto == null)
            return null;

        ViajeModel v = new ViajeModel();

        v.setId(dto.getId()); // Mongo lo ignora si es null y genera uno
        v.setFechaInicio(dto.getFechaInicio());
        v.setFechaFin(dto.getFechaFin());
        v.setKmRecorridos(dto.getKmRecorridos());
        v.setCuentaId(dto.getCuentaId());
        v.setMonopatinId(dto.getMonopatinId());
        v.setUsuarioId(dto.getUsuarioId());

        // Pausas quedan vacías al crear un viaje nuevo
        v.setPausas(new java.util.ArrayList<>());

        return v;
    }
}
