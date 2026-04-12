package com.oficinaMenezes.backoficina.models.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import jakarta.persistence.Table;

@Entity
@Table(name = "servico")
public class Servico {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "data", nullable = false)
    private LocalDate data;
    @Column(name = "nome", nullable = false)
    private String nome;
    @Column(name = "quantidade", nullable = false)
    private int quantidade;
    @Column(name = "valor", nullable = false)
    private BigDecimal valor;
    @ManyToOne
    private Funcionario funcionario;
    @ManyToOne
    private Entrada entrada;

    public Servico(){}

    public Servico(Funcionario funcionario, Entrada entrada, String nome, int quantidade, BigDecimal valor) {
        this.funcionario = funcionario;
        this.entrada = entrada;
        this.nome = nome;
        this.quantidade = quantidade;
        this.valor = valor;
        this.data = LocalDate.now();
    }

    public Long getId() {return id;}
    public int getQuantidade(){return quantidade;}
    public BigDecimal getValorUnidade() {return valor;}
    public String getNome() {return nome;}
    public BigDecimal valorTotal() {return valor.multiply(BigDecimal.valueOf(quantidade));}
}
