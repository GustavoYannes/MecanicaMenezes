package com.oficinaMenezes.backoficina.models.exceptions.entrada;

public class EntradaJaFinalizada extends RuntimeException {
    public EntradaJaFinalizada(String message) {
        super(message);
    }
    public EntradaJaFinalizada(){ super("Entrada ja finalizada"); }
}
