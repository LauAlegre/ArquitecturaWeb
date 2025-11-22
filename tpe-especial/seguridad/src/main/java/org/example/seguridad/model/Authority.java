package org.example.seguridad.model;



import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Authority  implements GrantedAuthority {

    @Id
    private String name;

    @Override
    public String getAuthority() {
        return name;
    }
}

