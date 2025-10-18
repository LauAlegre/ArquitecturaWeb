package org.controller;

import org.dto.EstudianteDTO;
import org.service.EstudianteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estudiante")
public class  EstudianteController {

    private final EstudianteService service;

    public EstudianteController(EstudianteService estudianteService) {
        this.service = estudianteService;
    }

    @PostMapping("/crear")
    public List<EstudianteDTO> crearEstudiantes(@RequestBody List<EstudianteDTO> estudiantes) {
        return estudiantes.stream()
                .map(service::crearEstudiante)
                .toList();
    }

    //Dejo comentado el metodo para crear un solo estudiante en caso de ser necesario

//    @PostMapping("/crear")
//    public EstudianteDTO crearEstudiante(@RequestBody EstudianteDTO dto) {
//        return service.crearEstudiante(dto);
//    }



    // c) Recuperar todos los estudiantes ordenados por apellido
    @GetMapping("/ordenados-por-apellido")
    public List<EstudianteDTO> obtenerEstudiantesOrdenadosPorApellido() {
        return service.obtenerEstudiantesOrdenadosPorApellido();
    }

    // d) Recuperar un estudiante por número de libreta
    @GetMapping("/{nroLibreta}")
    public ResponseEntity<EstudianteDTO> obtenerEstudiantePorNroLibreta(@PathVariable Long nroLibreta) {
        EstudianteDTO dto = service.obtenerEstudiantePorNroLibreta(nroLibreta);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }

    // e) Recuperar todos los estudiantes por género
    @GetMapping("/por-genero")
    public List<EstudianteDTO> obtenerEstudiantesPorGenero(@RequestParam String genero) {
        return service.obtenerEstudiantesPorGenero(genero);
    }

    // g) Recuperar estudiantes de una carrera filtrados por ciudad
    @GetMapping("/por-carrera-ciudad")
    public List<EstudianteDTO> obtenerEstudiantesPorCarreraYCiudad(
            @RequestParam Long carreraId,
            @RequestParam String ciudad) {
        return service.obtenerEstudiantesPorCarreraYCiudad(carreraId, ciudad);
    }
}
