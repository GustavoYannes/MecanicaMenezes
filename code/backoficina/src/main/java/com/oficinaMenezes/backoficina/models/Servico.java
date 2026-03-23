package com.oficinaMenezes.backoficina.models;

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
}
