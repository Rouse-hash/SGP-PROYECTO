package com.sgp.sgp.dto;

public class LoginResponse {

    private String token;
    private String correo;
    private String rol;
    private String mensaje;

    public LoginResponse() {}

    public LoginResponse(String token, String correo, String rol, String mensaje) {
        this.token = token;
        this.correo = correo;
        this.rol = rol;
        this.mensaje = mensaje;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
