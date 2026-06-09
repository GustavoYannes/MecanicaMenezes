package com.oficinaMenezes.backoficina.models.entities;

import com.oficinaMenezes.backoficina.models.entities.enums.ERole;
import jakarta.persistence.Entity;

@Entity
public class Mecanico extends Funcionario {

    public Mecanico() {
        super();
    }

    public Mecanico(String cpf, String nome, String telefone, String email, String senha) {
        super(cpf, nome, telefone, email, senha);
        this.role = ERole.MECANICO;
    }
    
}
