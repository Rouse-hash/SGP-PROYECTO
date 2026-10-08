package com.sgp.sgp.repository;

import com.sgp.sgp.model.Usuario;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @EntityGraph(attributePaths = "empleado")
    List<Usuario> findAll();

    @EntityGraph(attributePaths = "empleado")
    Optional<Usuario> findById(Long id);

    @EntityGraph(attributePaths = "empleado")
    Optional<Usuario> findByCorreo(String correo);

    boolean existsByCorreo(String correo);

    @EntityGraph(attributePaths = "empleado")
    Optional<Usuario> findByEmpleado_IdEmpleado(Long idEmpleado);
}
