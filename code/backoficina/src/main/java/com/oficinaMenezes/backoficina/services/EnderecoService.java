package com.oficinaMenezes.backoficina.services;

import org.springframework.stereotype.Service;

import com.oficinaMenezes.backoficina.dtos.CreateEntradaDTO;
import com.oficinaMenezes.backoficina.models.Endereco;
import com.oficinaMenezes.backoficina.repositories.EnderecoRepository;

@Service
public class EnderecoService {

    private EnderecoRepository enderecoRepository;
    public EnderecoService(EnderecoRepository enderecoRepository) {
        this.enderecoRepository = enderecoRepository;
    }

    public Endereco criarEndereco(CreateEntradaDTO data){
        Endereco endereco = new Endereco(   
            data.cidade(),
            data.bairro(),
            data.rua(),
            data.numero(),
            data.cep(), 
            data.estado()

        );
        return enderecoRepository.save(endereco);
    }
    
}
