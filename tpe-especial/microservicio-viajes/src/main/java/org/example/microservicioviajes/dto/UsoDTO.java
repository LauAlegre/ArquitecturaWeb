package org.example.microservicioviajes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsoDTO {

    private Long idCuenta;   // 🔥 CORREGIDO: ERA String

    private Double kmTotales;
    private Double tiempoTotal;
    private Integer cantidadViajes;
}
