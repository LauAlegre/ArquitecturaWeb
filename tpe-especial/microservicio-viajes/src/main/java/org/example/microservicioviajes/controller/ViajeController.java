package org.example.microservicioviajes.controller;

import org.example.microservicioviajes.dto.ResumenViajeDTO;
import org.example.microservicioviajes.dto.UsoDTO;
import org.example.microservicioviajes.dto.ViajeDTO;
import org.example.microservicioviajes.repository.ViajeRepository;
import org.example.microservicioviajes.service.ViajeService;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/viajes")
public class ViajeController {

    private final ViajeService service;

    public ViajeController(ViajeService service) {
        this.service = service;
    }

    // CRUD básico
    @GetMapping
    public List<ViajeDTO> listar() {
        return service.listar(Pageable.unpaged()).getContent();
    }

    @GetMapping("/{id}")
    public ViajeDTO buscarPorId(@PathVariable Long id) {
        return service.obtenerPorId(id);
    }

    @PostMapping("/iniciar")
    public ViajeDTO iniciarViaje(@RequestBody ViajeDTO dto) {
        return service.iniciarViaje(dto);
    }

    @PutMapping("/{id}")
    public ViajeDTO actualizar(@PathVariable Long id, @RequestBody ViajeDTO dto) {
        return service.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }

    // Cerrar viaje (devuelve resumen)
    @PutMapping("/{id}/cerrar")
    public ResumenViajeDTO cerrarViaje(@PathVariable("id") Long viajeId,
            @RequestParam("kmRecorridos") Double kmRecorridos) {
        return service.cerrarViaje(viajeId, java.time.LocalDateTime.now(), kmRecorridos);
    }

    // Filtrar por monopatín (query param)
    @GetMapping(params = "monopatinId")
    public List<ViajeDTO> listarPorMonopatin(@RequestParam Long monopatinId) {
        return service.listar(Pageable.unpaged()).getContent()
                .stream()
                .filter(v -> v.getMonopatinId() != null && v.getMonopatinId().equals(monopatinId))
                .toList();
    }

    // ===== Reportes / uso =====

    @GetMapping("/reporte/monopatines-mas-viajes")
    public List<ViajeRepository.MonopatinViajesCount> monopatinesMasViajes(@RequestParam int anio,
            @RequestParam long minViajes,
            @RequestParam Long usuarioAdminId) {
        return service.monopatinesConMasDeXViajes(anio, minViajes, usuarioAdminId);
    }

    @GetMapping("/uso-cuenta/{idCuenta}")
    public UsoDTO usoPorCuentaDTO(@PathVariable Long idCuenta,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        return service.usoPorCuenta(idCuenta, desde, hasta);
    }

    @GetMapping("/uso-usuario/{idUsuario}")
    public UsoDTO usoPorUsuarioDTO(@PathVariable Long idUsuario,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        return service.usoPorUsuario(idUsuario, desde, hasta);
    }

    // Ranking de usuarios por tipo. Ahora se pasa el usuarioAdminId (debe ser
    // admin).
    // Ejemplo:
    // /uso-usuarios-por-tipo?desde=2025-01-01&hasta=2025-01-31&tipoUsuario=PREMIUM&usuarioAdminId=1&limite=10
    @GetMapping("/uso-usuarios-por-tipo")
    public List<ViajeRepository.UsoUsuario> rankingUsuariosPorTipo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            @RequestParam String tipoUsuario,
            @RequestParam Long usuarioAdminId,
            @RequestParam(defaultValue = "0") int limite) {
        return service.usuariosMasActivosPorTipo(desde, hasta, tipoUsuario, usuarioAdminId, limite);
    }
}