package org.mapper;

import org.dto.CarreraDTO;
import org.model.Carrera;

import java.util.List;
import java.util.stream.Collectors;

public  class CarreraMapper {
    private CarreraMapper() {}

    public static CarreraDTO toDto(Carrera entity) {
        if (entity == null) return null;
        return CarreraDTO.builder()
                .idCarrera(entity.getId_carrera())           // ajustar si el id se llama distinto
                .nombre(entity.getNombre())
                .duracionAnios(entity.getDuracion_anios())
                .build();
    }

    public static Carrera toEntity(CarreraDTO dto) {
        if (dto == null) return null;
        Carrera c = new Carrera();
        c.setId(dto.getIdCarrera());            // descomentar si se permite setear el id
        c.setNombre(dto.getNombre());
        c.setDuracion_anios(dto.getDuracionAnios());
        return c;
    }


}

