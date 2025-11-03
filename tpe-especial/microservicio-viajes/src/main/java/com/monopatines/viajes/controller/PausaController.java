package com.monopatines.viajes.controller;

import com.monopatines.viajes.dto.PausaDTO;
import com.monopatines.viajes.service.PausaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/viajes/{viajeId}/pausas")
public class PausaController {

    private final PausaService service;

    public PausaController(PausaService service) {
        this.service = service;
    }

    // Listar todas las pausas del viaje
    @GetMapping
    public List<PausaDTO> listar(@PathVariable Long viajeId) {
        return service.listarPorViaje(viajeId);
    }

    // Obtener la pausa abierta (si existe)
    @GetMapping("/abierta")
    public PausaDTO obtenerAbierta(@PathVariable Long viajeId) {
        return service.obtenerAbierta(viajeId);
    }

    // Iniciar una pausa para el viaje
    @PostMapping
    public PausaDTO iniciar(@PathVariable Long viajeId) {
        return service.iniciar(viajeId);
    }

    // Finalizar una pausa específica
    @PutMapping("/{pausaId}/finalizar")
    public PausaDTO finalizar(@PathVariable Long viajeId, @PathVariable Long pausaId) {
        return service.finalizar(viajeId, pausaId);
    }
}
