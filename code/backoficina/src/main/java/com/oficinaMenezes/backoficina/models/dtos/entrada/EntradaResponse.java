package com.oficinaMenezes.backoficina.models.dtos.entrada;

import java.time.LocalDate;

public record EntradaResponse(
        Long id,
        String status,
        LocalDate dataEntrada,
        LocalDate dataSaida
) {
}
