package com.sgp.sgp.config;

import com.sgp.sgp.security.JwtFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {})
                .sessionManagement(session -> session.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/error",
                                "/api/auth/**",
                                "/api/publica/**",
                                "/api/health"
                        ).permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/empleados/**").hasAnyRole("ADMIN", "EMPLEADO")
                        .requestMatchers("/api/empleados/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/nominas/**").hasAnyRole("ADMIN", "EMPLEADO")
                        .requestMatchers("/api/nominas/**").hasRole("ADMIN")
                        .requestMatchers("/api/contratos/**").hasRole("ADMIN")
                        .requestMatchers("/api/usuarios/**").hasRole("ADMIN")
                        .requestMatchers("/api/dashboard/**").hasAnyRole("ADMIN", "EMPLEADO")
                        .requestMatchers(HttpMethod.GET, "/api/archivos/*/descargar").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/archivos/**").hasAnyRole("ADMIN", "EMPLEADO")
                        .requestMatchers("/api/archivos/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                        )
                // Sesión sin token válido o sin usuario -> 401
                // Token válido pero sin permisos -> 403
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(form -> form.disable())
                .addFilterBefore(
                        jwtFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
