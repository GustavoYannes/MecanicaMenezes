package com.oficinaMenezes.backoficina.controllers;

import com.oficinaMenezes.backoficina.models.dtos.dashboard.DashboardEntradasResponse;
import com.oficinaMenezes.backoficina.models.dtos.dashboard.DashboardEvolucaoResponse;
import com.oficinaMenezes.backoficina.models.dtos.dashboard.DashboardResponse;
import com.oficinaMenezes.backoficina.models.dtos.dashboard.DashboardResumoResponse;
import com.oficinaMenezes.backoficina.services.DashboardService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DashboardControllerTest {

    @Mock
    private DashboardService dashboardService;

    @InjectMocks
    private DashboardController dashboardController;

    @Test
    void deveRetornarDashboardComStatusOk() {
        LocalDate inicio = LocalDate.of(2026, 1, 1);
        LocalDate fim = LocalDate.of(2026, 1, 31);
        DashboardResponse dashboard = new DashboardResponse(
                inicio,
                fim,
                new DashboardResumoResponse(5L, new BigDecimal("2.00")),
                new DashboardEntradasResponse(1L, 5L),
                new DashboardEvolucaoResponse(List.of(), List.of(), List.of())
        );

        when(dashboardService.buscarDashboard(inicio, fim)).thenReturn(dashboard);

        ResponseEntity<DashboardResponse> response = dashboardController.buscarDashboard(inicio, fim);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(dashboard, response.getBody());
        verify(dashboardService).buscarDashboard(inicio, fim);
    }
}
