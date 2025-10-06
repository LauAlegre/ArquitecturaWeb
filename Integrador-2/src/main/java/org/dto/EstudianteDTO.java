package org.dto;

public class EstudianteDTO {
    private int id;
    private String nombre;
    private String apellido;
    private String ciudadResidencia;
    private String genero;
    private int nroLibretaUniversitaria;


    public EstudianteDTO(int id, String nombre, String apellido, String ciudadResidencia, String genero, int nroLibretaUniversitaria) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.ciudadResidencia = ciudadResidencia;
        this.genero = genero;
        this.nroLibretaUniversitaria = nroLibretaUniversitaria;
    }

    public String getCiudadResidencia() {
        return ciudadResidencia;
    }

    public void setCiudadResidencia(String ciudadResidencia) {
        this.ciudadResidencia = ciudadResidencia;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public int getNroLibretaUniversitaria() {
        return nroLibretaUniversitaria;
    }
    public void setNroLibretaUniversitaria(int nroLibretaUniversitaria) {
        this.nroLibretaUniversitaria = nroLibretaUniversitaria;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    @Override
    public String toString() {
        return String.format(
                "📘 [Libreta: %d] %s (%s) - DNI: %d - Ciudad: %s",
                nroLibretaUniversitaria,
                nombre,
                genero.equalsIgnoreCase("F") ? "Femenino" :
                        genero.equalsIgnoreCase("M") ? "Masculino" : genero,
                nroLibretaUniversitaria,
                ciudadResidencia
        );
    }
}
