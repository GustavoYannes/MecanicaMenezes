package com.oficinaMenezes.backoficina.models.dtos.veiculo;

import com.oficinaMenezes.backoficina.models.dtos.Cliente.ClienteResponse;

public record VeiculoResponse(
        String placa,
        String marca,
        String modelo,
        int ano,
        String cor,
        int km,
        ClienteResponse cliente
) {
}
