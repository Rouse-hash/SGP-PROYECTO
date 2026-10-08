package com.sgp.sgp.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sgp.sgp.exception.RecursoNoEncontradoException;
import com.sgp.sgp.model.Contrato;
import com.sgp.sgp.model.Empleado;
import com.sgp.sgp.model.Nomina;
import com.sgp.sgp.repository.ContratoRepository;
import com.sgp.sgp.repository.DetalleNominaRepository;
import com.sgp.sgp.repository.EmpleadoRepository;
import com.sgp.sgp.repository.NominaRepository;
import com.sgp.sgp.repository.ReporteNominaRepository;

/*
    Implementación de la lógica de negocio para Contrato
    trabajando directamente con la entidad JPA.
    Se encarga de manejar las operaciones CRUD con validaciones.
*/
@Service
public class ContratoServiceImpl implements ContratoService {

    private final ContratoRepository contratoRepository;
    private final EmpleadoRepository empleadoRepository;
    private final NominaRepository nominaRepository;
    private final DetalleNominaRepository detalleNominaRepository;
    private final ReporteNominaRepository reporteNominaRepository;

    // Constructor con inyección de dependencias
    public ContratoServiceImpl(ContratoRepository contratoRepository,
                               EmpleadoRepository empleadoRepository,
                               NominaRepository nominaRepository,
                               DetalleNominaRepository detalleNominaRepository,
                               ReporteNominaRepository reporteNominaRepository) {
        this.contratoRepository = contratoRepository;
        this.empleadoRepository = empleadoRepository;
        this.nominaRepository = nominaRepository;
        this.detalleNominaRepository = detalleNominaRepository;
        this.reporteNominaRepository = reporteNominaRepository;
    }

    /*
        Listar todos los contratos registrados en la base de datos.
    */
    @Override
    public List<Contrato> listarContratos() {
        return contratoRepository.findAll();
    }

    /*
        Buscar un contrato por su ID.
        Si no existe, lanza una excepción personalizada.
    */
    @Override
    public Contrato buscarContratoPorId(Long idContrato) {
        return contratoRepository.findById(idContrato)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Contrato no encontrado con ID: " + idContrato));
    }

    /*
        Listar contratos asociados a un empleado específico.
    */
    @Override
    public List<Contrato> listarContratosPorEmpleado(Long idEmpleado) {
        return contratoRepository.findByEmpleado_IdEmpleado(idEmpleado);
    }

    /*
        Crear un nuevo contrato asociado a un empleado.
        Primero se valida que el empleado exista.
    */
    @Override
    public Contrato crearContrato(Long idEmpleado, Contrato contrato) {
        // Validar que el empleado exista
        Empleado empleado = empleadoRepository.findById(idEmpleado)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Empleado no encontrado con ID: " + idEmpleado));

        // Asociar el empleado al contrato
        contrato.setEmpleado(empleado);

        // Guardar contrato en la base de datos
        return contratoRepository.save(contrato);
    }

    /*
        Actualizar un contrato existente.
        Si idEmpleado es nulo se conserva el empleado actual.
    */
    @Override
    public Contrato actualizarContrato(Long idContrato, Long idEmpleado, Contrato contrato) {
        Contrato existente = contratoRepository.findById(idContrato)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Contrato no encontrado con ID: " + idContrato));

        // Actualizar datos del contrato
        existente.setTipoContrato(contrato.getTipoContrato());
        existente.setFechaInicio(contrato.getFechaInicio());
        existente.setFechaFin(contrato.getFechaFin());
        existente.setSalario(contrato.getSalario());

        if (idEmpleado != null) {
            Empleado empleado = empleadoRepository.findById(idEmpleado)
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "Empleado no encontrado con ID: " + idEmpleado));
            existente.setEmpleado(empleado);
        }

        // Guardar cambios
        return contratoRepository.save(existente);
    }

    /*
        Eliminar un contrato por su ID.
        Si no existe, lanza excepción.
        Elimina en cascada las nóminas asociadas (y sus detalles y reportes).
    */
    @Override
    @Transactional
    public void eliminarContrato(Long idContrato) {
        Contrato existente = contratoRepository.findById(idContrato)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Contrato no encontrado con ID: " + idContrato));

        List<Nomina> nominas = nominaRepository.findByContrato_IdContrato(idContrato);
        for (Nomina nomina : nominas) {
            detalleNominaRepository.deleteByNomina_IdNomina(nomina.getIdNomina());
            reporteNominaRepository.deleteByNomina_IdNomina(nomina.getIdNomina());
        }
        nominaRepository.deleteAll(nominas);

        contratoRepository.delete(existente);
    }
}
