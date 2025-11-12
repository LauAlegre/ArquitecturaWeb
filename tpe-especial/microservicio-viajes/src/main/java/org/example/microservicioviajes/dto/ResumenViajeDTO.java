package org.example.microservicioviajes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResumenViajeDTO {
    private Long idViaje;
    private int minutosTotales;
    private int minutosPausas;
    private Double kmRecorridos;

    // Getters/setters explícitos por si Lombok no se procesa
    public Long getIdViaje() {
        return idViaje;
    }

    public void setIdViaje(Long idViaje) {
        this.idViaje = idViaje;
    }

    public int getMinutosTotales() {
        return minutosTotales;
    }

    public void setMinutosTotales(int minutosTotales) {
        this.minutosTotales = minutosTotales;
    }

    public int getMinutosPausas() {
        return minutosPausas;
    }

    public void setMinutosPausas(int minutosPausas) {
        this.minutosPausas = minutosPausas;
    }

    public Double getKmRecorridos() {
        return kmRecorridos;
    }

    public void setKmRecorridos(Double kmRecorridos) {
        this.kmRecorridos = kmRecorridos;
    }
}
