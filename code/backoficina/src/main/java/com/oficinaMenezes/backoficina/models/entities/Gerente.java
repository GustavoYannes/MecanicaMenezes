package com.oficinaMenezes.backoficina.models.entities;

import com.oficinaMenezes.backoficina.models.entities.enums.ERole;
import jakarta.persistence.Entity;

@Entity
public class Gerente extends Funcionario {


    public Gerente() {
        super();
    }

    public Gerente(String cpf, String nome, String telefone, String email, String senha) {
        super(cpf, nome, telefone, email, senha);
        this.role = ERole.GERENTE;
    }
}
