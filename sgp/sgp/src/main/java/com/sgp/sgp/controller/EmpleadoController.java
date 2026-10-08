package com.sgp.sgp.controller;

import com.sgp.sgp.dto.ResumenEmpleadoDTO;
import com.sgp.sgp.model.ArchivoEmpleado;
import com.sgp.sgp.model.Contrato;
import com.sgp.sgp.model.Empleado;
import com.sgp.sgp.repository.ContratoRepository;
import com.sgp.sgp.repository.EmpleadoRepository;
import com.sgp.sgp.service.ArchivoService;
import com.sgp.sgp.service.EmpleadoService;
import com.sgp.sgp.service.SesionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {

    private final EmpleadoService empleadoService;
    private final EmpleadoRepository empleadoRepository;
    private final ContratoRepository contratoRepository;
    private final ArchivoService archivoService;
    private final SesionService sesionService;

    public EmpleadoController(EmpleadoService empleadoService,
                              EmpleadoRepository empleadoRepository,
                              ContratoRepository contratoRepository,
                              ArchivoService archivoService,
                              SesionService sesionService) {
        this.empleadoService = empleadoService;
        this.empleadoRepository = empleadoRepository;
        this.contratoRepository = contratoRepository;
        this.archivoService = archivoService;
        this.sesionService = sesionService;
    }

    @GetMapping
    public List<Empleado> listarEmpleados(Authentication authentication) {
        if (!sesionService.esAdmin(authentication)) {
            return sesionService.empleadoActual(authentication)
                    .map(List::of)
                    .orElse(List.of());
        }
        return empleadoService.listarEmpleados();
    }

    @GetMapping("/me")
    public ResponseEntity<Empleado> obtenerMiEmpleado(Authentication authentication) {
        return sesionService.empleadoActual(authentication)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{idEmpleado}")
    public ResponseEntity<Empleado> buscarEmpleadoPorId(@PathVariable Long idEmpleado,
                                                        Authentication authentication) {
        if (!sesionService.puedeAccederEmpleado(authentication, idEmpleado)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Empleado empleado = empleadoService.buscarEmpleadoPorId(idEmpleado);
        return ResponseEntity.ok(empleado);
    }

    @GetMapping("/documento/{numeroDocumento}")
    public ResponseEntity<Empleado> buscarPorDocumento(@PathVariable String numeroDocumento,
                                                       Authentication authentication) {
        if (!sesionService.puedeAccederDocumento(authentication, numeroDocumento)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return empleadoRepository.findByNumeroDocumento(numeroDocumento)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/resumen")
    public ResponseEntity<ResumenEmpleadoDTO> obtenerResumen(@PathVariable Long id,
                                                             Authentication authentication) {
        if (!sesionService.puedeAccederEmpleado(authentication, id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Empleado empleado = empleadoService.buscarEmpleadoPorId(id);
        List<Contrato> contratos = contratoRepository.findByEmpleado_IdEmpleado(id);
        List<ArchivoEmpleado> archivos = archivoService.listarArchivos(id);
        return ResponseEntity.ok(new ResumenEmpleadoDTO(empleado, contratos, archivos));
    }

    @PostMapping
    public Empleado crearEmpleado(@RequestBody Empleado empleado) {
        return empleadoService.crearEmpleado(empleado);
    }

    @PutMapping("/{idEmpleado}")
    public ResponseEntity<Empleado> actualizarEmpleado(
            @PathVariable Long idEmpleado,
            @RequestBody Empleado empleadoActualizado) {

        Empleado actualizado = empleadoService.actualizarEmpleado(idEmpleado, empleadoActualizado);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{idEmpleado}")
    public ResponseEntity<Void> eliminarEmpleado(@PathVariable Long idEmpleado) {
        empleadoService.eliminarEmpleado(idEmpleado);
        return ResponseEntity.noContent().build();
    }
}
