package com.sgp.sgp.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.sgp.sgp.model.Contrato;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.query.Param;

public interface ContratoRepository extends JpaRepository<Contrato, Long> {

    /*
     * Busca todos los contratos asociados a un empleado por su ID.
     * Spring Data JPA interpreta "Empleado_IdEmpleado" como:
     * Contrato → empleado → idEmpleado
     * Esto genera automáticamente la consulta:
     * SELECT * FROM contrato WHERE id_empleado = ?
     */
    @EntityGraph(attributePaths = "empleado")
    List<Contrato> findByEmpleado_IdEmpleado(Long idEmpleado);

    /*
     * Consulta todos los contratos con su empleado cargado (JOIN FETCH).
     * Evita problemas de LazyInitialization y asegura que el empleado
     * venga incluido en la respuesta sin necesidad de otra consulta.
     */
    @org.springframework.data.jpa.repository.Query("SELECT c FROM Contrato c JOIN FETCH c.empleado")
    List<Contrato> findAllWithEmpleado();

    /*
     * Busca un contrato específico con su empleado cargado.
     * Útil cuando necesitas el contrato y su relación en una sola consulta.
     */
    @org.springframework.data.jpa.repository.Query("SELECT c FROM Contrato c JOIN FETCH c.empleado WHERE c.idContrato = :id")
    Contrato findByIdWithEmpleado(@Param("id") Long id);

    /*
     * Sobrescribe el método findAll() de JpaRepository para que,
     * al listar contratos, también se cargue el empleado asociado.
     * EntityGraph indica que debe traer la relación "empleado" junto con el contrato.
     */
    @EntityGraph(attributePaths = "empleado")
    List<Contrato> findAll();
}
