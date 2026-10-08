package com.sgp.sgp.repository;

import com.sgp.sgp.model.ArchivoEmpleado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArchivoEmpleadoRepository extends JpaRepository<ArchivoEmpleado, Long> {
    List<ArchivoEmpleado> findByIdEmpleadoOrderByFechaSubidaDesc(Long idEmpleado);

    void deleteByIdEmpleado(Long idEmpleado);
}
