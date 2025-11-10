package com.monopatines.viajes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsoDTO {
    private Long idCuenta;
    private Double kmTotales;
    private Double tiempoTotal;
    private Integer cantidadViajes;

    // Getters y setters explícitos (por si el analizador no procesa Lombok)
    public Long getIdCuenta() { return idCuenta; }
    public void setIdCuenta(Long idCuenta) { this.idCuenta = idCuenta; }
    public Double getKmTotales() { return kmTotales; }
    public void setKmTotales(Double kmTotales) { this.kmTotales = kmTotales; }
    public Double getTiempoTotal() { return tiempoTotal; }
    public void setTiempoTotal(Double tiempoTotal) { this.tiempoTotal = tiempoTotal; }
    public Integer getCantidadViajes() { return cantidadViajes; }
    public void setCantidadViajes(Integer cantidadViajes) { this.cantidadViajes = cantidadViajes; }
}
