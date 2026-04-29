package com.oficinaMenezes.backoficina.models.exceptions.entrada;

public class VeiculoEmAtendimentoException extends RuntimeException {
    public VeiculoEmAtendimentoException() {
        super("Veículo já em atendimento");
    }
    public VeiculoEmAtendimentoException(String message) {super(message);}
}