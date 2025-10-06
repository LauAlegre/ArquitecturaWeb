package org.dto;

public class CarreraDTO {
    private int id_carrera;
    private String nombre;
    private long cantidadInscriptos;

    public CarreraDTO(int id_carrera, String nombre, long cantidadInscriptos) {
        this.id_carrera = id_carrera;
        this.nombre = nombre;
        this.cantidadInscriptos = cantidadInscriptos;
    }

    public int getId_carrera() {
        return id_carrera;
    }

    public void setId_carrera(int id_carrera) {
        this.id_carrera = id_carrera;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public long getCantidadInscriptos() {
        return cantidadInscriptos;
    }

    public void setCantidadInscriptos(long cantidadInscriptos) {
        this.cantidadInscriptos = cantidadInscriptos;
    }


    @Override
    public String toString() {
        return String.format(
                """
                🎓 Carrera: %s
                   • ID: %d
                   • Inscriptos: %d
                """,
                nombre,
                id_carrera,
                cantidadInscriptos
        );
    }


}
