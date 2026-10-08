package com.sgp.sgp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RegisterRequest {

    @NotNull
    private Long idEmpleado;

    @NotBlank
    private String correo;

    @NotBlank
    private String password;

    @NotBlank
    private String rol;

    public RegisterRequest() {}

    public RegisterRequest(Long idEmpleado, String correo, String password, String rol) {
        this.idEmpleado = idEmpleado;
        this.correo = correo;
        this.password = password;
        this.rol = rol;
    }

    public Long getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(Long idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
