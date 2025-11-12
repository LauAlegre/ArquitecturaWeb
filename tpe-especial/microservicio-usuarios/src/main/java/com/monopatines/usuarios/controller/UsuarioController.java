package com.monopatines.usuarios.controller;

import com.monopatines.usuarios.dto.UsoCuentaDTO;
import com.monopatines.usuarios.dto.UsuarioDTO;
import com.monopatines.usuarios.service.UsuarioService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping
    public List<UsuarioDTO> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public UsuarioDTO buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    public UsuarioDTO crear(@RequestBody UsuarioDTO dto) {
        return service.crear(dto);
    }

    @PutMapping("/{id}")
    public UsuarioDTO actualizar(@PathVariable Long id, @RequestBody UsuarioDTO dto) {
        return service.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }

    @GetMapping("/{id}/uso/{idUsuario}")
    public UsoCuentaDTO obtenerUso(@PathVariable Long idUsuario,@PathVariable Long id,
                                   @RequestParam LocalDate desde,
                                   @RequestParam LocalDate hasta,
                                   @RequestParam(defaultValue = "false") boolean incluirRelacionados) {
        return service.obtenerUso(id, desde, hasta, incluirRelacionados, idUsuario);
    }
    // ✅ Nuevo endpoint: verificar si un usuario es admin
    @GetMapping("/{id}/es-admin")
    public boolean esAdmin(@PathVariable Long id) {
        return service.esAdmin(id);
    }

    @GetMapping("/por-tipo-cuenta")
    public List<Long> obtenerUsuariosPorTipoCuenta(@RequestParam String tipoCuenta) {
        return service.obtenerUsuariosPorTipoCuenta(tipoCuenta);
    }

}
