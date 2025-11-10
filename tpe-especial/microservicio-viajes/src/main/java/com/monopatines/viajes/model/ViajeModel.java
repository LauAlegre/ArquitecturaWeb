package com.monopatines.viajes.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "viaje")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "pausas") // evita recursión con PausaModel
public class ViajeModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_inicio")
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDateTime fechaFin;

    @Column(name = "km_recorridos")
    private BigDecimal kmRecorridos;

    @Column(name = "id_cuenta")
    private Long cuentaId;

    @Column(name = "id_monopatin")
    private Long monopatinId;

    @Column(name = "id_usuario")
    private Long usuarioId;

    @OneToMany(mappedBy = "viaje")
    private List<PausaModel> pausas = new ArrayList<>();
}