package com.sgp.sgp.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "archivo_empleado")
public class ArchivoEmpleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_empleado", nullable = false)
    private Long idEmpleado;

    @Column(name = "nombre_original", nullable = false, length = 255)
    private String nombreOriginal;

    @Column(name = "nombre_archivo", nullable = false, length = 255)
    private String nombreArchivo;

    @Column(name = "tipo", nullable = false, length = 20)
    private String tipo;

    @Column(name = "ruta", nullable = false, length = 500)
    private String ruta;

    @Column(name = "mime_type", length = 100)
    private String mimeType;

    @Column(name = "tamano")
    private Long tamano;

    @Column(name = "fecha_subida", nullable = false)
    private LocalDateTime fechaSubida;

    public ArchivoEmpleado() {}

    public ArchivoEmpleado(Long idEmpleado, String nombreOriginal, String nombreArchivo,
                           String tipo, String ruta, String mimeType, Long tamano) {
        this.idEmpleado = idEmpleado;
        this.nombreOriginal = nombreOriginal;
        this.nombreArchivo = nombreArchivo;
        this.tipo = tipo;
        this.ruta = ruta;
        this.mimeType = mimeType;
        this.tamano = tamano;
        this.fechaSubida = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(Long idEmpleado) { this.idEmpleado = idEmpleado; }

    public String getNombreOriginal() { return nombreOriginal; }
    public void setNombreOriginal(String nombreOriginal) { this.nombreOriginal = nombreOriginal; }

    public String getNombreArchivo() { return nombreArchivo; }
    public void setNombreArchivo(String nombreArchivo) { this.nombreArchivo = nombreArchivo; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getRuta() { return ruta; }
    public void setRuta(String ruta) { this.ruta = ruta; }

    public String getMimeType() { return mimeType; }
    public void setMimeType(String mimeType) { this.mimeType = mimeType; }

    public Long getTamano() { return tamano; }
    public void setTamano(Long tamano) { this.tamano = tamano; }

    public LocalDateTime getFechaSubida() { return fechaSubida; }
    public void setFechaSubida(LocalDateTime fechaSubida) { this.fechaSubida = fechaSubida; }
}
