package com.sgp.sgp.service;

import java.util.List;
import com.sgp.sgp.model.Contrato;

/*
    Interfaz que define las operaciones del módulo Contrato
    trabajando directamente con la entidad JPA.
*/
public interface ContratoService {

    /*
        Lista todos los contratos.
        Ejemplo: GET /api/contratos
    */
    List<Contrato> listarContratos();

    /*
        Busca un contrato por ID.
        Ejemplo: GET /api/contratos/5
    */
    Contrato buscarContratoPorId(Long idContrato);

    /*
        Lista contratos asociados a un empleado.
        Ejemplo: GET /api/contratos/empleado/3
    */
    List<Contrato> listarContratosPorEmpleado(Long idEmpleado);

    /*
        Crea un contrato asociado a un empleado existente.
        Ejemplo: POST /api/contratos/empleado/3
    */
    Contrato crearContrato(Long idEmpleado, Contrato contrato);

    /*
        Actualiza un contrato existente.
        Ejemplo: PUT /api/contratos/10
    */
    Contrato actualizarContrato(Long idContrato, Long idEmpleado, Contrato contrato);

    /*
        Elimina un contrato por ID.
        Ejemplo: DELETE /api/contratos/10
    */
    void eliminarContrato(Long idContrato);
}





