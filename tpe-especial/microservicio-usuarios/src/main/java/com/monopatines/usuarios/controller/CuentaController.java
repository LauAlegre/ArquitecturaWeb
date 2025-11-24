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

    @PostMapping("/crear")
    public CuentaDTO crear(@RequestBody CuentaDTO dto) {
        return service.crear(dto);
    }

    @PutMapping("/{id}")
    public CuentaDTO actualizar(@PathVariable Long id, @RequestBody CuentaDTO dto) {
        return service.actualizar(id, dto);
    }

    @DeleteMapping("/borrar/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }

    @PutMapping("/{id}/anular")
    public void anularCuenta(@PathVariable Long id) {
        service.anularCuenta(id);
    }

    @PutMapping("/{id}/activar")
    public void activarCuenta(@PathVariable Long id) {
        service.activarCuenta(id);
    }

    @PostMapping("/{idCuenta}/asociar-usuario/{idUsuario}")
    public CuentaDTO asociarUsuario(
            @PathVariable Long idCuenta,
            @PathVariable Long idUsuario) {
        return service.asociarUsuario(idCuenta, idUsuario);
    }
    @PutMapping("/{id}/debitar/{monto}")
    public String debitar(@PathVariable Long id, @PathVariable double monto) {
        service.debitarSaldo(id, monto);
        return "Se debitó $" + monto + " de la cuenta " + id;
    }


}

