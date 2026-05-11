package com.oficinaMenezes.backoficina.models.exceptions.cliente;

public class ClienteNaoExisteException extends RuntimeException {
    public ClienteNaoExisteException(String message) {
        super(message);
    }
    public ClienteNaoExisteException() {super("Cliente informado não existe");}
}
