package example.org.microserviciofacturacion.service;

import example.org.microserviciofacturacion.client.CuentaClient;
import example.org.microserviciofacturacion.dto.DatosDeFacturacionDto;
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
    private final CuentaClient cuentaClient = new CuentaClient();

    public FacturaServiceImpl(FacturaRepository facturaRepository,
                              TarifaService tarifaService,
                              Facturamapper facturaMapper) {
        this.facturaRepository = facturaRepository;
        this.tarifaService = tarifaService;
        this.facturaMapper = facturaMapper;
    }

    @Override
    @Transactional
    public Factura generarFactura(DatosDeFacturacionDto dto) {

        // 1️⃣ Validaciones básicas
        if (dto.getIdCuenta() == null || dto.getIdViaje() == null)
            throw new IllegalArgumentException("idCuenta e idViaje son obligatorios");
        if (dto.getMinutosViaje() <= 0)
            throw new IllegalArgumentException("Los minutos de viaje deben ser mayores a 0");

        // 2️⃣ Fecha de emisión
        LocalDate fecha = (dto.getFechaEmision() == null) ? LocalDate.now() : dto.getFechaEmision();

        // 3️⃣ Tarifa vigente según la fecha
        Tarifa tarifa = tarifaService.vigente(fecha);

        // 4️⃣ Cálculo del monto base
        double monto = dto.getMinutosViaje() * tarifa.getPrecioMinuto();

        // 5️⃣ Evaluar pausas (cortas o extensas)
        if (dto.getMinutosPausa() > 0) {
            if (dto.getMinutosPausa() > 15) {
                // 🕒 Pausa extensa → tarifa extra definida por admin
                monto += tarifa.getPrecioExtraPausa();
            } else {
                // ⏸ Pausa corta → se cobra al precio normal
                monto += dto.getMinutosPausa() * tarifa.getPrecioMinuto();
            }
        }

        // 6️⃣ Crear la entidad Factura
        Factura factura = new Factura();
        factura.setIdCuenta(dto.getIdCuenta());
        factura.setIdViaje(dto.getIdViaje());
        factura.setFechaEmision(fecha);
        factura.setMontoTotal(monto);

        // 7️⃣ Guardar en BD
        Factura guardada = facturaRepository.save(factura);

        // 8️⃣ Comunicar al microservicio Usuarios para debitar el saldo
        try {
            cuentaClient.debitarSaldo(dto.getIdCuenta(), monto);
        } catch (Exception e) {
            // Si falla el débito, revertimos la creación de la factura
            throw new RuntimeException("Error al debitar saldo en el microservicio de usuarios: " + e.getMessage());
        }

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
