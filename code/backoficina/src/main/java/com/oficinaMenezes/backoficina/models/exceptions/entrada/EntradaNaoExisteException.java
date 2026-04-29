package com.oficinaMenezes.backoficina.models.exceptions.entrada;

public class EntradaNaoExisteException extends RuntimeException {
    public EntradaNaoExisteException(String message) {
        super(message);
    }
    public EntradaNaoExisteException(){super("Entrada inexistente");}
}
