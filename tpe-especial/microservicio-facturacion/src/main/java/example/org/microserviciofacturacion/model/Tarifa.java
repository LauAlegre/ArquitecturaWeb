package example.org.microserviciofacturacion.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;

@Entity
public class Tarifa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTarifa;

    private double precioMinuto;
    private double precioExtraPausa;
    private LocalDate fechaInicioVigencia;
    private LocalDate fechaFinVigencia; // puede ser null si sigue vigente

    ///CONSTRUCTORES ///
    public Tarifa() {
    }

    public Tarifa(Long idTarifa, double precioMinuto, double precioExtraPausa, LocalDate fechaInicioVigencia, LocalDate fechaFinVigencia) {
        this.idTarifa = idTarifa;
        this.precioMinuto = precioMinuto;
        this.precioExtraPausa = precioExtraPausa;
        this.fechaInicioVigencia = fechaInicioVigencia;
        this.fechaFinVigencia = fechaFinVigencia;
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /// GETTERS Y SETTERS ///

    public Long getIdTarifa() {
        return idTarifa;
    }

    public double getPrecioMinuto() {
        return precioMinuto;
    }

    public double getPrecioExtraPausa() {
        return precioExtraPausa;
    }

    public LocalDate getFechaInicioVigencia() {
        return fechaInicioVigencia;
    }

    public LocalDate getFechaFinVigencia() {
        return fechaFinVigencia;
    }

    public void setIdTarifa(Long idTarifa) {
        this.idTarifa = idTarifa;
    }

    public void setPrecioMinuto(double precioMinuto) {
        this.precioMinuto = precioMinuto;
    }

    public void setPrecioExtraPausa(double precioExtraPausa) {
        this.precioExtraPausa = precioExtraPausa;
    }

    public void setFechaInicioVigencia(LocalDate fechaInicioVigencia) {
        this.fechaInicioVigencia = fechaInicioVigencia;
    }

    public void setFechaFinVigencia(LocalDate fechaFinVigencia) {
        this.fechaFinVigencia = fechaFinVigencia;
    }

    /// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



}