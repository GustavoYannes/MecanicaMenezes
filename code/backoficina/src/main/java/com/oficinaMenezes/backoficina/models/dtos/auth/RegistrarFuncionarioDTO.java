package com.oficinaMenezes.backoficina.models.dtos.auth;

public record RegistrarFuncionarioDTO(
        String cpf,
        String senha,
        String nome,
        String email,
        String telefone
) {
}
