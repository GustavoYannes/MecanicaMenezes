package com.oficinaMenezes.backoficina.repositories;

import com.oficinaMenezes.backoficina.models.entities.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface FuncionarioRepository extends JpaRepository<Funcionario, String> {

    UserDetails findByCpf(String cpf);

}
