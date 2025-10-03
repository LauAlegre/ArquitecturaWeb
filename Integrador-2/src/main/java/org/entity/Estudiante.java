package org.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
@Entity
public class Estudiante {
    @Id
    private int numeroDeLibreta;

    @Column(name = "nombre", length = 50, nullable = false)
    private String nombre;

    @Column(name = "fecha_nacimiento")
    private Date fechaNacimiento;

    @Column(name = "genero", length = 1)
    private String genero;

    @Column(name = "nro_documento")
    private Integer nroDocumento;

    @Column(name = "ciudad_residencia", length = 50)
    private String ciudadResidencia;

    @OneToMany(mappedBy = "estudiante", fetch = FetchType.LAZY)

    private List<Inscripcion> inscripciones;


    // Getters y setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Date getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(Date fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public Integer getNroDocumento() { return nroDocumento; }
    public void setNroDocumento(Integer nroDocumento) { this.nroDocumento = nroDocumento; }

    public String getCiudadResidencia() { return ciudadResidencia; }
    public void setCiudadResidencia(String ciudadResidencia) { this.ciudadResidencia = ciudadResidencia; }

}
