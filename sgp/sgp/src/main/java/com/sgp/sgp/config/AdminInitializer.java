package com.sgp.sgp.config;

import com.sgp.sgp.model.Usuario;
import com.sgp.sgp.repository.UsuarioRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminInitializer.class);

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${BOOTSTRAP_ADMIN_CORREO:}")
    private String correo;

    @Value("${BOOTSTRAP_ADMIN_PASSWORD:}")
    private String password;

    public AdminInitializer(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (correo == null || correo.isBlank() || password == null || password.isBlank()) {
            log.debug("Administrador inicial no configurado (BOOTSTRAP_ADMIN_CORREO / BOOTSTRAP_ADMIN_PASSWORD).");
            return;
        }

        String correoLimpio = correo.trim();

        if (usuarioRepository.existsByCorreo(correoLimpio)) {
            log.info("El administrador inicial ya existe: {}", correoLimpio);
            return;
        }

        Usuario administrador = new Usuario(
                null,
                null,
                correoLimpio,
                passwordEncoder.encode(password),
                "ADMIN",
                true);

        usuarioRepository.save(administrador);

        log.info("Administrador inicial creado correctamente: {}", correoLimpio);
    }
}
