package com.oficinaMenezes.backoficina.models.dtos.entrada;

import com.oficinaMenezes.backoficina.models.validations.interfaces.CPF;

import jakarta.validation.constraints.NotBlank;

public record CreateEntradaDTO(
    
    @NotBlank(message = "A placa é obrigatória.")
    String placa, 
    String modelo,
    int ano,
    int km,
    String cor,

    @CPF
    @NotBlank(message = "O CPF é obrigatório.")
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
