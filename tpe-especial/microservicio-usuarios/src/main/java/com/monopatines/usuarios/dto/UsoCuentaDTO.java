package com.monopatines.usuarios.dto;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsoCuentaDTO {
    private Long idCuenta;
    private Double kmTotales;
    private Double tiempoTotal;
    private Integer cantidadViajes;
}
