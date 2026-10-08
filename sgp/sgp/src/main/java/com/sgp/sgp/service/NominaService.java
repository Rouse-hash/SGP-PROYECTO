package com.sgp.sgp.service;

import com.sgp.sgp.model.Nomina;

import java.util.List;
import java.util.Optional;

public interface NominaService {

    /*
     * Lista todas las nóminas registradas.
     */
    List<Nomina> listarNominas();

    /*
     * Busca una nómina por su ID.
     */
    Optional<Nomina> buscarPorId(Long idNomina);

    /*
     * Guarda una nueva nómina.
     */
    Nomina guardarNomina(Nomina nomina);

    /*
     * Actualiza una nómina existente.
     */
    Nomina actualizarNomina(Long idNomina, Nomina nomina);

    /*
     * Elimina una nómina por ID.
     */
    void eliminarNomina(Long idNomina);

    /*
     * Lista nóminas por departamento (ej: "Meta").
     */
    List<Nomina> listarPorDepartamento(String departamento);

    /*
     * Lista nóminas por municipio (ej: "Villavicencio").
     */
    List<Nomina> listarPorMunicipio(String municipio);

    /*
     * Lista nóminas de un empleado por su ID.
     */
    List<Nomina> listarPorEmpleado(Long idEmpleado);
}
