package com.oficinaMenezes.backoficina.models.exceptions.auth;

public class UsuarioNaoCadastrado extends RuntimeException {
    public UsuarioNaoCadastrado(String message) {
        super(message);
    }
    public UsuarioNaoCadastrado(){super("Usuario não cadastrado");}
}
