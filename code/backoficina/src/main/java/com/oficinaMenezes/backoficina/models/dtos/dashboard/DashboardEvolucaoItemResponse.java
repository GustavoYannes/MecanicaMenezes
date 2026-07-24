package com.oficinaMenezes.backoficina.models.dtos.dashboard;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DashboardEvolucaoItemResponse(
        String periodo,
        LocalDate inicio,
        LocalDate fim,
        int quantidadeServicos,
        BigDecimal faturamento
) {
}
