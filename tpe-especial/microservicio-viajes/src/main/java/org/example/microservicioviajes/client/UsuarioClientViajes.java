package org.example.microservicioviajes.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;

@Component
public class UsuarioClientViajes {

    private final RestTemplate restTemplate = new RestTemplate();

    // Consulta si un usuario es admin en el microservicio de usuarios.
    public Boolean esAdmin(Long idUsuario) {
        if (idUsuario == null) {
            return false;
        }
        String url = UriComponentsBuilder
                .fromHttpUrl("http://localhost:8080/usuarios/{id}/es-admin")
                .buildAndExpand(idUsuario)
                .toUriString();

        Boolean resp = restTemplate.getForObject(url, Boolean.class);
        return resp != null && resp;
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
