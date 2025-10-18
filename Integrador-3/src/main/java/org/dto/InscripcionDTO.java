package org.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InscripcionDTO {
    private Long idEstudiante;
    private long idCarrera;
    private int fechaInscripcion;
    private int fechaGraduado;
    private int antiguedad;
}




