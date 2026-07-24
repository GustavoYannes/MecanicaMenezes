package com.oficinaMenezes.backoficina.models.exceptions.dashboard;

public class PeriodoDashboardInvalidoException extends RuntimeException {

    public PeriodoDashboardInvalidoException() {
        super("A data de inicio do dashboard nao pode ser posterior a data fim");
    }
}
