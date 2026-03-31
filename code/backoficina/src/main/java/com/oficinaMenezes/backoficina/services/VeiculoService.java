package com.oficinaMenezes.backoficina.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.oficinaMenezes.backoficina.models.dtos.entrada.CreateEntradaDTO;
import com.oficinaMenezes.backoficina.models.entities.Cliente;
import com.oficinaMenezes.backoficina.models.entities.Veiculo;
import com.oficinaMenezes.backoficina.models.entities.enums.EStatusVeiculo;
import com.oficinaMenezes.backoficina.models.exceptions.entrada.VeiculoEmAtendimentoException;

import com.oficinaMenezes.backoficina.repositories.VeiculoRepository;

@Service
public class VeiculoService {
    private ClienteService clienteService;
    private VeiculoRepository veiculoRepository;
   

    public VeiculoService(VeiculoRepository veiculoRepository,  
            ClienteService clienteService) {
        this.veiculoRepository = veiculoRepository;
        this.clienteService = clienteService;
    }

    public Veiculo verificarVeiculo(Veiculo veiculo) {
        if(veiculo.getStatus() == EStatusVeiculo.CONCLUIDO){
          veiculo.novaEntrada();
        }
        if(veiculo.getStatus() == EStatusVeiculo.EM_PROGRESSO){
            throw new VeiculoEmAtendimentoException();
        }
        if(veiculo.getStatus() == EStatusVeiculo.ESPERA){
            throw new VeiculoEmAtendimentoException();
        }

        return veiculoRepository.save(veiculo);
        
    }

    public Veiculo buscarVeiculo(CreateEntradaDTO data) {
        Optional<Veiculo> veiculo = veiculoRepository.findById(data.placa());
        if (veiculo.isEmpty()) {
            return criarVeiculo(data);
        }
        return verificarVeiculo(veiculo.get());
    }

    public Veiculo criarVeiculo(CreateEntradaDTO data) {
       Cliente cliente = clienteService.buscarCliente(data);
       
        Veiculo veiculo = new Veiculo(
                data.placa(),
                data.modelo(),
                data.ano(),
                data.cor(),
                data.km(),
                cliente

        );
        return veiculoRepository.save(veiculo);
    }

    public EStatusVeiculo primeiroServico(Veiculo veiculo){
        veiculo.PrimeiroServico();
        veiculoRepository.save(veiculo);
        return veiculo.getStatus();
    }
}
