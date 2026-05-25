package com.oficinaMenezes.backoficina.models.exceptions.servico;

public class ServicoNaoExisteException extends RuntimeException {
    public ServicoNaoExisteException(String message) {
        super(message);
    }
    public ServicoNaoExisteException() {
        super("Serviço não existe no banco de dados.");
    }
}
