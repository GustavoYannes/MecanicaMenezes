package com.oficinaMenezes.backoficina.models.dtos.Cliente;

import com.oficinaMenezes.backoficina.models.dtos.endereco.EnderecoResponse;

public record ClienteResponse(
        String nomeCompleto,
        String email,
        String cpf,
        String telefone,
        EnderecoResponse endereco
) {
}
