package com.oficinaMenezes.backoficina.models.dtos.pdf;

import com.oficinaMenezes.backoficina.models.entities.Servico;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class OrcamentoPDFDto {
        private String placa;
        private String modelo;
        private int ano;
        private int km;

        private String nomeCliente;

        private LocalDate dataEntrada;
        private LocalDate dataSaida;

        private List<Servico> servicos;
        private BigDecimal valorTotal;
}
