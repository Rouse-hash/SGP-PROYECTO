package com.sgp.sgp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empleado", unique = true)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Empleado empleado;

    @Email
    @NotBlank
    @Column(unique = true)
    private String correo;

    // Se acepta en las peticiones entrantes pero nunca se envía en las respuestas
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotBlank
    @Pattern(regexp = "^(ADMIN|EMPLEADO)$", message = "El rol debe ser ADMIN o EMPLEADO")
    private String rol;

    private Boolean activo = true;

    public Usuario() {}

    public Usuario(Long id, Empleado empleado, String correo, String password, String rol, Boolean activo) {
        this.id = id;
        this.empleado = empleado;
        this.correo = correo;
        this.password = password;
        this.rol = rol;
        this.activo = activo;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Empleado getEmpleado() { return empleado; }
    public void setEmpleado(Empleado empleado) { this.empleado = empleado; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}
