package com.sgp.sgp.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class ColombiaService {

    private final RestTemplate restTemplate;

    public ColombiaService() {
        this.restTemplate = new RestTemplate();
    }

    public List<Map<String, Object>> getDepartamentos() {
        String url = "https://api-colombia.com/api/v1/Department";
        List<Map<String, Object>> response = restTemplate.exchange(
                url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {}
        ).getBody();

        for (Map<String, Object> dept : response) {
            dept.put("nombre", dept.remove("name"));
        }

        return response;
    }

    public List<Map<String, Object>> getMunicipios(Integer idDepartamento) {
        String url = "https://api-colombia.com/api/v1/Department/" + idDepartamento + "/cities";
        List<Map<String, Object>> response = restTemplate.exchange(
                url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {}
        ).getBody();

        for (Map<String, Object> city : response) {
            city.put("nombre", city.remove("name"));
        }

        return response;
    }
}
