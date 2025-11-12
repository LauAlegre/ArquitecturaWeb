package com.monopatines.usuarios.dto;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsoCuentaDTO {
    private Long idReferencia;
    private String tipoReferencia;
    private Double kmTotales;
    private Double tiempoTotal;
    private Integer cantidadViajes;
}
