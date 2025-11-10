package org.example.microserviciomonopatines.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "monopatin")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Monopatin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private EstadoMonopatin estado;

    // Coordenadas GPS actuales
    private Double latitud;
    private Double longitud;

    // Datos acumulados
    @Column(name = "total_km")
    private Double totalKm;

    @Column(name = "total_tiempo_uso")
    private Long totalTiempoUso;

    // FK opcional a la parada donde se encuentra
    @Column(name = "parada_id")
    private Long paradaId;
}
