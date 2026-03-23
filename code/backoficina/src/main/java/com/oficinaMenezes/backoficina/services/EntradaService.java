package com.oficinaMenezes.backoficina.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.oficinaMenezes.backoficina.models.dtos.entrada.CreateEntradaDTO;
import com.oficinaMenezes.backoficina.models.entities.Entrada;
import com.oficinaMenezes.backoficina.models.entities.Veiculo;
import com.oficinaMenezes.backoficina.models.entities.enums.EStatusVeiculo;
import com.oficinaMenezes.backoficina.models.exceptions.entrada.VeiculoEmAtendimentoException;
import com.oficinaMenezes.backoficina.repositories.EntradaRepository;
import com.oficinaMenezes.backoficina.repositories.VeiculoRepository;

@Service
public class EntradaService {
    private EntradaRepository entradaRepository;
    private VeiculoRepository veiculoRepository;
    private VeiculoService veiculoService;

    public EntradaService(EntradaRepository entradaRepository, VeiculoRepository veiculoRepository, VeiculoService veiculoService) {
        this.entradaRepository = entradaRepository;
        this.veiculoRepository = veiculoRepository;
        this.veiculoService = veiculoService;
    }

    public Entrada criarEntrada(CreateEntradaDTO data){
        Veiculo veiculoJaCriado = new Veiculo();
        Optional<Veiculo> veiculo = veiculoRepository.findById(data.placa());
        
        if(veiculoJaCriado.getStatus() != EStatusVeiculo.CONCLUIDO){
            throw new VeiculoEmAtendimentoException();
        }
        Entrada entrada = new Entrada(
            veiculoJaCriado
        );

        return entradaRepository.save(entrada);
    }
}
