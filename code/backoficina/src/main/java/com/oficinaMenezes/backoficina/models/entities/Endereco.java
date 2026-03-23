package com.oficinaMenezes.backoficina.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "endereco")
public class Endereco {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private long id;
    @Column(name = "cidade", nullable = false)
    private String cidade;
    @Column(name = "bairro", nullable = false)
    private String bairro;
    @Column(name = "rua", nullable = false)
    private String rua;
    @Column(name = "numero", nullable = false)
    private String numero;
    @Column(name = "cep")
    private int cep;
    @Column(name = "estado", nullable = false)
    private String estado;

    public Endereco(){}

    public Endereco(String cidade, String bairro, String rua, String numero, int cep, String estado) {
        this.cidade = cidade;
        this.bairro = bairro;
        this.rua = rua;
        this.numero = numero;
        this.cep = cep;
        this.estado = estado;
    }

}
