package org.model;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
@Data
@Entity
public class Estudiante {
    @Id
    private Long nroDocumento;

    @Column(name = "nombre", length = 50, nullable = false)
    private String nombre;
    @Column(name = "apellido", length = 50, nullable = false)
    private String apellido;

    @Column(name = "genero", length = 20)
    private String genero;

    @Column(name = "nro_libreta_universitaria", unique = true, nullable = false)
    private long numeroDeLibreta;


    @Column(name = "ciudad_residencia", length = 50)
    private String ciudadResidencia;

    @OneToMany(mappedBy = "estudiante", fetch = FetchType.LAZY)

    private List<Inscripcion> inscripciones;




}
