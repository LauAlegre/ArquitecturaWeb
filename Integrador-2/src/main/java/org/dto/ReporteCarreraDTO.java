package org.dto;

public class ReporteCarreraDTO {
    private String nombreCarrera;
    private long inscriptos;
    private long egresados;
    private int anio;

    public ReporteCarreraDTO(String nombreCarrera, long inscriptos, long egresados, int anio) {
        this.nombreCarrera = nombreCarrera;
        this.inscriptos = inscriptos;
        this.egresados = egresados;
        this.anio = anio;
    }

    // Getters
    public String getNombreCarrera() { return nombreCarrera; }
    public long getInscriptos() { return inscriptos; }
    public long getEgresados() { return egresados; }
    public int getAnio() { return anio; }

    @Override
    public String toString() {
        return String.format("%s | Año %d | Inscriptos: %d | Egresados: %d",
                nombreCarrera, anio, inscriptos, egresados);
    }
}
