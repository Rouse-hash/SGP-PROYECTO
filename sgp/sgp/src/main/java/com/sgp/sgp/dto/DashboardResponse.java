package com.sgp.sgp.dto;

import com.sgp.sgp.model.Contrato;
import com.sgp.sgp.model.Empleado;

import java.util.List;

public class DashboardResponse {

    private long totalEmpleados;
    private long totalContratos;
    private long totalUsuarios;
    private long totalNominas;
    private double resumenNomina;
    private List<Empleado> ultimosEmpleados;
    private List<Contrato> ultimosContratos;

    public DashboardResponse() {}

    public DashboardResponse(long totalEmpleados, long totalContratos, long totalUsuarios,
                             long totalNominas, double resumenNomina,
                             List<Empleado> ultimosEmpleados, List<Contrato> ultimosContratos) {
        this.totalEmpleados = totalEmpleados;
        this.totalContratos = totalContratos;
        this.totalUsuarios = totalUsuarios;
        this.totalNominas = totalNominas;
        this.resumenNomina = resumenNomina;
        this.ultimosEmpleados = ultimosEmpleados;
        this.ultimosContratos = ultimosContratos;
    }

    public long getTotalEmpleados() { return totalEmpleados; }
    public void setTotalEmpleados(long totalEmpleados) { this.totalEmpleados = totalEmpleados; }

    public long getTotalContratos() { return totalContratos; }
    public void setTotalContratos(long totalContratos) { this.totalContratos = totalContratos; }

    public long getTotalUsuarios() { return totalUsuarios; }
    public void setTotalUsuarios(long totalUsuarios) { this.totalUsuarios = totalUsuarios; }

    public long getTotalNominas() { return totalNominas; }
    public void setTotalNominas(long totalNominas) { this.totalNominas = totalNominas; }

    public double getResumenNomina() { return resumenNomina; }
    public void setResumenNomina(double resumenNomina) { this.resumenNomina = resumenNomina; }

    public List<Empleado> getUltimosEmpleados() { return ultimosEmpleados; }
    public void setUltimosEmpleados(List<Empleado> ultimosEmpleados) { this.ultimosEmpleados = ultimosEmpleados; }

    public List<Contrato> getUltimosContratos() { return ultimosContratos; }
    public void setUltimosContratos(List<Contrato> ultimosContratos) { this.ultimosContratos = ultimosContratos; }
}
