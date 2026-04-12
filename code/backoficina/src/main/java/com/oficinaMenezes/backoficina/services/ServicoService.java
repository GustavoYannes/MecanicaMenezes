package com.oficinaMenezes.backoficina.services;

import com.oficinaMenezes.backoficina.models.dtos.servico.CreateServicoDTO;
import com.oficinaMenezes.backoficina.models.entities.Entrada;
import com.oficinaMenezes.backoficina.models.entities.Funcionario;
import com.oficinaMenezes.backoficina.models.entities.Servico;
import com.oficinaMenezes.backoficina.models.entities.enums.EStatusEntrada;
import com.oficinaMenezes.backoficina.models.exceptions.entrada.EntradaJaFinalizada;
import com.oficinaMenezes.backoficina.models.exceptions.entrada.EntradaNaoExisteException;
import com.oficinaMenezes.backoficina.models.exceptions.funcionario.FuncionarioNaoExiste;
import com.oficinaMenezes.backoficina.repositories.EntradaRepository;
import com.oficinaMenezes.backoficina.repositories.ServicoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class ServicoService {

    private ServicoRepository servicoRepository;
    private EntradaRepository entradaRepository;
    private FuncionarioService funcionarioService;
    private VeiculoService veiculoService;

    public ServicoService(ServicoRepository servicoRepository, EntradaRepository entradaRepository, FuncionarioService funcionarioService, VeiculoService veiculoService) {
        this.servicoRepository = servicoRepository;
        this.entradaRepository = entradaRepository;
        this.funcionarioService = funcionarioService;
        this.veiculoService = veiculoService;
    }

    public Servico criarServico(CreateServicoDTO data, UUID idFuncionario){
        Entrada entrada = entradaRepository.findById(data.idEntrada())
                .orElseThrow(EntradaNaoExisteException::new);
        if (entrada == null) {throw new EntradaNaoExisteException();}
        if (primeiroServicoEntrada(entrada)) {veiculoService.primeiroServico(entrada.getVeiculo());}
        if (entrada.getStatus() == EStatusEntrada.FECHADA) throw new EntradaJaFinalizada();


        Funcionario funcionario = funcionarioService.findByUUID(idFuncionario);
        if (funcionario == null) {throw new FuncionarioNaoExiste();}
        Servico newServico = new Servico(
                funcionario,
                entrada,
                data.nome(),
                data.quantidade(),
                BigDecimal.valueOf(data.valor())
        );
        return servicoRepository.save(newServico);
    }

    public Boolean primeiroServicoEntrada(Entrada entrada){
        return !servicoRepository.existsByEntrada(entrada);
    }

    public List<Servico> servicoPorEntrada(Long entradaId) {return servicoRepository.findByEntradaId(entradaId);}


}
