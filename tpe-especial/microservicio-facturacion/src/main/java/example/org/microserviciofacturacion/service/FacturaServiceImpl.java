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

        // 🔹 DEBUG 1: Mostrar datos que llegan
        System.out.println("📩 [FACTURACIÓN] Recibido DTO: " + dto);

        // 1️⃣ Validaciones básicas
        if (dto.getIdCuenta() == null || dto.getIdViaje() == null)
            throw new IllegalArgumentException("idCuenta e idViaje son obligatorios");
        if (dto.getMinutosViaje() <= 0)
            throw new IllegalArgumentException("Los minutos de viaje deben ser mayores a 0");

        // 2️⃣ Fecha de emisión
        LocalDate fecha = (dto.getFechaEmision() == null) ? LocalDate.now() : dto.getFechaEmision();
        System.out.println("📅 Fecha de emisión: " + fecha);

        // 3️⃣ Tarifa vigente según la fecha
        Tarifa tarifa = tarifaService.vigente(fecha);
        System.out.println("💰 Tarifa vigente -> precioMinuto=" + tarifa.getPrecioMinuto()
                + ", precioExtraPausa=" + tarifa.getPrecioExtraPausa());

        // 4️⃣ Cálculo del monto base
        double monto = dto.getMinutosViaje() * tarifa.getPrecioMinuto();
        System.out.println("🧾 Minutos viaje: " + dto.getMinutosViaje() + " -> monto base: $" + monto);

        // 5️⃣ Evaluar pausas (cortas o extensas)
        if (dto.getMinutosPausa() > 0) {
            if (dto.getMinutosPausa() > 15) {
                System.out.println("⏸ Pausa extensa (" + dto.getMinutosPausa() + " min) -> se suma tarifa extra");
                monto += tarifa.getPrecioExtraPausa();
            } else {
                System.out.println("⏸ Pausa corta (" + dto.getMinutosPausa() + " min) -> se suma al precio normal");
                monto += dto.getMinutosPausa() * tarifa.getPrecioMinuto();
            }
        }

        System.out.println("💵 Monto final calculado: $" + monto);

        // 6️⃣ Crear la entidad Factura
        Factura factura = new Factura();
        factura.setIdCuenta(dto.getIdCuenta());
        factura.setIdViaje(dto.getIdViaje());
        factura.setFechaEmision(fecha);
        factura.setMontoTotal(monto);

        // 7️⃣ Guardar en BD
        Factura guardada = facturaRepository.save(factura);
        System.out.println("✅ Factura guardada con ID: " + guardada.getIdFactura());

        // 8️⃣ Comunicar al microservicio Usuarios para debitar el saldo
        try {
            System.out.println("🔁 Enviando solicitud a microservicio USUARIOS para debitar $" + monto
                    + " de la cuenta ID " + dto.getIdCuenta());
            cuentaClient.debitarSaldo(dto.getIdCuenta(), monto);
            System.out.println("✅ Débito realizado correctamente");
        } catch (Exception e) {
            System.err.println("🚨 ERROR al debitar saldo: " + e.getMessage());
            e.printStackTrace();
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
