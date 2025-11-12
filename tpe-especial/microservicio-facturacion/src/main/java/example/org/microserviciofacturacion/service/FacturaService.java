package example.org.microserviciofacturacion.service;

import example.org.microserviciofacturacion.dto.DatosDeFacturacionDto;
import example.org.microserviciofacturacion.dto.FacturaDto;
import example.org.microserviciofacturacion.model.Factura;

public interface FacturaService {

    Factura generarFactura(DatosDeFacturacionDto datos);  // calcula con Tarifa vigente
    Double totalFacturado(int anio,int mesInicio,int mesFin);
    Factura obtenerFactura(Long id);
    Iterable<Factura> obtenerTodas();
}
