package org.service;

import org.dto.CarreraDTO;
import org.dto.CarreraReporteDTO;
import org.dto.EstudianteDTO;
import org.dto.InscripcionDTO;
import org.mapper.InscripcionMapper;
import org.model.Inscripcion;
import org.repository.InscripcionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IncripcionService {
    private final InscripcionRepository repo;

    public IncripcionService(InscripcionRepository incripcionRepository) {
        this.repo = incripcionRepository;
    }

    //b) matricular un estudiante en una carrera
    public InscripcionDTO crearInscripcion(InscripcionDTO inscripcionDTO) {
        Inscripcion inscripcion = InscripcionMapper.toEntity(inscripcionDTO);
        Inscripcion guardado = repo.save(inscripcion);
        return InscripcionMapper.toDto(guardado);
    }
    public List<CarreraReporteDTO> generarReporteCarrerasPorAnio() {
        return repo.getReporteCarrerasPorAnio()
                .stream()
                .map(r -> CarreraReporteDTO.builder()
                        .nombreCarrera((String) r[0])
                        .anio((Integer) r[1])
                        .inscriptos((Long) r[2])
                        .egresados((Long) r[3])
                        .build())
                .collect(Collectors.toList());
    }


}
