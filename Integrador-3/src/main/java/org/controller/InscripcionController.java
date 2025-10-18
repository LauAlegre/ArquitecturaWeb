package org.controller;

import org.dto.CarreraDTO;
import org.dto.CarreraReporteDTO;
import org.dto.EstudianteDTO;
import org.dto.InscripcionDTO;
import org.service.IncripcionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
    @RequestMapping("/api/inscripcion")
public class InscripcionController {

    private final IncripcionService service;

    public InscripcionController(IncripcionService service) {
        this.service = service;
    }

    // b) Matricular un estudiante en una carrera
    @PostMapping("/matricular")
    public List<InscripcionDTO> crearVarias(@RequestBody List<InscripcionDTO> requests) {
        return requests.stream()
                .map(req -> service.crearInscripcion(req))
                .toList();
    }

    // Dejo comentado el metodo para crear una sola inscripcion en caso de ser necesario


//    @PostMapping("/matricular")
//    public InscripcionDTO crearInscripcion(@RequestBody InscripcionDTO inscripcionDTO)
//    {
//        return service.crearInscripcion(inscripcionDTO);
//    }

    // h) Generar un reporte anual de carreras con cantidad de inscriptos y egresados
    @GetMapping("/reporte")
    public List<CarreraReporteDTO> getReporteCarrerasPorAnio() {
        return service.generarReporteCarrerasPorAnio();
    }


}
