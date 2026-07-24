package com.oficinaMenezes.backoficina.models.dtos.dashboard;

import java.util.List;

public record DashboardEvolucaoResponse(
        List<DashboardEvolucaoItemResponse> diaria,
        List<DashboardEvolucaoItemResponse> semanal,
        List<DashboardEvolucaoItemResponse> mensal
) {
}
