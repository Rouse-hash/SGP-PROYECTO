package com.sgp.sgp.dto;

import com.sgp.sgp.model.ArchivoEmpleado;
import com.sgp.sgp.model.Contrato;
import com.sgp.sgp.model.Empleado;

import java.util.List;

public class ResumenEmpleadoDTO {

    private Empleado empleado;
    private List<Contrato> contratos;
    private List<ArchivoEmpleado> archivos;

    public ResumenEmpleadoDTO() {}

    public ResumenEmpleadoDTO(Empleado empleado, List<Contrato> contratos, List<ArchivoEmpleado> archivos) {
        this.empleado = empleado;
        this.contratos = contratos;
        this.archivos = archivos;
    }

    public Empleado getEmpleado() { return empleado; }
    public void setEmpleado(Empleado empleado) { this.empleado = empleado; }

    public List<Contrato> getContratos() { return contratos; }
    public void setContratos(List<Contrato> contratos) { this.contratos = contratos; }

    public List<ArchivoEmpleado> getArchivos() { return archivos; }
    public void setArchivos(List<ArchivoEmpleado> archivos) { this.archivos = archivos; }
}
