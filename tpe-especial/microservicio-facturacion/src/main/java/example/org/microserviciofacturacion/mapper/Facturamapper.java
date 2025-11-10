package example.org.microserviciofacturacion.mapper;

import example.org.microserviciofacturacion.dto.FacturaDto;
import example.org.microserviciofacturacion.model.Factura;
import org.springframework.stereotype.Component;

@Component
public class Facturamapper {

    // Mapea lo que llega del cliente a la entidad (sin cálculos)
    public static Factura toEntity(FacturaDto dto) {
        if (dto == null) return null;
        Factura f = new Factura();
        f.setIdCuenta(dto.getIdCuenta());       // si tu entidad usa setId_cuenta(..), cambialo aquí
        f.setIdViaje(dto.getIdViaje());         // idem setId_viaje(..)
        f.setFechaEmision(dto.getFechaEmision()); // idem setFecha_emision(..)
        return f;
    }

    // Si más adelante querés devolver DTOs
    public FacturaDto toDto(Factura f) {
        if (f == null) return null;
        FacturaDto dto = new FacturaDto();
        dto.setIdCuenta(f.getIdCuenta());
        dto.setIdViaje(f.getIdViaje());
        dto.setFechaEmision(f.getFechaEmision());
        // minutos/pausaExtensa no están en la entidad; se calculan a partir del viaje
        return dto;
    }
}
