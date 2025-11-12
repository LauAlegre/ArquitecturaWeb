package com.monopatines.usuarios.controller;

import com.monopatines.usuarios.dto.CuentaDTO;
import com.monopatines.usuarios.service.CuentaService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/cuentas")
public class CuentaController {

    private final CuentaService service;

    public CuentaController(CuentaService service) {
        this.service = service;
    }

    @GetMapping
    public List<CuentaDTO> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public CuentaDTO buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    public CuentaDTO crear(@RequestBody CuentaDTO dto) {
        return service.crear(dto);
    }

    @PutMapping("/{id}")
    public CuentaDTO actualizar(@PathVariable Long id, @RequestBody CuentaDTO dto) {
        return service.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }

    @PutMapping("/{id}/anular/{idAdmin}")
    public void anularCuenta(@PathVariable Long id, @PathVariable Long idAdmin) {
        service.anularCuenta(id, idAdmin);
    }

    @PutMapping("/{id}/activar/{idAmin}")
    public void activarCuenta(@PathVariable Long id
    , @PathVariable Long idAmin) {
        service.activarCuenta(id, idAmin);
    }

    @PostMapping("/{idCuenta}/asociar-usuario/{idUsuario}")
    public CuentaDTO asociarUsuario(
            @PathVariable Long idCuenta,
            @PathVariable Long idUsuario) {
        return service.asociarUsuario(idCuenta, idUsuario);
    }

}

