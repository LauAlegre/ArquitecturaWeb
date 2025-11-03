package com.monopatines.viajes.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "viaje")
@Data
@NoArgsConstructor
@AllArgsConstructor
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

    // IDs externos a otros microservicios (sin constraints locales)
    @Column(name = "id_cuenta")
    private Long cuentaId;

    @Column(name = "id_monopatin")
    private Long monopatinId;

    // Relación interna del microservicio (lado inverso)
    @OneToMany(mappedBy = "viaje")
    private List<PausaModel> pausas = new ArrayList<>();

    // Getters y Setters explícitos
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }
    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }
    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    public BigDecimal getKmRecorridos() {
        return kmRecorridos;
    }
    public void setKmRecorridos(BigDecimal kmRecorridos) {
        this.kmRecorridos = kmRecorridos;
    }

    public Long getCuentaId() {
        return cuentaId;
    }
    public void setCuentaId(Long cuentaId) {
        this.cuentaId = cuentaId;
    }

    public Long getMonopatinId() {
        return monopatinId;
    }
    public void setMonopatinId(Long monopatinId) {
        this.monopatinId = monopatinId;
    }

    public List<PausaModel> getPausas() {
        return pausas;
    }
    public void setPausas(List<PausaModel> pausas) {
        this.pausas = pausas;
    }
}
