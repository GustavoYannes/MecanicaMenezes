package com.oficinaMenezes.backoficina.models.dtos.dashboard;

import java.math.BigDecimal;

public record DashboardResumoResponse(
        long quantidadeCarrosAtendidos,
        BigDecimal tempoMedioPermanenciaDias
) {
}
