package com.oficinaMenezes.backoficina.models.dtos.servico;

public record EditarServicoDTO(
        String nome,
        int quantidade,
        double valor
) {
}
