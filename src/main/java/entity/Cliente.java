package entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Genera getters, setters, toString(), etc.
@NoArgsConstructor // Genera el constructor vacío: new Cliente()
@AllArgsConstructor // Genera el constructor con todos los campos
public class Cliente {
    private String nombre;
    private String email;
}
