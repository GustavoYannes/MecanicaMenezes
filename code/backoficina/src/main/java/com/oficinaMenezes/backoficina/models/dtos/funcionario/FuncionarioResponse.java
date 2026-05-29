package com.oficinaMenezes.backoficina.models.dtos.funcionario;

import com.oficinaMenezes.backoficina.models.dtos.servico.ServicoResponse;

import java.util.List;

public record FuncionarioResponse(
        String nome,
        String cpf,
        String email,
        List<ServicoResponse> servicosDoMes
) {
}
