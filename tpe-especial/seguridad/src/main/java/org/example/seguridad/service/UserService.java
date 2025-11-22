package org.example.seguridad.service;

import lombok.RequiredArgsConstructor;

import org.example.seguridad.dto.UserDTO;
import org.example.seguridad.model.Authority;
import org.example.seguridad.model.User;
import org.example.seguridad.repository.AuthorityRepository;
import org.example.seguridad.repository.UserRepository;
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

        if (dto.getAuthorities() == null) {
            dto.setAuthorities(new ArrayList<>());
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(encoder.encode(dto.getPassword()));

        Set<Authority> roles = dto.getAuthorities()
                .stream()
                .map(roleName -> {
                    return authRepo.findByName(roleName)
                            .orElseGet(() -> {
                                // 👉 crear rol si no existe
                                Authority newAuth = new Authority();
                                newAuth.setName(roleName);
                                return authRepo.save(newAuth); // 🔥 GUARDARLO
                            });
                })
                .collect(Collectors.toSet());

        user.setAuthorities(roles);

        return userRepo.save(user);
    }
}
