package com.sgp.sgp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;

@Entity
@Table(name = "reporte_nomina")
public class ReporteNomina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reporte")
    private Long idReporte;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empleado", nullable = false)
    @JsonIgnore
    private Empleado empleado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nomina")
    @JsonIgnore
    private Nomina nomina;

    @Column(name = "fecha_generacion", nullable = false)
    private Date fechaGeneracion;

    @Column(name = "total_salario", nullable = false)
    private BigDecimal totalSalario;

    @Column(name = "total_deducciones", nullable = false)
    private BigDecimal totalDeducciones;

    @Column(name = "total_bonificaciones", nullable = false)
    private BigDecimal totalBonificaciones;

    @Column(name = "total_pagado", nullable = false)
    private BigDecimal totalPagado;

    public ReporteNomina() {}

    public Long getIdReporte() { return idReporte; }
    public void setIdReporte(Long idReporte) { this.idReporte = idReporte; }

    public Empleado getEmpleado() { return empleado; }
    public void setEmpleado(Empleado empleado) { this.empleado = empleado; }

    public Nomina getNomina() { return nomina; }
    public void setNomina(Nomina nomina) { this.nomina = nomina; }

    public Date getFechaGeneracion() { return fechaGeneracion; }
    public void setFechaGeneracion(Date fechaGeneracion) { this.fechaGeneracion = fechaGeneracion; }

    public BigDecimal getTotalSalario() { return totalSalario; }
    public void setTotalSalario(BigDecimal totalSalario) { this.totalSalario = totalSalario; }

    public BigDecimal getTotalDeducciones() { return totalDeducciones; }
    public void setTotalDeducciones(BigDecimal totalDeducciones) { this.totalDeducciones = totalDeducciones; }

    public BigDecimal getTotalBonificaciones() { return totalBonificaciones; }
    public void setTotalBonificaciones(BigDecimal totalBonificaciones) { this.totalBonificaciones = totalBonificaciones; }

    public BigDecimal getTotalPagado() { return totalPagado; }
    public void setTotalPagado(BigDecimal totalPagado) { this.totalPagado = totalPagado; }
}
