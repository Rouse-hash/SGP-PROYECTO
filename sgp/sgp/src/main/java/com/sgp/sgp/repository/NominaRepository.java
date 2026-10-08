package com.sgp.sgp.repository;

import com.sgp.sgp.model.Nomina;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

@Repository
public interface NominaRepository extends JpaRepository<Nomina, Long> {

    @EntityGraph(attributePaths = {"empleado", "contrato"})
    List<Nomina> findAll();

    @EntityGraph(attributePaths = {"empleado", "contrato"})
    Optional<Nomina> findById(Long id);

    List<Nomina> findByDepartamento(String departamento);

    List<Nomina> findByMunicipio(String municipio);

    boolean existsByEmpleadoIdEmpleadoAndMunicipio(Long idEmpleado, String municipio);

    @Query("SELECT COALESCE(SUM(n.totalPagado), 0) FROM Nomina n")
    Double sumTotalPagado();

    @EntityGraph(attributePaths = {"empleado", "contrato"})
    List<Nomina> findByEmpleadoIdEmpleado(Long idEmpleado);

    @EntityGraph(attributePaths = {"empleado", "contrato"})
    List<Nomina> findByContrato_IdContrato(Long idContrato);
}
