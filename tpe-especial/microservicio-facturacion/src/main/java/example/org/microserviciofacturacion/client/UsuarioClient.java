package example.org.microserviciofacturacion.client;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class UsuarioClient {
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

        return restTemplate.getForObject(url, Boolean.class);
    }
}
