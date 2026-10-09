package com.sgp.sgp.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey key;
    private final long expiration;

    public JwtUtil(@Value("${jwt.secret}") String secret,
                   @Value("${jwt.expiration}") long expiration) {
        this.key = construirClave(secret);
        this.expiration = expiration;
    }

    /*
        Valida y construye la clave JWT.
        Lanza un mensaje claro si la variable no está configurada o es muy corta.
    */
    public static SecretKey construirClave(String secret) {
        if (secret == null || secret.isBlank()) {
            throw new IllegalStateException(
                    "JWT_SECRET no está configurado. Defínalo en las variables de entorno del servidor "
                            + "(Render: Environment -> JWT_SECRET).");
        }
        byte[] bytes = secret.getBytes(StandardCharsets.UTF_8);
        if (bytes.length < 32) {
            throw new IllegalStateException(
                    "JWT_SECRET es demasiado corto: debe tener al menos 32 caracteres.");
        }
        return Keys.hmacShaKeyFor(bytes);
    }

    public String generateToken(String correo, String rol) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .subject(correo)
                .claim("rol", rol)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(key)
                .compact();
    }
}
