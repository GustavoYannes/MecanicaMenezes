package com.oficinaMenezes.backoficina.models.dtos.veiculo;

public record VeiculoResponse(
        String placa,
        String marca,
        String modelo,
        int ano,
        String cor,
        int km
) {
}
