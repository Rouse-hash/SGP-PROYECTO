package com.sgp.sgp.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sgp.sgp.exception.RecursoNoEncontradoException;
import com.sgp.sgp.exception.RecursoDuplicadoException;
import com.sgp.sgp.model.Contrato;
import com.sgp.sgp.model.Empleado;
import com.sgp.sgp.model.Nomina;
import com.sgp.sgp.model.Usuario;
import com.sgp.sgp.repository.ArchivoEmpleadoRepository;
import com.sgp.sgp.repository.ContratoRepository;
import com.sgp.sgp.repository.DetalleNominaRepository;
import com.sgp.sgp.repository.EmpleadoRepository;
import com.sgp.sgp.repository.NominaRepository;
import com.sgp.sgp.repository.ReporteNominaRepository;
import com.sgp.sgp.repository.UsuarioRepository;

/*
    Implementación de la lógica de negocio para Empleado
    trabajando directamente con la entidad JPA.
    Se encarga de manejar las operaciones CRUD con validaciones.
*/
@Service
public class EmpleadoServiceImpl implements EmpleadoService {

    /*
        Repository de empleados para acceder a la base de datos.
    */
    private final EmpleadoRepository empleadoRepository;
    private final ContratoRepository contratoRepository;
    private final NominaRepository nominaRepository;
    private final DetalleNominaRepository detalleNominaRepository;
    private final ReporteNominaRepository reporteNominaRepository;
    private final ArchivoEmpleadoRepository archivoEmpleadoRepository;
    private final UsuarioRepository usuarioRepository;

    /*
        Constructor para inyección de dependencias.
    */
    public EmpleadoServiceImpl(EmpleadoRepository empleadoRepository,
                               ContratoRepository contratoRepository,
                               NominaRepository nominaRepository,
                               DetalleNominaRepository detalleNominaRepository,
                               ReporteNominaRepository reporteNominaRepository,
                               ArchivoEmpleadoRepository archivoEmpleadoRepository,
                               UsuarioRepository usuarioRepository) {
        this.empleadoRepository = empleadoRepository;
        this.contratoRepository = contratoRepository;
        this.nominaRepository = nominaRepository;
        this.detalleNominaRepository = detalleNominaRepository;
        this.reporteNominaRepository = reporteNominaRepository;
        this.archivoEmpleadoRepository = archivoEmpleadoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /*
        Lista todos los empleados registrados en la base de datos.
    */
    @Override
    public List<Empleado> listarEmpleados() {
        return empleadoRepository.findAll();
    }

    /*
        Busca un empleado por su ID.
        Si no existe, lanza una excepción personalizada.
    */
    @Override
    public Empleado buscarEmpleadoPorId(Long idEmpleado) {
        return empleadoRepository.findById(idEmpleado)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Empleado no encontrado con ID: " + idEmpleado));
    }

    /*
        Crea un nuevo empleado.
        Valida que el número de documento no esté registrado.
    */
    @Override
    public Empleado crearEmpleado(Empleado empleado) {
        validarDocumentoNoDuplicado(empleado.getNumeroDocumento(), null);
        return empleadoRepository.save(empleado);
    }

    /*
        Actualiza un empleado existente.
        Se valida que el empleado exista antes de modificarlo.
    */
    @Override
    public Empleado actualizarEmpleado(Long idEmpleado, Empleado empleado) {
        Empleado existente = empleadoRepository.findById(idEmpleado)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Empleado no encontrado con ID: " + idEmpleado));

        validarDocumentoNoDuplicado(empleado.getNumeroDocumento(), idEmpleado);

        // Actualizar datos básicos
        existente.setNombre(empleado.getNombre());
        existente.setApellidos(empleado.getApellidos());
        existente.setTipoDocumento(empleado.getTipoDocumento());
        existente.setNumeroDocumento(empleado.getNumeroDocumento());
        existente.setFechaNacimiento(empleado.getFechaNacimiento());
        existente.setEstadoCivil(empleado.getEstadoCivil());

        // Guardar cambios
        return empleadoRepository.save(existente);
    }

    /*
        Valida que el número de documento no esté registrado por otro empleado.
        Si ya existe, lanza una excepción de recurso duplicado.
    */
    private void validarDocumentoNoDuplicado(String numeroDocumento, Long idEmpleado) {
        if (numeroDocumento == null || numeroDocumento.isBlank()) {
            return;
        }
        empleadoRepository.findByNumeroDocumento(numeroDocumento)
                .filter(otro -> idEmpleado == null || !otro.getIdEmpleado().equals(idEmpleado))
                .ifPresent(otro -> {
                    throw new RecursoDuplicadoException(
                            "El número de documento ya se encuentra registrado");
                });
    }

    /*
        Elimina un empleado por ID.
        Si no existe, lanza excepción.
        Elimina en cascada nóminas (con sus detalles y reportes), contratos,
        archivos y el usuario vinculado.
    */
    @Override
    @Transactional
    public void eliminarEmpleado(Long idEmpleado) {
        Empleado existente = empleadoRepository.findById(idEmpleado)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Empleado no encontrado con ID: " + idEmpleado));

        List<Nomina> nominas = nominaRepository.findByEmpleadoIdEmpleado(idEmpleado);
        for (Nomina nomina : nominas) {
            detalleNominaRepository.deleteByNomina_IdNomina(nomina.getIdNomina());
            reporteNominaRepository.deleteByNomina_IdNomina(nomina.getIdNomina());
        }
        nominaRepository.deleteAll(nominas);

        List<Contrato> contratos = contratoRepository.findByEmpleado_IdEmpleado(idEmpleado);
        contratoRepository.deleteAll(contratos);

        archivoEmpleadoRepository.deleteByIdEmpleado(idEmpleado);

        Usuario usuario = usuarioRepository.findByEmpleado_IdEmpleado(idEmpleado).orElse(null);
        if (usuario != null) {
            usuarioRepository.delete(usuario);
        }

        empleadoRepository.delete(existente);
    }
}



