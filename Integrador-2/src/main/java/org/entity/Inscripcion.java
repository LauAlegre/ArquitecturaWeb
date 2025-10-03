package org.entity;


import javax.persistence.*;
import java.util.Date;


@Entity
public class Inscripcion {
    @EmbeddedId
    private InscripcionId id;
    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_inscripcion")
    private Date fechaInscripcion;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_graduado")
    private Date fechaGraduado;


    @ManyToOne (fetch = FetchType.LAZY)
    @MapsId("idEstudiante")
    @Column(name = "id_estudiante")
    private Estudiante estudiante;


    @ManyToOne (fetch = FetchType.LAZY)
    @MapsId("idCarrera")
    @Column(name = "id_carrera")
    private Carrera carrera;

    // Getters y setters


    public InscripcionId getId() { return id; }
    public void setId(InscripcionId id) { this.id = id; }

    public Date getFechaInscripcion() { return fechaInscripcion; }
    public void setFechaInscripcion(Date fechaInscripcion) { this.fechaInscripcion = fechaInscripcion; }

    public Date getFechaGraduado() { return fechaGraduado; }
    public void setFechaGraduado(Date fechaGraduado) { this.fechaGraduado = fechaGraduado; }

    public Estudiante getEstudiante() { return estudiante; }
    public void setEstudiante(Estudiante estudiante) { this.estudiante = estudiante; }

    public Carrera getCarrera() { return carrera; }
    public void setCarrera(Carrera carrera) { this.carrera = carrera; }
}
