package com.oficinaMenezes.backoficina.models.entities.enums;

public enum ERole {

    GERENTE("GERENTE"),
    MECANICO("MECANICO"),
    ASSISTENTE("ASSISTENTE");

    private String role;

    ERole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

}
