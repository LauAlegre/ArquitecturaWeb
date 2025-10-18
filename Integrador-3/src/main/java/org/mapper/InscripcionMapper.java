package org.mapper;

import org.dto.CarreraDTO;
import org.dto.EstudianteDTO;
import org.dto.InscripcionDTO;
import org.model.Carrera;
import org.model.Estudiante;
import org.model.Inscripcion;
import org.model.InscripcionId;

import java.util.List;
import java.util.stream.Collectors;

public  class InscripcionMapper {
    private InscripcionMapper() {
    }

    public static InscripcionDTO toDto(Inscripcion entity) {
        if (entity == null) return null;
        InscripcionDTO dto = InscripcionDTO.builder()
                .idEstudiante(entity.getEstudiante().getNroDocumento())   // ajustar si el pk difiere
                .idCarrera(entity.getCarrera().getId_carrera())
                .build();
        dto.setFechaInscripcion(entity.getFechaInscripcion());         // ajustar tipos/atributos
        dto.setFechaGraduado(entity.getFechaGraduado());
        dto.setAntiguedad(entity.getAntiguedad());
        return dto;
    }

    // Requiere las entidades asociadas ya cargadas para garantizar integridad
    public static Inscripcion toEntity(InscripcionDTO dto) {
        if (dto == null) return null;
        Inscripcion insc = new Inscripcion();

        // Clave compuesta
        insc.setId(new InscripcionId(dto.getIdEstudiante(), dto.getIdCarrera()));

        // ✅ Crear objetos de referencia con solo el ID
        Estudiante e = new Estudiante();
        e.setNroDocumento(dto.getIdEstudiante());
        insc.setEstudiante(e);

        Carrera c = new Carrera();
        c.setId(dto.getIdCarrera()); // ⚠️ ESTE ID YA EXISTE EN LA BD
        insc.setCarrera(c);

        insc.setFechaInscripcion(dto.getFechaInscripcion());
        insc.setFechaGraduado(dto.getFechaGraduado());
        insc.setAntiguedad(dto.getAntiguedad());
        return insc;
    }

}



