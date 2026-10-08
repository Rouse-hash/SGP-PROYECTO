package com.sgp.sgp.controller;

import com.sgp.sgp.dto.DashboardResponse;
import com.sgp.sgp.service.DashboardService;
import com.sgp.sgp.service.SesionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;
    private final SesionService sesionService;

    public DashboardController(DashboardService dashboardService, SesionService sesionService) {
        this.dashboardService = dashboardService;
        this.sesionService = sesionService;
    }

    @GetMapping
    public ResponseEntity<DashboardResponse> obtenerDashboard(Authentication authentication) {
        if (!sesionService.esAdmin(authentication)) {
            Long miId = sesionService.idEmpleadoActual(authentication).orElse(null);
            return ResponseEntity.ok(dashboardService.obtenerDashboardEmpleado(miId));
        }
        return ResponseEntity.ok(dashboardService.obtenerDashboard());
    }
}
