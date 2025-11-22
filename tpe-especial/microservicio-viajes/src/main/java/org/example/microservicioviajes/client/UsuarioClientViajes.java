package org.example.microservicioviajes.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;

@Component
public class UsuarioClientViajes {

    private final RestTemplate restTemplate ;
    public UsuarioClientViajes(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Obtiene los IDs de cuentas/usuarios por tipo de cuenta desde el microservicio
    // de usuarios.
    public List<Long> obtenerIdsUsuariosPorTipo(String tipoCuenta) {
        String url = UriComponentsBuilder
                .fromHttpUrl("http://localhost:8080/usuarios/por-tipo-cuenta")
                .queryParam("tipoCuenta", tipoCuenta)
                .toUriString();

        Long[] arr = restTemplate.getForObject(url, Long[].class);
        if (arr == null || arr.length == 0)
            return List.of();
        return Arrays.asList(arr);
    }

}
