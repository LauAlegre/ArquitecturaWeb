package org.controller;

import org.dto.CarreraDTO;
import org.springframework.web.bind.annotation.*;
import org.service.CarreraService;

import java.util.List;

@RestController
@RequestMapping("/api/carrera")
public class CarreraController {
    private final CarreraService service;
    public CarreraController(CarreraService carreraService) {
        this.service = carreraService;
    }
// f) recuperar las carreras con estudiantes inscriptos, ordenadas por cantidad de inscriptos (desc)
    @GetMapping("/inscriptos-ordenados")
    public List<CarreraDTO> obtenerCarrerasConInscriptosOrdenadasPorCantidadDesc() {
        return service.obtenerCarrerasConInscriptosOrdenadasPorCantidadDesc();
    }

    @PostMapping("/crear")
    public CarreraDTO crearCarrera(@RequestBody CarreraDTO carreraDTO) {
        System.out.println("DTO recibido: " + carreraDTO);
        return service.crearCarrera(carreraDTO);
    }

    @GetMapping("/todas")
    public List<CarreraDTO> obtenerTodasLasCarreras() {
        return service.obtenerTodasLasCarreras();
    }





}
