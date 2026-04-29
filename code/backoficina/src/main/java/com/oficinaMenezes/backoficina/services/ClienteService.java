package com.oficinaMenezes.backoficina.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.oficinaMenezes.backoficina.models.dtos.Cliente.ClienteResponse;
import com.oficinaMenezes.backoficina.models.dtos.Cliente.ListClienteDTO;
import com.oficinaMenezes.backoficina.models.specifications.ClienteSpec;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.oficinaMenezes.backoficina.models.dtos.entrada.CreateEntradaDTO;
import com.oficinaMenezes.backoficina.models.entities.Cliente;
import com.oficinaMenezes.backoficina.repositories.ClienteRepository;

@Service
public class ClienteService {

    private OrcamentoService orcamentoService;
    private EnderecoService enderecoService;
    private ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository, EnderecoService enderecoService, OrcamentoService orcamentoService) {
        this.clienteRepository = clienteRepository;
        this.enderecoService = enderecoService;
        this.orcamentoService = orcamentoService;
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

    public Page<ListClienteDTO> buscarTodosClientes(String nomeCompleto, int page) {
        Pageable pageable = PageRequest.of(page, 5, Sort.by("nomeCompleto").ascending());

        Page<Cliente> clientesPage = clienteRepository.findAll(
                ClienteSpec.nomeCompletoContains(nomeCompleto),
                pageable
        );

        return clientesPage.map(cliente -> {
            BigDecimal valorTotalGasto = orcamentoService.valorTotalPorCpf(cliente.getCpf());

            return cliente.listClientes(valorTotalGasto);
        });
    }
    
}
