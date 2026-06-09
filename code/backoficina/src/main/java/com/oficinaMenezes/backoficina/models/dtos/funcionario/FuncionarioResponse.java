package com.oficinaMenezes.backoficina.models.dtos.funcionario;

public record FuncionarioResponse(
        String nome,
        String cpf,
        String email,
        String telefone
) {
}
