package org.example.microservicioviajes.controller;

import org.example.microservicioviajes.dto.DatosDeFacturacionDTO;
import org.example.microservicioviajes.dto.UsoDTO;
import org.example.microservicioviajes.dto.ViajeDTO;
import org.example.microservicioviajes.dto.MonopatinViajesCountDTO;
import org.example.microservicioviajes.dto.UsoUsuarioDTO;
import org.example.microservicioviajes.service.ViajeService;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
        return service.listar(Pageable.unpaged()).getContent();
    }

    @GetMapping("/{id}")
    public ViajeDTO buscarPorId(@PathVariable String id) {
        return service.obtenerPorId(id);
    }

    @PostMapping("/iniciar")
    public ViajeDTO iniciarViaje(@RequestBody ViajeDTO dto) {
        return service.iniciarViaje(dto);
    }

    @PutMapping("/{id}")
    public ViajeDTO actualizar(@PathVariable String id, @RequestBody ViajeDTO dto) {
        return service.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable String id) {
        service.eliminar(id);
    }

    @PutMapping("/{id}/cerrar")
    public DatosDeFacturacionDTO cerrarViaje(
            @PathVariable String id,
            @RequestParam Double kmRecorridos) {

        return service.cerrarViaje(id, LocalDateTime.now(), kmRecorridos);
    }

    // ========================
    // 🔹 REPORTES
    // ========================

    @GetMapping("/uso-cuenta/{idCuenta}")
    public UsoDTO usoPorCuentaDTO(
            @PathVariable Long idCuenta,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {

        return service.usoPorCuenta(idCuenta, desde, hasta);
    }

    @GetMapping("/uso-usuario/{idUsuario}")
    public UsoDTO usoPorUsuarioDTO(
            @PathVariable Long idUsuario,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {

        return service.usoPorUsuario(idUsuario, desde, hasta);
    }

    @GetMapping("/reporte/monopatines-mas-viajes")
    public List<MonopatinViajesCountDTO> monopatinesMasViajes(@RequestParam int anio,
                                                              @RequestParam long minViajes,
                                                              @RequestParam Long usuarioAdminId) {

        return service.monopatinesConMasDeXViajes(anio, minViajes, usuarioAdminId);
    }

    @GetMapping("/uso-usuarios-por-tipo")
    public List<UsoUsuarioDTO> rankingUsuariosPorTipo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            @RequestParam String tipoUsuario,
            @RequestParam Long usuarioAdminId,
            @RequestParam(defaultValue = "0") int limite) {

        return service.usuariosMasActivosPorTipo(desde, hasta, tipoUsuario, usuarioAdminId, limite);
    }
}
