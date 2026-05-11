package com.oficinaMenezes.backoficina.models.exceptions.veiculo;

public class VeiculoNaoExisteException extends RuntimeException {
    public VeiculoNaoExisteException(String message) {
        super(message);
    }
    public VeiculoNaoExisteException() {super("Veiculo não existe no banco de dados.");}
}
