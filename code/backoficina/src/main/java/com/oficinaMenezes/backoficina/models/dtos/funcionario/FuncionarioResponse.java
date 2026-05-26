package com.oficinaMenezes.backoficina.models.dtos.funcionario;

import java.math.BigDecimal;

public record FuncionarioResponse(
        String nome,
        BigDecimal totalGeradoMensal,
        Long totalServicoMensal
) {
}
