package com.oficinaMenezes.backoficina.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
@Entity
@Table(name = "orcamento")
public class Orcamento {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "nome_cliente", nullable = false)
    private String nomeCliente;
    @Column(name = "nome_gerente", nullable = false)
    private  String nomeGerente;
    @OneToOne
    private  Entrada entrada;
    @Column(name = "valor_total", nullable = false)
    private  BigDecimal valorTotal;
    @Column(name = "data_geracao", nullable = false)
    private  LocalDateTime dataGeracao;
    
}
