package example.org.microserviciofacturacion.service;

import example.org.microserviciofacturacion.dto.FacturaDto;
import example.org.microserviciofacturacion.mapper.Facturamapper;
import example.org.microserviciofacturacion.mapper.Facturamapper;
import example.org.microserviciofacturacion.model.Factura;
import example.org.microserviciofacturacion.model.Tarifa;
import example.org.microserviciofacturacion.repository.FacturaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class FacturaServiceImpl implements FacturaService {

    private final FacturaRepository facturaRepository;
    private final TarifaService tarifaService;
    private final Facturamapper facturaMapper;

    public FacturaServiceImpl(FacturaRepository facturaRepository,
                              TarifaService tarifaService,
                              Facturamapper facturaMapper) {
        this.facturaRepository = facturaRepository;
        this.tarifaService = tarifaService;
        this.facturaMapper = facturaMapper;
    }

    @Transactional
    @Override
    public Factura generarFactura(FacturaDto dto) {

        if (dto.getIdCuenta() == null || dto.getIdViaje() == null)
            throw new IllegalArgumentException("idCuenta e idViaje son obligatorios");
        if (dto.getMinutos() <= 0)
            throw new IllegalArgumentException("minutos debe ser mayor a 0");

        // Fecha de emisión
        LocalDate fecha = (dto.getFechaEmision() == null) ? LocalDate.now() : dto.getFechaEmision();

        // Tarifa vigente a esa fecha
        Tarifa tarifa = tarifaService.vigente(fecha);

        // Cálculo del monto
        double monto = dto.getMinutos() * tarifa.getPrecioMinuto();
        if (dto.isPausaExtensa()) {
            monto += tarifa.getPrecioExtraPausa();
        }

        // Base desde mapper
        Factura f = Facturamapper.toEntity(dto);

        // Campos calculados/derivados
        f.setFechaEmision(fecha);   // si tu entidad tiene setFecha_emision, cambialo
        f.setMontoTotal(monto);     // si tiene setMonto_total, cambialo

        return facturaRepository.save(f);
    }

    @Override
    public Double totalFacturado(int anio, int mesInicio, int mesFin) {
        LocalDate inicio = LocalDate.of(anio, mesInicio, 1);
        LocalDate fin = LocalDate.of(anio, mesFin, LocalDate.of(anio, mesFin, 1).lengthOfMonth());
        return facturaRepository.calcularTotalFacturado(inicio, fin);
    }

    @Override
    public Factura obtenerFactura(Long id) {
        return facturaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Factura no encontrada: " + id));
    }
}
