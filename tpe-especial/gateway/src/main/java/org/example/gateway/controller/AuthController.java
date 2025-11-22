package org.example.gateway.controller;

import lombok.RequiredArgsConstructor;
import org.example.gateway.DTO.LoginRequest;
import org.example.gateway.DTO.UserDTO;
import org.example.gateway.model.User;
import org.example.gateway.service.JwtService;
import org.example.gateway.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final UserService userService;

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginRequest dto) {

        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getUsername(),
                        dto.getPassword()
                )
        );

        String token = jwtService.generarToken((UserDetails) auth.getPrincipal());

        return Map.of("token", token);
    }

    @PostMapping("/register")
    public User crear(@RequestBody UserDTO dto) {
        return userService.createUser(dto);
    }
}

