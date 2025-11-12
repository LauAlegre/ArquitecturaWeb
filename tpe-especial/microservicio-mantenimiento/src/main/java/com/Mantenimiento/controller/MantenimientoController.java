package com.Mantenimiento.controller;

import com.Mantenimiento.dto.MantenimientoDTO;
import com.Mantenimiento.service.MantenimientoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mantenimientos")
public class MantenimientoController {

    private final MantenimientoService service;

    public MantenimientoController(MantenimientoService service) {
        this.service = service;
    }

    // GET /mantenimientos  -> lista
    @GetMapping
    public List<MantenimientoDTO> listar() {
        return service.listar();
    }

    // GET /mantenimientos/{id}  -> uno
    @GetMapping("/{id}")
    public MantenimientoDTO buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    // PUT /mantenimientos/{id}  -> actualizar
    @PutMapping("/{id}")
    public MantenimientoDTO actualizar(@PathVariable Long id,
                                       @RequestBody MantenimientoDTO dto) {
        return service.actualizar(id, dto);
    }

    // POST /mantenimientos/iniciar  -> iniciar mantenimiento “real”
    @PostMapping("/iniciar")
    @ResponseStatus(HttpStatus.CREATED)
    public MantenimientoDTO iniciar(@RequestParam Long monopatinId,
                                    @RequestParam String descripcion,
                                    @RequestParam String responsable,
                                    @RequestParam Double costo) {
        return service.iniciar(monopatinId, descripcion, responsable, costo);
    }

    // PUT /mantenimientos/{id}/finalizar  -> finalizar
    @PutMapping("/{id}/finalizar")
    public MantenimientoDTO finalizar(@PathVariable Long id,
                                      @RequestParam String observaciones,
                                      @RequestParam(required = false) Long paradaId) {
        return service.finalizar(id, observaciones, paradaId);
    }

    // GET /mantenimientos/activos  -> solo activos
    @GetMapping("/activos")
    public List<MantenimientoDTO> activos() {
        return service.activos();
    }
}
