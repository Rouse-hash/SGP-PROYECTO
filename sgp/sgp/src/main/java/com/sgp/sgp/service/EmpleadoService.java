package com.sgp.sgp.service;

import java.util.List;
import com.sgp.sgp.model.Empleado;

/*
    Interfaz que define las operaciones del módulo Empleado
    trabajando directamente con la entidad JPA.
*/
public interface EmpleadoService {

    /*
        Lista todos los empleados.
        Ejemplo: GET /api/empleados
    */
    List<Empleado> listarEmpleados();

    /*
        Busca un empleado por ID.
        Ejemplo: GET /api/empleados/5
    */
    Empleado buscarEmpleadoPorId(Long idEmpleado);

    /*
        Crea un nuevo empleado.
        Ejemplo: POST /api/empleados
    */
    Empleado crearEmpleado(Empleado empleado);

    /*
        Actualiza un empleado existente.
        Ejemplo: PUT /api/empleados/5
    */
    Empleado actualizarEmpleado(Long idEmpleado, Empleado empleado);

    /*
        Elimina un empleado por ID.
        Ejemplo: DELETE /api/empleados/5
    */
    void eliminarEmpleado(Long idEmpleado);
}



