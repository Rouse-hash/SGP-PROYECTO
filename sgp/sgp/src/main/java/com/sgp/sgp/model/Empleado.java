package com.sgp.sgp.model;

import jakarta.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

/*
    Entidad JPA que representa la tabla 'empleado' en la base de datos.
    Aquí se incluyen todos los campos, aunque no todos se envíen al frontend.
    El DTO (EmpleadoDto) se encarga de filtrar qué información viaja a la interfaz.
*/
@Entity
@Table(name = "empleado")
public class Empleado {

    // Identificador único del empleado (PK con auto_increment)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_Empleado") // coincide con la columna en MySQL
    private Long idEmpleado;

    // Nombre del empleado
    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    // Apellidos del empleado
    @Column(name = "apellidos", nullable = false, length = 100)
    private String apellidos;

    // Tipo de documento (CC, TI, Pasaporte, etc.)
    @Column(name = "tipo_documento", length = 45)
    private String tipoDocumento;

    // Número de documento
    @Column(name = "numero_documento", length = 20)
    private String numeroDocumento;

    // Fecha de nacimiento (se guarda en BD pero no se envía al DTO)
    @Column(name = "fecha_nacimiento")
    private Date fechaNacimiento;

    // Estado civil (se guarda en BD pero no se envía al DTO)
    @Column(name = "estado_civil", length = 20)
    private String estadoCivil;

    /*
        Relación con Contrato:
        - Un empleado puede tener varios contratos.
        - Usamos @JsonIgnore para evitar el error de LazyInitialization
          cuando devolvemos empleados en JSON.
        - CascadeType.ALL y orphanRemoval permiten que si se elimina
          un empleado, también se eliminen sus contratos asociados.
    */
    @OneToMany(mappedBy = "empleado", fetch = FetchType.LAZY,
               cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Contrato> contratos = new ArrayList<>();

    // --- Getters y Setters ---
    public Long getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(Long idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public List<Contrato> getContratos() {
        return contratos;
    }

    public void setContratos(List<Contrato> contratos) {
        this.contratos = contratos;
    }
}
