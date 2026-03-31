package com.oficinaMenezes.backoficina.services;

import com.oficinaMenezes.backoficina.models.dtos.servico.CreateServicoDTO;
import com.oficinaMenezes.backoficina.models.entities.Entrada;
import com.oficinaMenezes.backoficina.models.entities.Funcionario;
import com.oficinaMenezes.backoficina.models.entities.Servico;
import com.oficinaMenezes.backoficina.models.exceptions.entrada.EntradaNaoExisteException;
import com.oficinaMenezes.backoficina.models.exceptions.funcionario.FuncionarioNaoExiste;
import com.oficinaMenezes.backoficina.repositories.ServicoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ServicoService {

    private ServicoRepository servicoRepository;
    private EntradaService entradaService;
    private FuncionarioService funcionarioService;
    private VeiculoService veiculoService;

    public ServicoService(ServicoRepository servicoRepository, EntradaService entradaService, FuncionarioService funcionarioService, VeiculoService veiculoService) {
        this.servicoRepository = servicoRepository;
        this.entradaService = entradaService;
        this.funcionarioService = funcionarioService;
        this.veiculoService = veiculoService;
    }

    public Servico criarServico(CreateServicoDTO data){
        Entrada entrada = entradaService.entradaExiste(data.idEntrada());
        if (entrada == null) {throw new EntradaNaoExisteException();}
        if (primeiroServicoEntrada(entrada)) {veiculoService.primeiroServico(entrada.getVeiculo());}


        Funcionario funcionario = funcionarioService.findByCpf(data.cpfFuncionario());
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


}
