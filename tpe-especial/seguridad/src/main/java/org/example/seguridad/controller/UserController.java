package org.example.seguridad.controller;



import lombok.RequiredArgsConstructor;

import org.example.seguridad.dto.UserDTO;
import org.example.seguridad.model.User;
import org.example.seguridad.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDTO dto) {
        User user = service.createUser(dto);
        return ResponseEntity.ok(user.getId());
    }

}
