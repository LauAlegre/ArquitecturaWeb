package org.mapper;

import org.dto.EstudianteDTO;
import org.model.Estudiante;

import java.util.List;
import java.util.stream.Collectors;

public  class EstudianteMapper {
    private EstudianteMapper() {}

    public static EstudianteDTO toDto(Estudiante entity) {
        if (entity == null) return null;
        return EstudianteDTO.builder()
                .nroDocumento(entity.getNroDocumento())      // ajustar si el pk se llama distinto
                .nombre(entity.getNombre())
                .apellido(entity.getApellido())
                .genero(entity.getGenero())
                .numeroDeLibreta(entity.getNumeroDeLibreta())
                .ciudadResidencia(entity.getCiudadResidencia())
                .build();
    }

    public static Estudiante toEntity(EstudianteDTO dto) {
        if (dto == null) return null;
        Estudiante e = new Estudiante();
         e.setNroDocumento(dto.getNroDocumento());         // descomentar si el pk no es autogenerado
        e.setNombre(dto.getNombre());
        e.setApellido(dto.getApellido());
        e.setGenero(dto.getGenero());
        e.setNumeroDeLibreta(dto.getNumeroDeLibreta());
        e.setCiudadResidencia(dto.getCiudadResidencia());
        return e;
    }


}

