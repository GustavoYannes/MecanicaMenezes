package com.oficinaMenezes.backoficina.services;

import org.springframework.stereotype.Service;

import com.oficinaMenezes.backoficina.models.dtos.entrada.CreateEntradaDTO;
import com.oficinaMenezes.backoficina.models.entities.Entrada;
import com.oficinaMenezes.backoficina.models.entities.Veiculo;
import com.oficinaMenezes.backoficina.repositories.EntradaRepository;

import java.util.Optional;

@Service
public class EntradaService {

    private EntradaRepository entradaRepository;
    private VeiculoService veiculoService;

    public EntradaService(EntradaRepository entradaRepository, VeiculoService veiculoService) {
        this.entradaRepository = entradaRepository;
        this.veiculoService = veiculoService;
    }

    public Entrada criarEntrada(CreateEntradaDTO data){
        Veiculo veiculoJaCriado = veiculoService.buscarVeiculo(data);
        Entrada entrada = new Entrada(
            veiculoJaCriado
        );

        return entradaRepository.save(entrada);
    }

    public Entrada entradaExiste(Long entradaId){
        Optional<Entrada> entrada = entradaRepository.findById(entradaId);
        return entrada.orElse(null);
    }
}
