package com.sgp.sgp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "nomina")
public class Nomina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_nomina")
    private Long idNomina;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empleado", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Empleado empleado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_contrato", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Contrato contrato;

    @Column(name = "fecha_pago", nullable = false)
    private Date fechaPago;

    @Column(name = "salario_base", nullable = false)
    private Double salarioBase;

    @Column(name = "deducciones")
    private Double deducciones;

    @Column(name = "bonificaciones")
    private Double bonificaciones;

    @Column(name = "total_pagado", nullable = false)
    private Double totalPagado;

    @Column(name = "estado", length = 20)
    private String estado; // Ej: "Pagado", "Pendiente"

    // Campos para API-Colombia
    @Column(name = "departamento", nullable = false)
    private String departamento;

    @Column(name = "municipio", nullable = false)
    private String municipio;

    public Nomina() {}

    public Nomina(Long idNomina, Empleado empleado, Contrato contrato, Date fechaPago,
                  Double salarioBase, Double deducciones, Double bonificaciones,
                  Double totalPagado, String estado, String departamento, String municipio) {
        this.idNomina = idNomina;
        this.empleado = empleado;
        this.contrato = contrato;
        this.fechaPago = fechaPago;
        this.salarioBase = salarioBase;
        this.deducciones = deducciones;
        this.bonificaciones = bonificaciones;
        this.totalPagado = totalPagado;
        this.estado = estado;
        this.departamento = departamento;
        this.municipio = municipio;
    }

    // --- Getters y Setters ---
    public Long getIdNomina() { return idNomina; }
    public void setIdNomina(Long idNomina) { this.idNomina = idNomina; }

    public Empleado getEmpleado() { return empleado; }
    public void setEmpleado(Empleado empleado) { this.empleado = empleado; }

    public Contrato getContrato() { return contrato; }
    public void setContrato(Contrato contrato) { this.contrato = contrato; }

    public Date getFechaPago() { return fechaPago; }
    public void setFechaPago(Date fechaPago) { this.fechaPago = fechaPago; }

    public Double getSalarioBase() { return salarioBase; }
    public void setSalarioBase(Double salarioBase) { this.salarioBase = salarioBase; }

    public Double getDeducciones() { return deducciones; }
    public void setDeducciones(Double deducciones) { this.deducciones = deducciones; }

    public Double getBonificaciones() { return bonificaciones; }
    public void setBonificaciones(Double bonificaciones) { this.bonificaciones = bonificaciones; }

    public Double getTotalPagado() { return totalPagado; }
    public void setTotalPagado(Double totalPagado) { this.totalPagado = totalPagado; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getDepartamento() { return departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }

    public String getMunicipio() { return municipio; }
    public void setMunicipio(String municipio) { this.municipio = municipio; }
}

