package org.entity;


import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;


@Entity
public class Inscripcion {
    @EmbeddedId
    private InscripcionId id;

    @Column(name = "fecha_inscripcion")
    private int fechaInscripcion;



    @Column(name = "fecha_graduado")
    private int fechaGraduado;

    @Column(name = "antiguedad")
    private int antiguedad;



    @ManyToOne (fetch = FetchType.LAZY)
    @MapsId("idEstudiante")

    private Estudiante estudiante;


    @ManyToOne (fetch = FetchType.LAZY)
    @MapsId("idCarrera")

    private Carrera carrera;

    // Getters y setters


    public InscripcionId getId() { return id; }
    public void setId(InscripcionId id) { this.id = id; }

    public int getFechaInscripcion() { return fechaInscripcion; }
    public void setFechaInscripcion(int fechaInscripcion) { this.fechaInscripcion = fechaInscripcion; }

    public int getFechaGraduado() { return fechaGraduado; }
    public void setFechaGraduado(int fechaGraduado) { this.fechaGraduado = fechaGraduado; }

    public Estudiante getEstudiante() { return estudiante; }
    public void setEstudiante(Estudiante estudiante) { this.estudiante = estudiante; }

    public Carrera getCarrera() { return carrera; }
    public void setCarrera(Carrera carrera) { this.carrera = carrera; }
    public int getAntiguedad() { return antiguedad; }
    public void setAntiguedad(int antiguedad) { this.antiguedad = antiguedad; }
}
