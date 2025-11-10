package com.monopatines.viajes.controller;

import com.monopatines.viajes.dto.UsoDTO;
import com.monopatines.viajes.dto.ViajeDTO;
import com.monopatines.viajes.repository.ViajeRepository;
import com.monopatines.viajes.service.ViajeService;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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

    @PostMapping
    public ViajeDTO crear(@RequestBody ViajeDTO dto) {
        return service.crear(dto);
    }

    @PutMapping("/{id}")
    public ViajeDTO actualizar(@PathVariable Long id, @RequestBody ViajeDTO dto) {
        return service.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }

    // Cerrar viaje (fecha fin ahora, km como parámetro)
    @PutMapping("/{id}/cerrar")
    public ViajeDTO cerrarViaje(@PathVariable("id") Long viajeId,
                                 @RequestParam("kmRecorridos") BigDecimal kmRecorridos) {
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
                                                                           @RequestParam long minViajes) {
        return service.monopatinesConMasDeXViajes(anio, minViajes);
    }

    @GetMapping("/uso-usuarios")
    public List<ViajeRepository.UsoUsuario> rankingUsuarios(@RequestParam LocalDate desde,
                                                            @RequestParam LocalDate hasta,
                                                            @RequestParam(defaultValue = "0") int limite) {
        return service.usuariosMasActivos(desde, hasta, limite);
    }

    @GetMapping("/uso-cuenta/{idCuenta}")
    public UsoDTO usoPorCuentaDTO(@PathVariable Long idCuenta,
                                  @RequestParam
                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        return service.usoPorCuenta(idCuenta, desde, hasta);
    }

    @GetMapping("/uso-usuario/{idUsuario}")
    public UsoDTO usoPorUsuarioDTO(@PathVariable Long idUsuario,
                                   @RequestParam
                                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        return service.usoPorUsuario(idUsuario, desde, hasta);
    }
}
