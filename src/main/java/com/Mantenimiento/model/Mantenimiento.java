package com.Mantenimiento.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Mantenimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long monopatinId;
    private LocalDate inicio;
    private LocalDate fin;
    private String descripcion;
    private String responsable;
    private Double costo;
    private String observaciones;

    public boolean activo(){ return fin == null; }
}
