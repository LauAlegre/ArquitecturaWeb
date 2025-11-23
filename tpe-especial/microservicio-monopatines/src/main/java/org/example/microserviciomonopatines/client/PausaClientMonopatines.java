package org.example.microserviciomonopatines.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.example.microserviciomonopatines.dto.PausaMonopatinDTO;

@Component
public class PausaClientMonopatines {

    private final RestTemplate restTemplate;

    public PausaClientMonopatines(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**

     Obtiene los minutos de pausa acumulados de un monopatín,
     consultando al microservicio de viajes.*/
    public PausaMonopatinDTO obtenerPausasDeMonopatin(Long monopatinId) {
        if (monopatinId == null) {
            throw new IllegalArgumentException("monopatinId es obligatorio");}

        String url = UriComponentsBuilder
                .fromHttpUrl("http://localhost:8081/api/v1/viajes/pausas/monopatines/{monopatinId}/minutos")
                .buildAndExpand(monopatinId)
                .toUriString();

        return restTemplate.getForObject(url, PausaMonopatinDTO.class);
    }
}
