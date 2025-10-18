package org.model;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Carrera {
    @Id
    private Long id_carrera;
    @Column
    private String nombre;
    @Column
    private int duracion_anios;
    @OneToMany(mappedBy = "carrera", fetch = FetchType.LAZY)
    private List<Inscripcion> inscripciones;

    public void setId(Long id_carrera) {
        this.id_carrera = id_carrera;
    }


}