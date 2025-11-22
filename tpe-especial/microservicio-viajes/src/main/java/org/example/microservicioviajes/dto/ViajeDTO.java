package org.example.microservicioviajes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViajeDTO {

    private String id;   // 🔥 CAMBIADO DE Long → String

    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private Double kmRecorridos;
    private Long cuentaId;
    private Long monopatinId;
    private Long usuarioId;
}
