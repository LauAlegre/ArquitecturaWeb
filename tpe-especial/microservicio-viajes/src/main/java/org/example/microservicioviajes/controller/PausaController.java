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

    // 🔹 Listar todas las pausas de un viaje
    @GetMapping
    public List<PausaDTO> listar(@PathVariable Long viajeId) {
        return service.listarPorViaje(viajeId);
    }

    // 🔹 Obtener una pausa por ID
    @GetMapping("/{pausaId}")
    public PausaDTO obtener(@PathVariable Long pausaId) {
        return service.obtenerPorId(pausaId);
    }

    // 🔹 Ver pausa abierta del viaje (si existe)
    @GetMapping("/abierta")
    public PausaDTO abierta(@PathVariable Long viajeId) {
        return service.pausaAbierta(viajeId);
    }

    // 🔹 Iniciar pausa
    @PostMapping
    public PausaDTO iniciar(@PathVariable Long viajeId) {
        return service.crear(viajeId);
    }

    // 🔹 Actualizar pausa
    @PutMapping("/{pausaId}")
    public PausaDTO actualizar(@PathVariable("pausaId") Long pausaId,
                               @RequestBody PausaDTO dto) {
        return service.actualizar(pausaId, dto);
    }

    // 🔹 Cerrar / frenar pausa (endpoint que faltaba)
    @PutMapping("/{pausaId}/cerrar")
    public PausaDTO cerrar(@PathVariable Long pausaId) {
        return service.cerrar(pausaId);
    }

    // 🔹 Eliminar pausa
    @DeleteMapping("/{pausaId}")
    public void eliminar(@PathVariable Long pausaId) {
        service.eliminar(pausaId);
    }
}
