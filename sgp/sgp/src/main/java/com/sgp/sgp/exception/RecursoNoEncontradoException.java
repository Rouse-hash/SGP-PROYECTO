package com.sgp.sgp.exception;

/*
    Excepción personalizada para manejar casos
    donde un recurso no existe en la base de datos.
*/
public class RecursoNoEncontradoException extends RuntimeException {

    /*
        Constructor que recibe el mensaje del error.
    */
    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}

