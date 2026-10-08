package com.sgp.sgp.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.sgp.sgp.model.Empleado;

import java.util.List;
import java.util.Optional;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
    List<Empleado> findAllByOrderByIdEmpleadoDesc(Pageable pageable);
    Optional<Empleado> findByNumeroDocumento(String numeroDocumento);
}

