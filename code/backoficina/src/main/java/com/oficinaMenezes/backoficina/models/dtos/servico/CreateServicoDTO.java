package com.oficinaMenezes.backoficina.models.dtos.servico;

public record CreateServicoDTO(
        String nome,
        int quantidade,
        double valor,
        String cpfFuncionario,
        Long idEntrada
) {
}
