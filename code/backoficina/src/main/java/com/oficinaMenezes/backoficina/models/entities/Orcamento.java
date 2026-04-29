package com.oficinaMenezes.backoficina.models.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

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
    @Column(name = "cpf_cliente", nullable = false)
    private String cpfCliente;
    @OneToOne
    private Entrada entrada;
    @Column(name = "valor_total", nullable = false)
    private  BigDecimal valorTotal;
    @Column(name = "data_geracao", nullable = false)
    private LocalDate dataGeracao;

    public Orcamento(){}

    public Orcamento(
            String nomeCliente,
            String cpfCliente,
            Entrada entrada,
            BigDecimal valorTotal,
            LocalDate dataGeracao
    ) {
        this.nomeCliente = nomeCliente;
        this.cpfCliente = cpfCliente;
        this.entrada = entrada;
        this.valorTotal = valorTotal;
        this.dataGeracao = dataGeracao;
    }
    
}
