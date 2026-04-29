package com.oficinaMenezes.backoficina.models.entities;

import com.oficinaMenezes.backoficina.models.dtos.pdf.OrcamentoPDFDto;
import com.oficinaMenezes.backoficina.models.entities.enums.EStatusVeiculo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import jakarta.persistence.Table;

@Entity
@Table(name = "veiculo")
public class Veiculo {
    @Id
    @Column(name = "placa", nullable = false)
    private String placa;
    @Column(name = "modelo")
    private String modelo;
    @Column(name = "ano")
    private int ano;
    @Column(name = "cor")
    private String cor;
    @Column(name = "km")
    private int km;
    @ManyToOne
    private Cliente cliente;
    @Column(name = "status")
    @Enumerated(jakarta.persistence.EnumType.STRING)
    private EStatusVeiculo status;

    public Veiculo() {
    }

    public Veiculo(String placa, String modelo, int ano, String cor, int km, Cliente cliente) {
        this.placa = placa;
        this.modelo = modelo;
        this.ano = ano;
        this.cor = cor;
        this.km = km;
        this.status = EStatusVeiculo.ESPERA;
        this.cliente = cliente;
    }

    public EStatusVeiculo getStatus() {
        return status;
    }

    public EStatusVeiculo novaEntrada() {
        return this.status = EStatusVeiculo.ESPERA;
    }

    public EStatusVeiculo PrimeiroServico(){
        return this.status = EStatusVeiculo.EMPROGRESSO;
    }

    public EStatusVeiculo liberarVeiculo(){
        return this.status = EStatusVeiculo.CONCLUIDO;
    }

    public OrcamentoPDFDto gerarOrcamento(OrcamentoPDFDto orcamento) {
        orcamento.setPlaca(this.placa);
        orcamento.setModelo(this.modelo);
        orcamento.setAno(this.ano);
        orcamento.setKm(this.km);
        cliente.gerarOrcamentoPDF(orcamento);
        return orcamento;
    }

}
