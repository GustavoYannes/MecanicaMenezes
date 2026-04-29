package com.oficinaMenezes.backoficina.models.exceptions.entrada;

public class EntradaNaoFoiFechada extends RuntimeException {
    public EntradaNaoFoiFechada(String message) {
        super(message);
    }
    public EntradaNaoFoiFechada() {
        super("Entrada não foi Finalizada");
    }
}
