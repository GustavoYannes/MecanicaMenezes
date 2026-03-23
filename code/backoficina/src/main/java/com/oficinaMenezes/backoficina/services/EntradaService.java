package com.oficinaMenezes.backoficina.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.oficinaMenezes.backoficina.dtos.CreateEntradaDTO;
import com.oficinaMenezes.backoficina.models.Entrada;
import com.oficinaMenezes.backoficina.models.Veiculo;
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
        Veiculo veiculoVerificado = new Veiculo();
        Optional<Veiculo> veiculo = veiculoRepository.findById(data.placa());
        if(veiculo.isEmpty()){
            veiculoVerificado = veiculoService.criarVeiculo(data);
        }
        Entrada entrada = new Entrada(
            veiculoVerificado
        );

        return entradaRepository.save(entrada);
    }
}
