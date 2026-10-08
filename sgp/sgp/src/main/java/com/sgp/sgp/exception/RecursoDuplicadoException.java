package com.sgp.sgp.exception;

/*
    Excepción personalizada para manejar casos
    donde se intenta registrar un recurso duplicado
    (número de documento, correo, etc.).
*/
public class RecursoDuplicadoException extends RuntimeException {

    /*
        Constructor que recibe el mensaje del error.
    */
    public RecursoDuplicadoException(String mensaje) {
        super(mensaje);
    }
}