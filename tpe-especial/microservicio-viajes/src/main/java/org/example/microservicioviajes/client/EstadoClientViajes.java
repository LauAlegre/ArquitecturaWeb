package org.example.microservicioviajes.client;

import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class EstadoClientViajes {

    private final RestTemplate restTemplate = new RestTemplate();

    // Cambia el estado del monopatín (p.ej., EN_USO, DISPONIBLE, EN_MANTENIMIENTO)
    public void cambiarEstado(Long monopatinId, String estado) {
        if (monopatinId == null || estado == null) {
            throw new IllegalArgumentException("monopatinId y estado son obligatorios");
        }

        String url = UriComponentsBuilder
                .fromHttpUrl("http://localhost:8083/monopatines/{id}/estado")
                .queryParam("estado", estado)
                .buildAndExpand(monopatinId)
                .toUriString();

        restTemplate.put(url, null);
    }

    // Finaliza el monopatín (lo deja DISPONIBLE y suma km)
    public void finalizarMonopatin(Long monopatinId, Double kmRecorridos, Long minutos) {
        UriComponents builder = UriComponentsBuilder
                .fromHttpUrl("http://localhost:8083/monopatines/{id}/finalizar")
                .buildAndExpand(monopatinId);

        String url = UriComponentsBuilder.fromUriString(builder.toUriString())
                .queryParam("kmRecorridos", kmRecorridos)
                .queryParam("minutosTotales", minutos)
                .toUriString();

        restTemplate.put(url, null);
    }

}
