package org.model;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class Estudiante {
    @Id
    private int nroDocumento;

    @Column(name = "nombre", length = 50, nullable = false)
    private String nombre;

    @Column(name = "genero", length = 20)
    private String genero;

    @Column(name = "nro_libreta_universitaria", unique = true, nullable = false)
    private int numeroDeLibreta;


    @Column(name = "ciudad_residencia", length = 50)
    private String ciudadResidencia;

    @OneToMany(mappedBy = "estudiante", fetch = FetchType.LAZY)

    private List<Inscripcion> inscripciones;


    // Getters y setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }


    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public Integer getNroDocumento() { return nroDocumento; }
    public void setNroDocumento(Integer nroDocumento) { this.nroDocumento = nroDocumento; }

    public String getCiudadResidencia() { return ciudadResidencia; }
    public void setCiudadResidencia(String ciudadResidencia) { this.ciudadResidencia = ciudadResidencia; }
    public int getNumeroDeLibreta() { return numeroDeLibreta; }

    public void setNroLibretaUniversitaria(int numeroDeLibreta) { this.numeroDeLibreta = numeroDeLibreta; }
    public int getNroLibretaUniversitaria() { return this.numeroDeLibreta; }

}
