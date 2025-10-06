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

    // Getters y Setters
    public int getId_carrera() { return id_carrera; }
    public void setId_carrera(int id_carrera) { this.id_carrera = id_carrera; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getDuracion_anios() { return duracion_anios; }
    public void setDuracion_anios(int duracion_anios) { this.duracion_anios = duracion_anios; }

    public List<Inscripcion> getInscripciones() { return inscripciones; }
    public void setInscripciones(List<Inscripcion> inscripciones) { this.inscripciones = inscripciones; }
}
