package example.org.microserviciofacturacion.dto;

import java.time.LocalDate;

public class FacturaDto {
    private Long idCuenta;
    private Long idViaje;
    private LocalDate fechaEmision;  // opcional; si es null se usa hoy
    private int minutos;             // minutos del viaje
    private boolean pausaExtensa;    // > 15 min

    // Getters & Setters
    public Long getIdCuenta() { return idCuenta; }
    public void setIdCuenta(Long idCuenta) { this.idCuenta = idCuenta; }

    public Long getIdViaje() { return idViaje; }
    public void setIdViaje(Long idViaje) { this.idViaje = idViaje; }

    public LocalDate getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(LocalDate fechaEmision) { this.fechaEmision = fechaEmision; }

    public int getMinutos() { return minutos; }
    public void setMinutos(int minutos) { this.minutos = minutos; }

    public boolean isPausaExtensa() { return pausaExtensa; }
    public void setPausaExtensa(boolean pausaExtensa) { this.pausaExtensa = pausaExtensa; }
}
