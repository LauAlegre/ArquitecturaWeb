package com.monopatines.viajes.controller;

import com.monopatines.viajes.dto.ViajeDTO;
import com.monopatines.viajes.service.ViajeService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/viajes")
public class ViajeController {

    private final ViajeService service;

    public ViajeController(ViajeService service) {
        this.service = service;
    }

    @GetMapping
    public List<ViajeDTO> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ViajeDTO buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    public ViajeDTO crear(@RequestParam Long cuentaId, @RequestParam Long monopatinId) {
        return service.crear(cuentaId, monopatinId);
    }

    @PutMapping("/{viajeId}/finalizar")
    public ViajeDTO finalizar(@PathVariable Long viajeId, @RequestParam BigDecimal kmRecorridos) {
        return service.finalizar(viajeId, kmRecorridos);
    }

    @GetMapping("/activo")
    public ViajeDTO activoPorMonopatin(@RequestParam Long monopatinId) {
        return service.obtenerActivoPorMonopatin(monopatinId);
    }

    @GetMapping(params = "cuentaId")
    public List<ViajeDTO> listarPorCuenta(@RequestParam Long cuentaId) {
        return service.listarPorCuenta(cuentaId);
    }

    @GetMapping(params = "monopatinId")
    public List<ViajeDTO> listarPorMonopatin(@RequestParam Long monopatinId) {
        return service.listarPorMonopatin(monopatinId);
    }
}
