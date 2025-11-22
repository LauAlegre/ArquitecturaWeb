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
@Getter
@Setter
@NoArgsConstructor
@ToString
public class ViajeModel {

    @Id
    private String id; // Mongo usa String u ObjectId

    @Field("fecha_inicio")
    private LocalDateTime fechaInicio;

    @Field("fecha_fin")
    private LocalDateTime fechaFin;

    @Field("km_recorridos")
    private Double kmRecorridos;

    @Field("id_cuenta")
    private Long cuentaId;

    @Field("id_monopatin")
    private Long monopatinId;

    @Field("id_usuario")
    private Long usuarioId;

    // 🔥 PAUSAS EMBEBIDAS
    @Field("pausas")
    private List<PausaModel> pausas = new ArrayList<>();
}
