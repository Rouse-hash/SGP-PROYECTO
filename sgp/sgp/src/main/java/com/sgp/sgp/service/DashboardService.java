package com.sgp.sgp.service;

import com.sgp.sgp.dto.DashboardResponse;

public interface DashboardService {
    DashboardResponse obtenerDashboard();

    DashboardResponse obtenerDashboardEmpleado(Long idEmpleado);
}
