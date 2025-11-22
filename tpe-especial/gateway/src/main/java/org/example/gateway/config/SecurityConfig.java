package org.example.gateway.config;

import lombok.RequiredArgsConstructor;
import org.example.gateway.service.CustomUserDetailsService;
import org.example.gateway.service.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable());

        http.sessionManagement(sess ->
                sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests(auth -> auth

                // ====================
                //     PUBLIC ROUTES
                // ====================
                .requestMatchers("/auth/**").permitAll()

                // ====================
                //     ADMIN ONLY
                // ====================

                // a - Reporte KM con o sin pausas  (acomodar endpoint)
                .requestMatchers(HttpMethod.GET,
                        "/monopatines/reporte/km").hasAuthority("ADMIN")

                // b - Anular cuentas
                .requestMatchers(HttpMethod.PUT,
                        "/cuentas/*/anular").hasAuthority("ADMIN")

                // c - Monopatines más viajes (acomodar endpoint)
                .requestMatchers(HttpMethod.GET,
                        "/api/v1/viajes/reporte/monopatines-mas-viajes")
                .hasAuthority("ADMIN")

                // d - Total facturado
                .requestMatchers(HttpMethod.GET,
                        "/facturas/total").hasAuthority("ADMIN")

                // e - Ranking usuarios por tipo (acomodar endpoint)
                .requestMatchers(HttpMethod.GET,
                        "/api/v1/viajes/uso-usuarios-por-tipo").hasAuthority("ADMIN")

                // f - Crear ajuste tarifa
                .requestMatchers(HttpMethod.POST,
                        "/tarifas/ajuste").hasAuthority("ADMIN")

                // facturas/todas (ya lo tenías)
                .requestMatchers(HttpMethod.GET,
                        "/facturas/todas").hasAuthority("ADMIN")

                // ====================
                //     USER + ADMIN
                // ====================

                // g - Monopatines cercanos
                .requestMatchers(HttpMethod.GET,
                        "/monopatines/cercanos").hasAnyAuthority("USER","ADMIN")

                // h - Uso de cuenta
                .requestMatchers(HttpMethod.GET,
                        "/usuarios/uso/**").hasAnyAuthority("USER","ADMIN")

                // ====================
                //     ANY LOGGED USER
                // ====================
                .anyRequest().authenticated()
        );

        http.authenticationProvider(authProvider());
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(encoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
