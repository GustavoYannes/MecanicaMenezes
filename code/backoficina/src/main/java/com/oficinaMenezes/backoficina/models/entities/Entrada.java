package com.oficinaMenezes.backoficina.models.entities;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "entrada")
public class Entrada {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Veiculo veiculo;
    @Column(name = "data_entrada")
    private LocalDate dataEntrada;
    @Column(name = "data_saida")
    private LocalDate dataSaida;

    public Entrada() {
    }

    public Entrada(Veiculo veiculo){
        this.veiculo = veiculo;
        this.dataEntrada = LocalDate.now();
    }

    public Long getId() {
        return id;
    }

}
