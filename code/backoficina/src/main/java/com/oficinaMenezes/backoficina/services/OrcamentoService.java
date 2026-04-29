package com.oficinaMenezes.backoficina.services;

import com.oficinaMenezes.backoficina.models.dtos.pdf.OrcamentoPDFDto;
import com.oficinaMenezes.backoficina.models.entities.Entrada;
import com.oficinaMenezes.backoficina.models.entities.Orcamento;
import com.oficinaMenezes.backoficina.repositories.OrcamentoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OrcamentoService {

    private OrcamentoRepository orcamentoRepository;

    public OrcamentoService(OrcamentoRepository orcamentoRepository) {
        this.orcamentoRepository = orcamentoRepository;
    }

    public Orcamento createOrcamento(OrcamentoPDFDto data, Entrada entrada){
        Orcamento orcamentoExiste = orcamentoRepository.findByEntrada(entrada);
        if (orcamentoExiste != null) { return orcamentoExiste; }
        Orcamento orcamento = new Orcamento(
                data.getNomeCliente(),
                data.getCpfCliente(),
                entrada,
                data.getValorTotal(),
                data.getDataSaida()
        );
        return orcamentoRepository.save(orcamento);
    }

    public BigDecimal valorTotalPorCpf(String cpf){
        return orcamentoRepository.somarValorTotalPorCpf(cpf);
    }

}
