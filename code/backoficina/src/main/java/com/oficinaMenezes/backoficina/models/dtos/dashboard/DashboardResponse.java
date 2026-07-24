package com.oficinaMenezes.backoficina.models.dtos.dashboard;

import java.time.LocalDate;

public record DashboardResponse(
        LocalDate inicio,
        LocalDate fim,
        DashboardResumoResponse resumo,
        DashboardEntradasResponse entradas,
        DashboardEvolucaoResponse evolucao
) {
}
