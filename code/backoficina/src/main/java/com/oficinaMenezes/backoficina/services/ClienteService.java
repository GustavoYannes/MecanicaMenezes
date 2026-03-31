package com.oficinaMenezes.backoficina.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.oficinaMenezes.backoficina.models.dtos.entrada.CreateEntradaDTO;
import com.oficinaMenezes.backoficina.models.entities.Cliente;
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

    public Cliente buscarCliente(CreateEntradaDTO data){
       Optional<Cliente> cliente = clienteRepository.findById(data.cpf());
       if(cliente.isEmpty()){
        return criarCliente(data);
       }
       return cliente.get();
    }
    
}
