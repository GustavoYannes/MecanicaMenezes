package com.oficinaMenezes.backoficina.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "cliente")
public class Cliente {
    @Id
    @Column(name = "cpf", nullable = false)
    private String cpf;
    @Column(name = "nome_completo", nullable = false)
    private String nomeCompleto;
    @Column(name = "telefone")
    private String telefone;
    @Column(name = "email")
    private String email;
    @ManyToOne
    private Endereco endereco;

    public Cliente() {
    }   

    public Cliente (String cpf, String nomeCompleto, String telefone, String email, Endereco endereco){
        this.cpf = cpf;
        this.nomeCompleto = nomeCompleto;
        this.telefone = telefone;
        this.email = email;
        this.endereco = endereco;
    }

}
