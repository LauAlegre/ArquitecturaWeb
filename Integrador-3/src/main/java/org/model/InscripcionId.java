
package org.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class InscripcionId implements Serializable {
    private Long idEstudiante;
    private Long idCarrera;


}


