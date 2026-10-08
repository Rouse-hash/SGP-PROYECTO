package com.sgp.sgp.controller;

import com.sgp.sgp.model.Empleado;
import com.sgp.sgp.model.Usuario;
import com.sgp.sgp.service.UsuarioService;
import com.sgp.sgp.dto.LoginResponse;
import com.sgp.sgp.dto.RegisterRequest;
import com.sgp.sgp.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST encargado de administrar
 * todas las operaciones CRUD del módulo Usuario.
 *
 * Todas las peticiones serán atendidas bajo la URL:
 * http://localhost:8080/api/usuarios
 */
@RestController

// Define la ruta base del controlador
@RequestMapping("/api/usuarios")
public class UsuarioController {

    /**
     * Inyección de dependencia del servicio Usuario.
     *
     * El controlador NO accede directamente al Repository,
     * sino que delega todas las operaciones al Service,
     * siguiendo la arquitectura por capas.
     */
    private final UsuarioService usuarioService;

    private final JwtUtil jwtUtil;

    /**
     * Constructor utilizado por Spring Boot para
     * inyectar automáticamente el servicio.
     */
    public UsuarioController(UsuarioService usuarioService, JwtUtil jwtUtil) {
        this.usuarioService = usuarioService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Lista todos los usuarios registrados.
     *
     * Método HTTP:
     * GET
     *
     * URL:
     * http://localhost:8080/api/usuarios
     *
     * Retorna una lista de usuarios.
     */
    @GetMapping
    public List<Usuario> listarUsuarios() {
        return usuarioService.listarUsuarios();
    }

    /**
     * Busca un usuario por su identificador.
     *
     * Método HTTP:
     * GET
     *
     * URL:
     * http://localhost:8080/api/usuarios/{id}
     *
     * Si el usuario existe devuelve HTTP 200.
     * Si no existe devuelve HTTP 404.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) {

        return usuarioService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Registra un nuevo usuario y devuelve un token JWT.
     *
     * Método HTTP:
     * POST
     *
     * URL:
     * http://localhost:8080/api/usuarios
     */
    @PostMapping
    public ResponseEntity<?> crearUsuario(@Valid @RequestBody RegisterRequest req) {

        Usuario usuario = new Usuario();
        Empleado empleado = new Empleado();
        empleado.setIdEmpleado(req.getIdEmpleado());
        usuario.setEmpleado(empleado);
        usuario.setCorreo(req.getCorreo());
        usuario.setPassword(req.getPassword());
        usuario.setRol(req.getRol());
        usuario.setActivo(true);

        Usuario guardado = usuarioService.guardarUsuario(usuario);
        String token = jwtUtil.generateToken(guardado.getCorreo(), guardado.getRol());
        LoginResponse response = new LoginResponse(token, guardado.getCorreo(), guardado.getRol(), "Usuario registrado exitosamente");
        return ResponseEntity.ok(response);
    }

    /**
     * Actualiza la información de un usuario existente.
     *
     * Método HTTP:
     * PUT
     *
     * URL:
     * http://localhost:8080/api/usuarios/{id}
     *
     * Recibe:
     * - El ID del usuario.
     * - El objeto Usuario con la información actualizada.
     *
     * Devuelve HTTP 200 cuando la actualización
     * se realiza correctamente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(

            @PathVariable Long id,

            @Valid @RequestBody Usuario usuario) {

        Usuario actualizado = usuarioService.actualizarUsuario(id, usuario);

        return ResponseEntity.ok(actualizado);
    }

    /**
     * Elimina un usuario existente.
     *
     * Método HTTP:
     * DELETE
     *
     * URL:
     * http://localhost:8080/api/usuarios/{id}
     *
     * Devuelve HTTP 204 (No Content)
     * cuando la eliminación fue exitosa.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {

        usuarioService.eliminarUsuario(id);

        return ResponseEntity.noContent().build();
    }
}
