package com.oficinaMenezes.backoficina.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

    public Veiculo() {
    }

    public Veiculo(String placa, String modelo, int ano, String cor, int km, Cliente cliente) {
        this.placa = placa;
        this.modelo = modelo;
        this.ano = ano;
        this.cor = cor;
        this.km = km;
        this.cliente = cliente;
    }

}
