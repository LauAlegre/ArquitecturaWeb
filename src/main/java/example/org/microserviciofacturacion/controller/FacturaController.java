package example.org.microserviciofacturacion.controller;

import example.org.microserviciofacturacion.dto.FacturaDto;
import example.org.microserviciofacturacion.model.Factura;
import example.org.microserviciofacturacion.service.FacturaService;
import org.springframework.web.bind.annotation.*;

@RestController // Indica que esta clase maneja peticiones HTTP (API REST)
@RequestMapping("/facturas") // para los endpoints
public class FacturaController {

    private final FacturaService facturaService;

    // Inyección del servicio
    public FacturaController(FacturaService facturaService) {
        this.facturaService = facturaService;
    }


    // Endpoint POST → /facturas/generar
    // Recibe un JSON con datos del viaje y genera una factura
    @PostMapping("/generar")
    public Factura generar(@RequestBody FacturaDto req) {
        return facturaService.generarFactura(req);
    }



    // Endpoint GET → /facturas/{id}
    // Devuelve una factura específica por su ID
    @GetMapping("/{id}")
    public Factura getById(@PathVariable Long id) {
        return facturaService.obtenerFactura(id);
    }


    // Endpoint GET → /facturas/total? Anio=2025&mesInicio=1&mesFin=6
    // Calcula el total facturado entre los meses indicados
    @GetMapping("/total")
    public Double total(@RequestParam int anio,
                        @RequestParam int mesInicio,
                        @RequestParam int mesFin) {
        return facturaService.totalFacturado(anio, mesInicio, mesFin);
    }
}
