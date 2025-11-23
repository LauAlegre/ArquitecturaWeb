package org.example.microservicioviajes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MinutosPausaMonopatinDTO {

    private Long monopatinId;
    private Long totalMinutosPausa;

}
