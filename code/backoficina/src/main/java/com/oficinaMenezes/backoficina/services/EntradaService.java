package com.oficinaMenezes.backoficina.services;

import com.oficinaMenezes.backoficina.models.dtos.pdf.OrcamentoPDFDto;
import com.oficinaMenezes.backoficina.models.entities.Orcamento;
import com.oficinaMenezes.backoficina.models.entities.Servico;
import com.oficinaMenezes.backoficina.models.entities.enums.EStatusEntrada;
import com.oficinaMenezes.backoficina.models.exceptions.entrada.EntradaJaFinalizada;
import com.oficinaMenezes.backoficina.models.exceptions.entrada.EntradaNaoExisteException;
import org.springframework.stereotype.Service;

import com.oficinaMenezes.backoficina.models.dtos.entrada.CreateEntradaDTO;
import com.oficinaMenezes.backoficina.models.entities.Entrada;
import com.oficinaMenezes.backoficina.models.entities.Veiculo;
import com.oficinaMenezes.backoficina.repositories.EntradaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class EntradaService {

    private EntradaRepository entradaRepository;
    private VeiculoService veiculoService;
    private PDFService pdfService;
    private ServicoService servicoService;
    private OrcamentoService orcamentoService;

    public EntradaService(EntradaRepository entradaRepository, VeiculoService veiculoService, PDFService pdfService, ServicoService servicoService, OrcamentoService orcamentoService) {
        this.entradaRepository = entradaRepository;
        this.veiculoService = veiculoService;
        this.pdfService = pdfService;
        this.servicoService = servicoService;
        this.orcamentoService = orcamentoService;
    }

    public Entrada criarEntrada(CreateEntradaDTO data){
        Veiculo veiculoJaCriado = veiculoService.buscarVeiculo(data);
        Entrada entrada = new Entrada(
            veiculoJaCriado
        );

        return entradaRepository.save(entrada);
    }

    public Entrada finalizarEntrada(Long entradaId){
        Optional<Entrada> entrada = entradaRepository.findById(entradaId);
        if (entrada.isEmpty()) throw new EntradaNaoExisteException();
        Entrada finalEntrada = entrada.get();

        if (finalEntrada.getStatus() == EStatusEntrada.FECHADA) throw new EntradaJaFinalizada();

        veiculoService.liberarVeiculo(finalEntrada.getVeiculo());
        finalEntrada.finalizarEntrada();
        return entradaRepository.save(finalEntrada);
    }

    public byte[] gerarPdf(Long entradaId){
        OrcamentoPDFDto orcamento = new OrcamentoPDFDto();
        Optional<Entrada> entrada = entradaRepository.findById(entradaId);
        if (entrada.isEmpty()) throw new EntradaNaoExisteException();
        if (entrada.get().getStatus() != EStatusEntrada.FECHADA) throw new EntradaNaoExisteException("O serviço ainda está em andamento. Finalize para gerar o PDF.");

        List<Servico> servicos = servicoService.servicoPorEntrada(entradaId);
        if (!servicos.isEmpty()) orcamento.setServicos(servicos);
        orcamento.setValorTotal(valorTotalEntrada(entradaId));
        entrada.get().gerarOrcamento(orcamento);
        orcamentoService.createOrcamento(orcamento, entrada.get());
        return pdfService.gerarOrcamentoPdf(orcamento);
    }

    public BigDecimal valorTotalEntrada(Long entradaId){
        List<Servico> servicos = servicoService.servicoPorEntrada(entradaId);
        BigDecimal valorTotal = BigDecimal.ZERO;
        for (Servico servico : servicos) {
            valorTotal = valorTotal.add(servico.valorTotal());
        }
        return valorTotal;
    }
}
