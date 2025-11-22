package org.example.microservicioviajes.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "viajes")
@Getter @Setter @NoArgsConstructor @ToString
public class ViajeModel {

    @Id
    private String id;

    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private Double kmRecorridos;
    private Long cuentaId;
    private Long monopatinId;
    private Long usuarioId;

    private List<PausaModel> pausas = new ArrayList<>();
}
