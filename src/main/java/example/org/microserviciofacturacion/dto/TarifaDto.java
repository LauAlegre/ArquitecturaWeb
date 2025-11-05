package example.org.microserviciofacturacion.dto;

import java.time.LocalDate;

public class TarifaDto {
    private double precioMinuto;
    private double precioExtraPausa;
    private LocalDate fechaInicioVigencia;
    private LocalDate fechaFinVigencia; // opcional (puede ser null)

    public double getPrecioMinuto() { return precioMinuto; }
    public void setPrecioMinuto(double precioMinuto) { this.precioMinuto = precioMinuto; }

    public double getPrecioExtraPausa() { return precioExtraPausa; }
    public void setPrecioExtraPausa(double precioExtraPausa) { this.precioExtraPausa = precioExtraPausa; }

    public LocalDate getFechaInicioVigencia() { return fechaInicioVigencia; }
    public void setFechaInicioVigencia(LocalDate fechaInicioVigencia) { this.fechaInicioVigencia = fechaInicioVigencia; }

    public LocalDate getFechaFinVigencia() { return fechaFinVigencia; }
    public void setFechaFinVigencia(LocalDate fechaFinVigencia) { this.fechaFinVigencia = fechaFinVigencia; }
}
