package com.oficinaMenezes.backoficina.services;

import org.springframework.stereotype.Service;

import com.oficinaMenezes.backoficina.dtos.CreateEntradaDTO;
import com.oficinaMenezes.backoficina.models.Cliente;
import com.oficinaMenezes.backoficina.repositories.ClienteRepository;

@Service
public class ClienteService {
    private EnderecoService enderecoService;
    private ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository, EnderecoService enderecoService) {
        this.clienteRepository = clienteRepository;
        this.enderecoService = enderecoService;

    }

    public Cliente criarCliente(CreateEntradaDTO data){
        Cliente cliente = new Cliente(
            data.cpf(),
            data.nomeCliente(),
            data.telefone(),
            data.email(),
            enderecoService.criarEndereco(data)
            

        );
        return clienteRepository.save(cliente);
    }
    
}
