package com.sgp.sgp.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

/*
    Manejador global de excepciones.
    Convierte las excepciones en respuestas HTTP
    con un mensaje legible y sin exponer stacktraces.
*/
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /*
        Recurso no encontrado -> HTTP 404
    */
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Map<String, String>> manejarNoEncontrado(
            RecursoNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("mensaje", ex.getMessage()));
    }

    /*
        Recurso duplicado -> HTTP 409 (Conflict)
    */
    @ExceptionHandler(RecursoDuplicadoException.class)
    public ResponseEntity<Map<String, String>> manejarDuplicado(
            RecursoDuplicadoException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("mensaje", ex.getMessage()));
    }

    /*
        Errores de validación de los DTO (@Valid) -> HTTP 400
    */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> manejarValidacion(
            MethodArgumentNotValidException ex) {
        String mensaje = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": "
                        + (error.getDefaultMessage() != null
                                ? error.getDefaultMessage()
                                : "valor inválido"))
                .collect(Collectors.joining("; "));
        if (mensaje.isBlank()) {
            mensaje = "Datos inválidos";
        }
        return ResponseEntity.badRequest()
                .body(Map.of("mensaje", mensaje));
    }

    /*
        JSON mal formado o tipos incorrectos -> HTTP 400
    */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> manejarCuerpoIlegible(
            HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest()
                .body(Map.of("mensaje", "Cuerpo de la petición inválido"));
    }

    /*
        Violación de integridad (claves foráneas, duplicados)
        -> HTTP 409 (Conflict)
    */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> manejarIntegridad(
            DataIntegrityViolationException ex) {
        log.warn("Violación de integridad de datos: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("mensaje",
                        "La operación no se puede completar: existe información relacionada o duplicada"));
    }

    /*
        Excepciones de negocio lanzadas por controllers/servicios
        (por ejemplo "Usuario no encontrado con ID: X") -> HTTP 400
    */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> manejarRuntime(RuntimeException ex) {
        log.warn("Excepción de negocio: {}", ex.getMessage());
        return ResponseEntity.badRequest()
                .body(Map.of("mensaje",
                        ex.getMessage() != null ? ex.getMessage() : "Error en la operación"));
    }

    /*
        Cualquier otro error -> HTTP 500 sin exponer detalles internos
    */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> manejarGeneral(Exception ex) {
        log.error("Error interno no controlado", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("mensaje", "Error interno del servidor"));
    }
}
