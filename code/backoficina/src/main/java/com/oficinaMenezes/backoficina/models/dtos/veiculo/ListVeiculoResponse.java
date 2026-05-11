package com.oficinaMenezes.backoficina.models.dtos.veiculo;

public record ListVeiculoResponse(
        String placa,
        String modelo,
        int ano,
        String cor,
        String status
) {
}
