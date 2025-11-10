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

    @GetMapping
    public List<PausaDTO> listar(@PathVariable Long viajeId) {
        return service.listarPorViaje(viajeId);
    }

    @GetMapping("/abierta")
    public PausaDTO abierta(@PathVariable Long viajeId) {
        return service.pausaAbierta(viajeId);
    }

    @PostMapping
    public PausaDTO iniciar(@PathVariable Long viajeId) {
        return service.crear(viajeId);
    }

    @PutMapping("/{pausaId}")
    public PausaDTO actualizar(@PathVariable("viajeId") Long viajeId,
                               @PathVariable("pausaId") Long pausaId,
                               @RequestBody PausaDTO dto) {
        return service.actualizar(pausaId, dto);
    }
}