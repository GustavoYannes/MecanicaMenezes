package com.oficinaMenezes.backoficina.models.dtos.funcionario;

import java.util.UUID;

public record FuncionarioListResponse(
        UUID id,
        String nome
) {
}
