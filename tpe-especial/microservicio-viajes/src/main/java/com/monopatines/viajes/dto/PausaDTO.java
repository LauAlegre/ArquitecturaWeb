package com.monopatines.viajes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PausaDTO {
    private Long idPausa;
    private LocalTime fecha_inicio;
    private LocalTime fecha_fin;
    private int duracion_minutos;
    private int id_viaje;

    // Getters y Setters explícitos
    public int getIdPausa() {
        return idPausa;
    }
    public void setIdPausa(int idPausa) {
        this.idPausa = idPausa;
    }

    public LocalTime getFecha_inicio() {
        return fecha_inicio;
    }
    public void setFecha_inicio(LocalTime fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public LocalTime getFecha_fin() {
        return fecha_fin;
    }
    public void setFecha_fin(LocalTime fecha_fin) {
        this.fecha_fin = fecha_fin;
    }

    public int getDuracion_minutos() {
        return duracion_minutos;
    }
    public void setDuracion_minutos(int duracion_minutos) {
        this.duracion_minutos = duracion_minutos;
    }

    public int getId_viaje() {
        return id_viaje;
    }
    public void setId_viaje(int id_viaje) {
        this.id_viaje = id_viaje;
    }
}