package com.oficinaMenezes.backoficina.models.dtos.servico;

import java.math.BigDecimal;

public record ServicoTotalDataResponse(
        int qtdServico,
        BigDecimal totalGerado
) {
}
