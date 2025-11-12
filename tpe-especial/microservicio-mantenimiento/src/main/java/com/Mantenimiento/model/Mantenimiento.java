package com.Mantenimiento.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "mantenimientos")
public class Mantenimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime inicio;

    private LocalDateTime fin;

    @Column(nullable = false)
    private Long monopatinId;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private String responsable;

    @Column(nullable = false)
    private Double costo;

    // 👉 NUEVO: para que compile set/getObservaciones()
    @Column
    private String observaciones;

    public Mantenimiento() {}

    // getters/setters (o poné @Data de Lombok si querés)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getInicio() { return inicio; }
    public void setInicio(LocalDateTime inicio) { this.inicio = inicio; }

    public LocalDateTime getFin() { return fin; }
    public void setFin(LocalDateTime fin) { this.fin = fin; }

    public Long getMonopatinId() { return monopatinId; }
    public void setMonopatinId(Long monopatinId) { this.monopatinId = monopatinId; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getResponsable() { return responsable; }
    public void setResponsable(String responsable) { this.responsable = responsable; }

    public Double getCosto() { return costo; }
    public void setCosto(Double costo) { this.costo = costo; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}

