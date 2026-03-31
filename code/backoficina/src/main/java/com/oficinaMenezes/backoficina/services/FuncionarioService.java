package com.oficinaMenezes.backoficina.services;

import com.oficinaMenezes.backoficina.models.entities.Funcionario;
import com.oficinaMenezes.backoficina.repositories.FuncionarioRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FuncionarioService {

    private FuncionarioRepository funcionarioRepository;

    public FuncionarioService(FuncionarioRepository funcionarioRepository) {
        this.funcionarioRepository = funcionarioRepository;
    }

    public Funcionario findByUUID(UUID id) {
        return funcionarioRepository.findById(id).orElse(null);
    }

}
