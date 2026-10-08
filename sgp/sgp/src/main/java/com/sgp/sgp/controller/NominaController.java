package com.sgp.sgp.controller;

import com.sgp.sgp.model.Nomina;
import com.sgp.sgp.service.NominaService;
import com.sgp.sgp.service.SesionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nominas")
public class NominaController {

    private final NominaService nominaService;
    private final SesionService sesionService;

    public NominaController(NominaService nominaService, SesionService sesionService) {
        this.nominaService = nominaService;
        this.sesionService = sesionService;
    }

    /*
     * Lista todas las nóminas.
     * GET /api/nominas
     */
    @GetMapping
    public List<Nomina> listarNominas(Authentication authentication) {
        if (!sesionService.esAdmin(authentication)) {
            return nominasPropias(authentication);
        }
        return nominaService.listarNominas();
    }

    /*
     * Busca una nómina por ID.
     * GET /api/nominas/{idNomina}
     */
    @GetMapping("/{idNomina}")
    public ResponseEntity<Nomina> buscarPorId(@PathVariable Long idNomina,
                                              Authentication authentication) {
        if (!sesionService.esAdmin(authentication)) {
            if (!esNominaPropia(authentication, idNomina)) {
                return ResponseEntity.notFound().build();
            }
        }
        return nominaService.buscarPorId(idNomina)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /*
     * Crea una nueva nómina.
     * POST /api/nominas
     */
    @PostMapping
    public Nomina crearNomina(@Valid @RequestBody Nomina nomina) {
        return nominaService.guardarNomina(nomina);
    }

    /*
     * Actualiza una nómina existente.
     * PUT /api/nominas/{idNomina}
     */
    @PutMapping("/{idNomina}")
    public ResponseEntity<Nomina> actualizarNomina(
            @PathVariable Long idNomina,
            @Valid @RequestBody Nomina nomina) {

        Nomina actualizada = nominaService.actualizarNomina(idNomina, nomina);
        return ResponseEntity.ok(actualizada);
    }

    /*
     * Elimina una nómina por ID.
     * DELETE /api/nominas/{idNomina}
     */
    @DeleteMapping("/{idNomina}")
    public ResponseEntity<Void> eliminarNomina(@PathVariable Long idNomina) {
        nominaService.eliminarNomina(idNomina);
        return ResponseEntity.noContent().build();
    }

    /*
     * Lista nóminas por departamento.
     * GET /api/nominas/departamento/{departamento}
     */
    @GetMapping("/departamento/{departamento}")
    public List<Nomina> listarPorDepartamento(@PathVariable String departamento,
                                              Authentication authentication) {
        List<Nomina> resultado = nominaService.listarPorDepartamento(departamento);
        return filtrarSiEmpleado(authentication, resultado);
    }

    /*
     * Lista nóminas por municipio.
     * GET /api/nominas/municipio/{municipio}
     */
    @GetMapping("/municipio/{municipio}")
    public List<Nomina> listarPorMunicipio(@PathVariable String municipio,
                                           Authentication authentication) {
        List<Nomina> resultado = nominaService.listarPorMunicipio(municipio);
        return filtrarSiEmpleado(authentication, resultado);
    }

    /*
     * Lista nóminas de un empleado por su ID.
     * GET /api/nominas/empleado/{idEmpleado}
     */
    @GetMapping("/empleado/{idEmpleado}")
    public ResponseEntity<List<Nomina>> listarPorEmpleado(@PathVariable Long idEmpleado,
                                                          Authentication authentication) {
        if (!sesionService.puedeAccederEmpleado(authentication, idEmpleado)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(nominaService.listarPorEmpleado(idEmpleado));
    }

    private List<Nomina> nominasPropias(Authentication authentication) {
        Long miId = sesionService.idEmpleadoActual(authentication).orElse(null);
        if (miId == null) {
            return List.of();
        }
        return nominaService.listarPorEmpleado(miId);
    }

    private List<Nomina> filtrarSiEmpleado(Authentication authentication, List<Nomina> resultado) {
        if (sesionService.esAdmin(authentication)) {
            return resultado;
        }
        Long miId = sesionService.idEmpleadoActual(authentication).orElse(null);
        if (miId == null) {
            return List.of();
        }
        return resultado.stream()
                .filter(n -> n.getEmpleado() != null && miId.equals(n.getEmpleado().getIdEmpleado()))
                .toList();
    }

    private boolean esNominaPropia(Authentication authentication, Long idNomina) {
        Long miId = sesionService.idEmpleadoActual(authentication).orElse(null);
        if (miId == null) {
            return false;
        }
        return nominaService.buscarPorId(idNomina)
                .filter(n -> n.getEmpleado() != null && miId.equals(n.getEmpleado().getIdEmpleado()))
                .isPresent();
    }
}
