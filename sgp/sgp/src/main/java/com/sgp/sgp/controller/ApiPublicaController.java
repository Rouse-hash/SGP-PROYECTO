package com.sgp.sgp.controller;

import com.sgp.sgp.service.ApiPublicaService;
import com.sgp.sgp.service.ColombiaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/publica")
public class ApiPublicaController {

    private final ApiPublicaService apiPublicaService;
    private final ColombiaService colombiaService;

    public ApiPublicaController(ApiPublicaService apiPublicaService,
                                ColombiaService colombiaService) {
        this.apiPublicaService = apiPublicaService;
        this.colombiaService = colombiaService;
    }

    @GetMapping("/usuarios")
    public String obtenerUsuariosExternos() {
        return apiPublicaService.obtenerUsuariosExternos();
    }

    @GetMapping("/colombia/departamentos")
    public List<Map<String, Object>> getDepartamentos() {
        return colombiaService.getDepartamentos();
    }

    @GetMapping("/colombia/departamentos/{id}/municipios")
    public List<Map<String, Object>> getMunicipios(@PathVariable Integer id) {
        return colombiaService.getMunicipios(id);
    }
}

