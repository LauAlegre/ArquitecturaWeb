package org.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Carrera {
    @Id
    private int id_carrera;
    @Column
    private String nombre;
    @Column
    private int duracion_anios;
    @OneToMany (mappedBy = "carrera", fetch = javax.persistence.FetchType.LAZY)

    private List<Inscripcion> inscripciones;
}

