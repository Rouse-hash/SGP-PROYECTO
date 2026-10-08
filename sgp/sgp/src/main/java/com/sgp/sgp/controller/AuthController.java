package com.sgp.sgp.controller;

import com.sgp.sgp.dto.LoginRequest;
import com.sgp.sgp.dto.LoginResponse;
import com.sgp.sgp.model.Usuario;
import com.sgp.sgp.service.UsuarioService;
import com.sgp.sgp.util.JwtUtil;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioService usuarioService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UsuarioService usuarioService, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Usuario encontrado = usuarioService.buscarPorCorreo(loginRequest.getCorreo()).orElse(null);
        if (encontrado == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }

        String passwordPlano = loginRequest.getPassword();
        String passwordAlmacenado = encontrado.getPassword();

        boolean coincide = false;

        if (passwordAlmacenado.startsWith("$2a$") || passwordAlmacenado.startsWith("$2b$") || passwordAlmacenado.startsWith("$2y$")) {
            coincide = passwordEncoder.matches(passwordPlano, passwordAlmacenado);
        } else {
            coincide = passwordAlmacenado.equals(passwordPlano);
            if (coincide) {
                encontrado.setPassword(passwordEncoder.encode(passwordPlano));
                usuarioService.guardarUsuarioMigrado(encontrado);
            }
        }

        if (coincide) {
            String token = jwtUtil.generateToken(encontrado.getCorreo(), encontrado.getRol());
            LoginResponse response = new LoginResponse(token, encontrado.getCorreo(), encontrado.getRol(), "Login exitoso");
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
    }
}
