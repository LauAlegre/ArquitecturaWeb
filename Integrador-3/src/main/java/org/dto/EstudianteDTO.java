package org.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstudianteDTO {
    private Long nroDocumento;
    private String nombre;
    private String apellido;
    private String genero;
    private long numeroDeLibreta;
    private String ciudadResidencia;
}

