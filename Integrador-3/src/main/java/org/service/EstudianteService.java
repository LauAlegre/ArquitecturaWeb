package org.service;

import org.dto.EstudianteDTO;
import org.mapper.EstudianteMapper;
import org.model.Estudiante;
import org.repository.EstudianteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EstudianteService {

    private final EstudianteRepository repo;

    public EstudianteService(EstudianteRepository estudianteRepository) {
        this.repo = estudianteRepository;
    }

    // a) Dar de alta un estudiante
    public EstudianteDTO crearEstudiante(EstudianteDTO estudianteDTO) {
        Estudiante estudiante = EstudianteMapper.toEntity(estudianteDTO);
        Estudiante guardado = repo.save(estudiante);
        return EstudianteMapper.toDto(guardado);
    }

    // c) Recuperar todos los estudiantes ordenados por apellido
    public List<EstudianteDTO> obtenerEstudiantesOrdenadosPorApellido() {
        return repo.findAllByOrderByApellidoAsc()
                .stream()
                .map(EstudianteMapper::toDto)
                .collect(Collectors.toList());
    }

    // d) Recuperar un estudiante por número de libreta
    public EstudianteDTO obtenerEstudiantePorNroLibreta(Long nroLibreta) {
        Optional<Estudiante> estudiante = repo.findByNroLibreta(nroLibreta);
        return estudiante.map(EstudianteMapper::toDto).orElse(null);
    }

    // e) Recuperar todos los estudiantes por género
    public List<EstudianteDTO> obtenerEstudiantesPorGenero(String genero) {
        return repo.findByGeneroIgnoreCase(genero)
                .stream()
                .map(EstudianteMapper::toDto)
                .collect(Collectors.toList());
    }

    // g) Recuperar estudiantes de una carrera filtrados por ciudad
    public List<EstudianteDTO> obtenerEstudiantesPorCarreraYCiudad(Long idCarrera, String ciudadResidencia) {
        return repo.findByCarreraIdAndCiudadResidencia(idCarrera, ciudadResidencia)
                .stream()
                .map(EstudianteMapper::toDto)
                .collect(Collectors.toList());
    }
}
