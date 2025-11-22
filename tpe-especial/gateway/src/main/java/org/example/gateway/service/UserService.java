package org.example.gateway.service;



import lombok.RequiredArgsConstructor;

import org.example.gateway.DTO.UserDTO;
import org.example.gateway.model.Authority;
import org.example.gateway.model.User;
import org.example.gateway.repository.AuthorityRepository;
import org.example.gateway.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepo;
    private final AuthorityRepository authRepo;
    private final PasswordEncoder encoder;

    @Transactional
    public User createUser(UserDTO dto) {

        if (dto.getAuthorities() == null || dto.getAuthorities().isEmpty()) {
            dto.setAuthorities(new ArrayList<>());
            dto.getAuthorities().add("USER"); // rol por defecto
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(encoder.encode(dto.getPassword()));

        // Crear o traer roles existentes
        Set<Authority> roles = dto.getAuthorities()
                .stream()
                .map(roleName ->
                        authRepo.findByName(roleName)
                                .orElseGet(() -> {
                                    Authority newAuth = new Authority();
                                    newAuth.setName(roleName);
                                    return authRepo.save(newAuth);
                                })
                )
                .collect(Collectors.toSet());

        user.setAuthorities(roles);

        return userRepo.save(user);
    }
}
