package example.org.microserviciofacturacion.controller;

import example.org.microserviciofacturacion.dto.TarifaDto;
import example.org.microserviciofacturacion.model.Tarifa;
import example.org.microserviciofacturacion.service.TarifaService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/tarifas")
public class TarifaController {

    private final TarifaService tarifaService;

    public TarifaController(TarifaService tarifaService) {
        this.tarifaService = tarifaService;
    }

    /// GET /tarifas/vigente?fecha=2025-06-01
    // Devuelve la tarifa vigente en una fecha determinada (si no se pasa fecha, usa la actual)
    @GetMapping("/vigente")
    public Tarifa getVigente(@RequestParam(required = false)
                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return tarifaService.vigente(fecha);
    }

    // POST /tarifas/ajuste
    // Crea una nueva tarifa con fecha de inicio de vigencia
    @PostMapping("/ajuste")
    public Tarifa crearAjuste(@RequestBody TarifaDto dto) {
        return tarifaService.crearAjuste(dto);
    }

    // GET /tarifas/historial
    // Devuelve el historial completo de tarifas ordenadas de la más reciente a la más antigua
    @GetMapping("/historial")
    public List<Tarifa> historial() {
        return tarifaService.historial();
    }
    @DeleteMapping("/{id}")
    public void eliminarTarifa(@PathVariable Long id) {
        tarifaService.eliminarTarifa(id);
    }
}
