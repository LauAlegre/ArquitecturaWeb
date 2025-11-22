package example.org.microserviciofacturacion.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class CuentaClient {

    private final RestTemplate restTemplate;

    public CuentaClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void debitarSaldo(Long idCuenta, double monto) {
        String url = UriComponentsBuilder
                .fromHttpUrl("http://localhost:8080/cuentas/{id}/debitar/{monto}")
                .buildAndExpand(idCuenta, monto)
                .toUriString();

        restTemplate.put(url, null);
    }
}

