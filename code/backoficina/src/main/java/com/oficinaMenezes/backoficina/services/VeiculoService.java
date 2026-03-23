package com.oficinaMenezes.backoficina.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.oficinaMenezes.backoficina.dtos.CreateEntradaDTO;
import com.oficinaMenezes.backoficina.models.Cliente;
import com.oficinaMenezes.backoficina.models.Veiculo;
import com.oficinaMenezes.backoficina.repositories.ClienteRepository;
import com.oficinaMenezes.backoficina.repositories.VeiculoRepository;

@Service
public class VeiculoService {
    private ClienteService clienteService;
    private VeiculoRepository veiculoRepository;
    private ClienteRepository clienteRepository; 

    public VeiculoService(VeiculoRepository veiculoRepository, ClienteRepository clienteRepository, ClienteService clienteService) {
        this.veiculoRepository = veiculoRepository;
        this.clienteRepository = clienteRepository;
        this.clienteService = clienteService;
    }
    
    public Veiculo criarVeiculo(CreateEntradaDTO data){
        Cliente clienteVerificado = new Cliente();
        Optional<Cliente> cliente = clienteRepository.findById(data.cpf());
        if(cliente.isEmpty()){
            clienteVerificado=clienteService.criarCliente(data);
        }
          clienteVerificado = cliente.get();
        Veiculo veiculo = new Veiculo(
            data.placa(),
            data.modelo(),
            data.ano(),
            data.cor(),
            data.km(),
            clienteVerificado
            
        );
        return veiculoRepository.save(veiculo);
    }
}
