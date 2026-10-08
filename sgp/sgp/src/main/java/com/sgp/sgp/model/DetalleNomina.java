package com.sgp.sgp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;

@Entity
@Table(name = "detalle_nomina")
public class DetalleNomina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private Long idDetalle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nomina", nullable = false)
    @JsonIgnore
    private Nomina nomina;

    @Column(name = "tipo_concepto", nullable = false)
    private String tipoConcepto;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "valor", nullable = false)
    private BigDecimal valor;

    @Column(name = "estado", nullable = false)
    private String estado;

    @Column(name = "fecha")
    private Date fecha;

    public DetalleNomina() {}

    public Long getIdDetalle() { return idDetalle; }
    public void setIdDetalle(Long idDetalle) { this.idDetalle = idDetalle; }

    public Nomina getNomina() { return nomina; }
    public void setNomina(Nomina nomina) { this.nomina = nomina; }

    public String getTipoConcepto() { return tipoConcepto; }
    public void setTipoConcepto(String tipoConcepto) { this.tipoConcepto = tipoConcepto; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
}
