package com.oficinaMenezes.backoficina.models.entities.enums;

public enum EStatusVeiculo {

    ESPERA("EM_ESPERA"),
    EMPROGRESSO("EMPROGRESSO"),
    CONCLUIDO("CONCLUIDO");

    private String status;

    EStatusVeiculo(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

}
