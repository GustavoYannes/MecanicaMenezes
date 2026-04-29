package com.oficinaMenezes.backoficina.models.dtos.pdf;

import com.oficinaMenezes.backoficina.models.entities.Servico;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class OrcamentoPDFDto {
        private String placa;
        private String modelo;
        private int ano;
        private int km;

        private String nomeCliente;
        private String cpfCliente;

        private LocalDate dataEntrada;
        private LocalDate dataSaida;

        private List<Servico> servicos;
        private BigDecimal valorTotal;

        public OrcamentoPDFDto() {}


        public String getPlaca() { return placa; }
        public void setPlaca(String placa) { this.placa = placa; }

        public String getCpfCliente() { return cpfCliente; }
        public void setCpfCliente(String cpfCliente) { this.cpfCliente = cpfCliente; }

        public String getModelo() { return modelo; }
        public void setModelo(String modelo) { this.modelo = modelo; }

        public int getAno() { return ano; }
        public void setAno(int ano) { this.ano = ano; }

        public int getKm() { return km; }
        public void setKm(int km) { this.km = km; }

        public String getNomeCliente() { return nomeCliente; }
        public void setNomeCliente(String nomeCliente) { this.nomeCliente = nomeCliente; }

        public LocalDate getDataEntrada() { return dataEntrada; }
        public void setDataEntrada(LocalDate dataEntrada) { this.dataEntrada = dataEntrada; }

        public LocalDate getDataSaida() { return dataSaida; }
        public void setDataSaida(LocalDate dataSaida) { this.dataSaida = dataSaida; }

        public List<Servico> getServicos() { return servicos; }
        public void setServicos(List<Servico> servicos) { this.servicos = servicos; }

        public BigDecimal getValorTotal() { return valorTotal; }
        public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }
}

