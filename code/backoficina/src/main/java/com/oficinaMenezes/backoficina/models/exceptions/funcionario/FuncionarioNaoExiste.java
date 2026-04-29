package com.oficinaMenezes.backoficina.models.exceptions.funcionario;

public class FuncionarioNaoExiste extends RuntimeException {
    public FuncionarioNaoExiste(String message) {
        super(message);
    }
    public FuncionarioNaoExiste(){super("Funcionario não existe no banco");}
}
