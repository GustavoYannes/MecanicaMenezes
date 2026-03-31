package com.oficinaMenezes.backoficina.models.entities;

import java.time.LocalDate;

import com.oficinaMenezes.backoficina.models.entities.enums.EStatusEntrada;
import jakarta.persistence.*;

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
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private EStatusEntrada status;

    public Entrada() {
    }

    public Entrada(Veiculo veiculo){
        this.veiculo = veiculo;
        this.dataEntrada = LocalDate.now();
        this.status = EStatusEntrada.ABERTA;
    }

    public Long getId() {
        return id;
    }
    public Veiculo getVeiculo() {return veiculo;}

    public EStatusEntrada getStatus() {return status;}

    public void finalizarEntrada(){
        this.status = EStatusEntrada.FECHADA;
        this.dataSaida = LocalDate.now();
    }

}
