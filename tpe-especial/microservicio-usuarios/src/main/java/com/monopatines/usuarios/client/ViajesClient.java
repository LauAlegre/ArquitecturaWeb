package com.monopatines.usuarios.client;

import com.monopatines.usuarios.dto.UsoCuentaDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;

@Component
public class ViajesClient {

    private final RestTemplate restTemplate ;
    public ViajesClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // 🔹 Trae los viajes por usuario (usa path variable)
    public UsoCuentaDTO obtenerUsoPorUsuario(Long idUsuario, LocalDate desde, LocalDate hasta) {
        String url = UriComponentsBuilder
                .fromHttpUrl("http://localhost:8081/api/v1/viajes/uso-usuario/" + idUsuario)
                .queryParam("desde", desde)
                .queryParam("hasta", hasta)
                .toUriString();

        return restTemplate.getForObject(url, UsoCuentaDTO.class);
    }

    // 🔹 Trae los viajes por cuenta (usa path variable)
    public UsoCuentaDTO obtenerUsoPorCuenta(Long idCuenta, LocalDate desde, LocalDate hasta) {
        String url = UriComponentsBuilder
                .fromHttpUrl("http://localhost:8081/api/v1/viajes/uso-cuenta/" + idCuenta)
                .queryParam("desde", desde)
                .queryParam("hasta", hasta)
                .toUriString();

        return restTemplate.getForObject(url, UsoCuentaDTO.class);
    }
}

