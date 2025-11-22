package example.org.microserviciofacturacion.service;

import example.org.microserviciofacturacion.client.CuentaClient;
import example.org.microserviciofacturacion.dto.DatosDeFacturacionDto;
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
    private final CuentaClient cuentaClient; // ✔ ahora se inyecta

    public FacturaServiceImpl(FacturaRepository facturaRepository,
                              TarifaService tarifaService,
                              Facturamapper facturaMapper,
                              CuentaClient cuentaClient) {  // ✔ inyectado por constructor
        this.facturaRepository = facturaRepository;
        this.tarifaService = tarifaService;
        this.facturaMapper = facturaMapper;
        this.cuentaClient = cuentaClient;
    }


    @Override
    @Transactional
    public Factura generarFactura(DatosDeFacturacionDto dto) {

        // 🔹 Validaciones
        if (dto.getIdCuenta() == null || dto.getIdViaje() == null)
            throw new IllegalArgumentException("idCuenta e idViaje son obligatorios");
        if (dto.getMinutosViaje() <= 0)
            throw new IllegalArgumentException("Los minutos de viaje deben ser mayores a 0");

        // Fecha
        LocalDate fecha = (dto.getFechaEmision() == null)
                ? LocalDate.now()
                : dto.getFechaEmision();

        // Tarifa vigente
        Tarifa tarifa = tarifaService.vigente(fecha);

        // Calcular monto
        double monto = dto.getMinutosViaje() * tarifa.getPrecioMinuto();

        if (dto.getMinutosPausa() > 0) {
            if (dto.getMinutosPausa() > 15) {
                monto += tarifa.getPrecioExtraPausa();
            } else {
                monto += dto.getMinutosPausa() * tarifa.getPrecioMinuto();
            }
        }

        // Crear factura
        Factura factura = new Factura();
        factura.setIdCuenta(dto.getIdCuenta());
        factura.setIdViaje(dto.getIdViaje());   // ← ahora es STRING
        factura.setFechaEmision(fecha);
        factura.setMontoTotal(monto);

        // Guardar
        Factura guardada = facturaRepository.save(factura);

        // Llamar a microservicio usuarios para debitar
        cuentaClient.debitarSaldo(dto.getIdCuenta(), monto);

        return guardada;
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
    @Override
    public Iterable<Factura> obtenerTodas() {
        return facturaRepository.findAll();
    }

}
