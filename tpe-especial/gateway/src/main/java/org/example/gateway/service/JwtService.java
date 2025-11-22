package org.example.gateway.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class JwtService {

    private static final String SECRET =
            "k9JHF7pL8aZ3qT6wN4vR1mY8cB5dS9hQ2eT7uW4xP3kL0zN6rV8tM5yB1cG4";

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    public String generarToken(UserDetails user) {
        return Jwts.builder()
                .subject(user.getUsername())
                .claim("roles", user.getAuthorities())
                .issuedAt(new Date())
                .expiration(Date.from(Instant.now().plus(2, ChronoUnit.HOURS)))
                .signWith(getSigningKey())
                .compact();
    }

    public String extraerUsername(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())   // ← este método ES de 0.12.6
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}

