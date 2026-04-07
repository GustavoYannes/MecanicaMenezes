package com.oficinaMenezes.backoficina.models.entities.enums;

public enum EStatusEntrada {

    ABERTA("ABERTA"),
    FECHADA("FECHADA");

    private String status;

    EStatusEntrada(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

}
