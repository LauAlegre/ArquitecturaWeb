package org.example.microserviciomonopatines.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PausaMonopatinDTO {

    private Long monopatinId;
    private Long totalMinutosPausa;

}
