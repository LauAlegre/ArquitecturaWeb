package org.example.microserviciomonopatines.controller;

import org.example.microserviciomonopatines.dto.ParadaDTO;
import org.example.microserviciomonopatines.service.ParadaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/paradas")
public class ParadaController {

    private final ParadaService service;

    public ParadaController(ParadaService service) {
        this.service = service;
    }

    @GetMapping
    public List<ParadaDTO> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ParadaDTO buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping("/crear/{idAdmin}")
    public ParadaDTO crear(@RequestBody ParadaDTO dto, @PathVariable Long idAdmin) {
        return service.crear(dto, idAdmin);
    }

    @PutMapping("/{id}")
    public ParadaDTO actualizar(@PathVariable Long id, @RequestBody ParadaDTO dto) {
        return service.actualizar(id, dto);
    }

    @DeleteMapping("/borrar/{idAdmin}")
    public void eliminar(@PathVariable Long id, @PathVariable Long idAdmin) {
        service.eliminar(id, idAdmin);
    }
}
