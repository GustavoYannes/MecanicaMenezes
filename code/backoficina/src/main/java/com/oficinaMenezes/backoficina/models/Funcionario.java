package com.oficinaMenezes.backoficina.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.Table;

@Entity
@Table(name = "funcionario")
@Inheritance(strategy = jakarta.persistence.InheritanceType.JOINED)
public class Funcionario {
    @Id
    @GeneratedValue
    private String cpf;
    @Column(name = "nome", nullable = false)
    private String nome;
    @Column(name = "telefone", nullable = false)
    private String telefone;
    @Column(name = "senha", nullable = false)
    private String senha;
    
}
