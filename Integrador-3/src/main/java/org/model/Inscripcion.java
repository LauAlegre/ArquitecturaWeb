package org.model;
import jakarta.persistence.*;
import lombok.Data;


@Data
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





}
