package org.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarreraReporteDTO {
    private String nombreCarrera;
    private int anio;
    private long inscriptos;
    private long egresados;
}
