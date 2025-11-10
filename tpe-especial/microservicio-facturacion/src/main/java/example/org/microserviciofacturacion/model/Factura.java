// example.org.microserviciofacturacion.model.Factura
package example.org.microserviciofacturacion.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "factura")
public class Factura {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFactura;

    private Long idCuenta;
    private Long idViaje;

    @Column(name = "fecha_emision")   // columna en la BD
    private LocalDate fechaEmision;   // nombre Java camelCase

    @Column(name = "monto_total", nullable = false)
    private double montoTotal;

    // === getters & setters ===
    public Long getIdFactura() { return idFactura; }
    public void setIdFactura(Long idFactura) { this.idFactura = idFactura; }

    public Long getIdCuenta() { return idCuenta; }
    public void setIdCuenta(Long idCuenta) { this.idCuenta = idCuenta; }

    public Long getIdViaje() { return idViaje; }
    public void setIdViaje(Long idViaje) { this.idViaje = idViaje; }

    public LocalDate getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(LocalDate fechaEmision) { this.fechaEmision = fechaEmision; }

    public double getMontoTotal() { return montoTotal; }
    public void setMontoTotal(double montoTotal) { this.montoTotal = montoTotal; }
}
