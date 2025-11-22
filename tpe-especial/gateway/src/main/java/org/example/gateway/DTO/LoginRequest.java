package org.example.gateway.DTO;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
