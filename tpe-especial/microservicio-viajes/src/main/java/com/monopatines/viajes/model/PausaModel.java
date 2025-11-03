package com.monopatines.viajes.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PausaModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalTime fecha_inicio;
    private LocalTime fecha_fin;
    private int duracion_minutos;

    @ManyToOne
    @JoinColumn(name = "viaje_id", nullable = false)
    private ViajeModel viaje;
}