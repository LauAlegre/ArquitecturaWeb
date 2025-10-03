package org.entity;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class InscripcionId implements Serializable {
    private int idEstudiante;
    private int idCarrera;

    // Getters y setters
    public int getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(Integer idEstudiante) {
        this.idEstudiante = idEstudiante;
    }
    public int getIdCarrera() {
        return idCarrera;
    }
    public void setIdCarrera(Integer idCarrera) {
        this.idCarrera = idCarrera;
    }
}

