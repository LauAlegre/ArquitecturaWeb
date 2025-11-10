package client;

import com.monopatines.usuarios.dto.UsoCuentaDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;

@Component
public class ViajesClient {

    private final RestTemplate restTemplate = new RestTemplate();

    // 🔹 trae los viajes por usuario
    public UsoCuentaDTO obtenerUsoPorUsuario(Long idUsuario, LocalDate desde, LocalDate hasta) {
        String url = UriComponentsBuilder
                .fromHttpUrl("http://localhost:8082/viajes/uso-usuario")
                .queryParam("idUsuario", idUsuario)
                .queryParam("desde", desde)
                .queryParam("hasta", hasta)
                .toUriString();

        return restTemplate.getForObject(url, UsoCuentaDTO.class);
    }

    // 🔹 trae los viajes por cuenta
    public UsoCuentaDTO obtenerUsoPorCuenta(Long idCuenta, LocalDate desde, LocalDate hasta) {
        String url = UriComponentsBuilder
                .fromHttpUrl("http://localhost:8082/viajes/uso-cuenta")
                .queryParam("idCuenta", idCuenta)
                .queryParam("desde", desde)
                .queryParam("hasta", hasta)
                .toUriString();

        return restTemplate.getForObject(url, UsoCuentaDTO.class);
    }
}
