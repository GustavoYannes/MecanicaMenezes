package com.oficinaMenezes.backoficina.models.dtos.servico;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ServicoResponse(
        String nome,
        LocalDate data,
        int quantidade,
        BigDecimal valor,
        BigDecimal valorTotal

) {
}
