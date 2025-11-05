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

    @GetMapping("/vigente")
    public Tarifa getVigente(@RequestParam(required = false)
                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return tarifaService.vigente(fecha);
    }

    @PostMapping("/ajuste")
    public Tarifa crearAjuste(@RequestBody TarifaDto dto) {
        return tarifaService.crearAjuste(dto);
    }

    @GetMapping("/historial")
    public List<Tarifa> historial() {
        return tarifaService.historial();
    }
}
