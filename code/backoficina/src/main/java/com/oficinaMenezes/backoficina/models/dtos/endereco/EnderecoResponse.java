package com.oficinaMenezes.backoficina.models.dtos.endereco;

public record EnderecoResponse(
        String cidade,
        String bairro,
        String rua,
        String numero,
        int cep,
        String estado
) {
}
