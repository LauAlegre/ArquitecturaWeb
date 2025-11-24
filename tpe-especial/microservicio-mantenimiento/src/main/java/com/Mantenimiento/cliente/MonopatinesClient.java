package com.Mantenimiento.cliente;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class MonopatinesClient {

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Cambia el estado del monopatín. Si paradaId != null, también la envía.
     */
    public void cambiarEstado(Long monopatinId, String estado) {
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl("http://localhost:8083/monopatines/{id}/estado")
                .queryParam("estado", estado);




        String url = builder.buildAndExpand(monopatinId).toUriString();

        restTemplate.put(url, null);
    }
}
