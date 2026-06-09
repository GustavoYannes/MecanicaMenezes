package com.oficinaMenezes.backoficina.repositories;

import com.oficinaMenezes.backoficina.models.entities.Funcionario;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.security.core.userdetails.UserDetails;

public interface FuncionarioRepository extends JpaRepository<Funcionario, UUID>, JpaSpecificationExecutor<Funcionario> {

    Funcionario findByCpf(String cpf);
    UserDetails findByUuid(UUID id);

}
