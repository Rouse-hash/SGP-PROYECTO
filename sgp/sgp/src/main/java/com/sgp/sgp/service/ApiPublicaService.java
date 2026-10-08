package com.sgp.sgp.service;

// Importa la anotación @Service para indicar que esta clase
// pertenece a la capa de lógica de negocio de Spring Boot.
import org.springframework.stereotype.Service;

// Importa la clase RestTemplate, utilizada para consumir
// servicios web o API REST externas mediante peticiones HTTP.
import org.springframework.web.client.RestTemplate;

// Indica que esta clase es un Servicio administrado por Spring.
@Service
public class ApiPublicaService {

    // Objeto encargado de realizar las peticiones HTTP
    // hacia una API pública externa.
    private final RestTemplate restTemplate;

    // Constructor de la clase.
    // Inicializa el objeto RestTemplate para poder consumir
    // servicios web externos.
    public ApiPublicaService() {
        this.restTemplate = new RestTemplate();
    }

    // Método que consulta una API pública externa.
    // Retorna la información recibida en formato JSON como una cadena de texto.
    public String obtenerUsuariosExternos() {

        // URL de la API pública utilizada para la práctica.
        // Esta API pertenece a JSONPlaceholder y proporciona
        // datos ficticios para pruebas de desarrollo.
        String url = "https://jsonplaceholder.typicode.com/users";

        // Realiza una petición HTTP GET a la URL indicada.
        //
        // Parámetros:
        // - url: dirección del servicio web.
        // - String.class: indica que la respuesta será devuelta
        //   como una cadena de texto (JSON).
        //
        // El método retorna directamente la respuesta obtenida
        // desde la API pública.
        return restTemplate.getForObject(url, String.class);
    }
}
