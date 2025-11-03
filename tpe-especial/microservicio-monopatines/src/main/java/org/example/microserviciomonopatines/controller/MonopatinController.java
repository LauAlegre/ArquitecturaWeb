package org.example.microserviciomonopatines.controller;

import org.example.microserviciomonopatines.dto.MonopatinDTO;
import org.example.microserviciomonopatines.service.MonopatinService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/monopatines")
public class MonopatinController {

    private final MonopatinService service;

    public MonopatinController(MonopatinService service) {
        this.service = service;
    }

    @GetMapping
    public List<MonopatinDTO> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public MonopatinDTO buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    public MonopatinDTO crear(@RequestBody MonopatinDTO dto) {
        return service.crear(dto);
    }

    @PutMapping("/{id}")
    public MonopatinDTO actualizar(@PathVariable Long id, @RequestBody MonopatinDTO dto) {
        return service.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }

    // --- Operaciones adicionales específicas del dominio ---

    @PutMapping("/{id}/estado")
    public MonopatinDTO cambiarEstado(@PathVariable Long id, @RequestParam String estado) {
        return service.cambiarEstado(id, estado);
    }

    @PutMapping("/{id}/ubicacion")
    public MonopatinDTO actualizarUbicacion(@PathVariable Long id,
            @RequestParam Double latitud,
            @RequestParam Double longitud) {
        return service.actualizarUbicacion(id, latitud, longitud);
    }

    @GetMapping("/cercanos")
    public List<MonopatinDTO> listarCercanos(@RequestParam("lat") Double latitud,
            @RequestParam("lon") Double longitud,
            @RequestParam("radio") Double radio) {
        return service.listarCercanos(latitud, longitud, radio);
    }

    @GetMapping("/disponibilidad")
    public Object obtenerDisponibilidad() {
        return service.obtenerDisponibilidad();
    }
}
