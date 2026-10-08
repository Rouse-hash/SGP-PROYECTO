package com.sgp.sgp.service;

import com.sgp.sgp.model.Empleado;
import com.sgp.sgp.model.Usuario;
import com.sgp.sgp.repository.UsuarioRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SesionService {

    private final UsuarioRepository usuarioRepository;

    public SesionService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /*
     * Devuelve el empleado vinculado al usuario autenticado (correo del JWT).
     */
    public Optional<Empleado> empleadoActual(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof String)) {
            return Optional.empty();
        }
        String correo = (String) authentication.getPrincipal();
        return usuarioRepository.findByCorreo(correo).map(Usuario::getEmpleado);
    }

    /*
     * Indica si el usuario autenticado tiene rol ADMIN.
     */
    public boolean esAdmin(Authentication authentication) {
        return authentication != null && authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    /*
     * ID del empleado vinculado al usuario autenticado (correo del JWT).
     */
    public Optional<Long> idEmpleadoActual(Authentication authentication) {
        return empleadoActual(authentication).map(Empleado::getIdEmpleado);
    }

    /*
     * Indica si el usuario autenticado puede acceder a los datos de un empleado:
     * los ADMIN acceden a cualquiera, los demás solo a su propio empleado.
     */
    public boolean puedeAccederEmpleado(Authentication authentication, Long idEmpleado) {
        if (esAdmin(authentication)) {
            return true;
        }
        return idEmpleadoActual(authentication)
                .filter(miId -> miId.equals(idEmpleado))
                .isPresent();
    }

    /*
     * Indica si el usuario autenticado puede buscar por un documento:
     * los ADMIN buscan cualquiera, los demás solo el suyo.
     */
    public boolean puedeAccederDocumento(Authentication authentication, String numeroDocumento) {
        if (esAdmin(authentication)) {
            return true;
        }
        return empleadoActual(authentication)
                .map(Empleado::getNumeroDocumento)
                .filter(miDocumento -> miDocumento.equals(numeroDocumento))
                .isPresent();
    }
}
