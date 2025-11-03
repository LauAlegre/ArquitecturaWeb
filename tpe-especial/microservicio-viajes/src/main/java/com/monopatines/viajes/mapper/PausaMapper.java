package com.monopatines.viajes.mapper;

import com.monopatines.viajes.dto.PausaDTO;
import com.monopatines.viajes.model.PausaModel;

public class PausaMapper {
    public PausaDTO toDTO(PausaModel pausa) {
        if (pausa == null) return null;
        PausaDTO dto = new PausaDTO();
        dto.setIdPausa(pausa.getId());
        dto.setFecha_inicio(pausa.getFecha_inicio());
        dto.setFecha_fin(pausa.getFecha_fin());
        dto.setDuracion_minutos(pausa.getDuracion_minutos());

        // evitamos recursión: solo guardamos el id del viaje
        if (pausa.getViaje() != null) {
            dto.setId_viaje(pausa.getViaje().getId().intValue());
        }

        return dto;
    }

    public PausaModel toEntity(PausaDTO dto) {
        if (dto == null) return null;
        PausaModel pausa = new PausaModel();
        pausa.setId(dto.getIdPausa());
        pausa.setFecha_inicio(dto.getFecha_inicio());
        pausa.setFecha_fin(dto.getFecha_fin());
        pausa.setDuracion_minutos(dto.getDuracion_minutos());
        // no seteamos el viaje completo aquí (solo se hará en el servicio si es necesario)
        return pausa;
    }
}


