package com.oficinaMenezes.backoficina.models.dtos.Cliente;

import java.math.BigDecimal;

public record ListClienteDTO(
        String nome,
        String CPF,
        BigDecimal totalGasto
) {
}
