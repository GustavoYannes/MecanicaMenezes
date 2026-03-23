package com.oficinaMenezes.backoficina.models.exceptions.auth;

public class UsuarioJaExisteException extends RuntimeException {
    public UsuarioJaExisteException() {
        super("Usuario já existe no sistema");
    }
    public UsuarioJaExisteException(String message) {super(message);}
}
