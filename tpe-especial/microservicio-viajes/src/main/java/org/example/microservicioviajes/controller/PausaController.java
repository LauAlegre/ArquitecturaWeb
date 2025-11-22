package org.example.microservicioviajes.controller;

import org.example.microservicioviajes.dto.PausaDTO;
import org.example.microservicioviajes.service.PausaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/viajes/{viajeId}/pausas")
public class PausaController {

    private final PausaService service;

    public PausaController(PausaService service) {
        this.service = service;
    }

    // LISTAR todas las pausas embebidas
    @GetMapping
    public List<PausaDTO> listar(@PathVariable String viajeId) {
        return service.listarPorViaje(viajeId);
    }

    // VER pausa abierta
    @GetMapping("/abierta")
    public PausaDTO abierta(@PathVariable String viajeId) {
        return service.pausaAbierta(viajeId);
    }

    // CREAR una pausa (iniciar)
    @PostMapping
    public PausaDTO iniciar(@PathVariable String viajeId) {
        return service.crear(viajeId);
    }

    // CERRAR la pausa abierta
    @PutMapping("/cerrar")
    public PausaDTO cerrar(@PathVariable String viajeId) {
        return service.cerrar(viajeId);
    }
}
