package com.sgp.sgp.controller;

import com.sgp.sgp.model.Contrato;
import com.sgp.sgp.service.ContratoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/contratos")
public class ContratoController {

    private final ContratoService contratoService;

    public ContratoController(ContratoService contratoService) {
        this.contratoService = contratoService;
    }

    @GetMapping
    public List<Contrato> listarContratos() {
        return contratoService.listarContratos();
    }

    @GetMapping("/{idContrato}")
    public ResponseEntity<Contrato> buscarContratoPorId(@PathVariable Long idContrato) {
        return ResponseEntity.ok(contratoService.buscarContratoPorId(idContrato));
    }

    // Crear contrato desde la raíz del módulo, el idEmpleado va en el body
    // POST /api/contratos
    @PostMapping
    public ResponseEntity<Contrato> crearContratoRaiz(@RequestBody Map<String, Object> payload) {
        Object idRaw = payload.get("idEmpleado");
        if (idRaw == null) {
            throw new IllegalArgumentException("El campo 'idEmpleado' es obligatorio en el body");
        }
        Long idEmpleado = Long.valueOf(idRaw.toString());

        Contrato contrato = new Contrato();
        contrato.setTipoContrato((String) payload.get("tipoContrato"));
        contrato.setFechaInicio(parseFecha(payload.get("fechaInicio")));
        contrato.setFechaFin(parseFecha(payload.get("fechaFin")));
        Object salarioRaw = payload.get("salario");
        if (salarioRaw != null) {
            contrato.setSalario(Double.valueOf(salarioRaw.toString()));
        }

        return ResponseEntity.ok(contratoService.crearContrato(idEmpleado, contrato));
    }

    // Crear contrato asociado a un empleado
    @PostMapping("/empleado/{idEmpleado}")
    public ResponseEntity<Contrato> crearContrato(
            @PathVariable Long idEmpleado,
            @RequestBody Contrato contrato) {
        return ResponseEntity.ok(contratoService.crearContrato(idEmpleado, contrato));
    }

    // Actualizar contrato existente
    @PutMapping("/{idContrato}")
    public ResponseEntity<Contrato> actualizarContrato(
            @PathVariable Long idContrato,
            @RequestBody Contrato contratoActualizado) {

        Long idEmpleado = contratoActualizado.getEmpleado() != null
                ? contratoActualizado.getEmpleado().getIdEmpleado()
                : null;

        Contrato actualizado = contratoService.actualizarContrato(idContrato, idEmpleado, contratoActualizado);
        return ResponseEntity.ok(actualizado);
    }

    // Eliminar contrato
    @DeleteMapping("/{idContrato}")
    public ResponseEntity<Void> eliminarContrato(@PathVariable Long idContrato) {
        contratoService.eliminarContrato(idContrato);
        return ResponseEntity.noContent().build();
    }

    private LocalDate parseFecha(Object fecha) {
        if (fecha == null || fecha.toString().isBlank()) {
            return null;
        }
        String texto = fecha.toString().trim();
        try {
            return LocalDate.parse(texto);
        } catch (Exception e) {
            return LocalDate.parse(texto, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
    }
}
