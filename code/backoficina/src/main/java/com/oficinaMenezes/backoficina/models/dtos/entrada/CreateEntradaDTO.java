package com.oficinaMenezes.backoficina.models.dtos.entrada;

public record CreateEntradaDTO(
    String placa, 
    String modelo,
    int ano,
    int km,
    String cor,

    String cpf,
    String telefone,
    String email, 
    String endereco,
    String nomeCliente,

    String cidade,
    String estado,
    int cep,
    String bairro,
    String rua,
    String numero
) {
    
}
