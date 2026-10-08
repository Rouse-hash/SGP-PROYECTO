package com.sgp.sgp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Contrato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idContrato;

    private String tipoContrato;

    // Usamos LocalDate para fechas
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    private Double salario;

    // Relación con empleado
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empleado")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Empleado empleado;

    // Getters y setters
    public Long getIdContrato() { return idContrato; }
    public void setIdContrato(Long idContrato) { this.idContrato = idContrato; }

    public String getTipoContrato() { return tipoContrato; }
    public void setTipoContrato(String tipoContrato) { this.tipoContrato = tipoContrato; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public Double getSalario() { return salario; }
    public void setSalario(Double salario) { this.salario = salario; }

    public Empleado getEmpleado() { return empleado; }
    public void setEmpleado(Empleado empleado) { this.empleado = empleado; }
}




