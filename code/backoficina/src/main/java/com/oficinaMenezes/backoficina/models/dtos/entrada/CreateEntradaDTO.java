package com.oficinaMenezes.backoficina.models.dtos.entrada;

import com.oficinaMenezes.backoficina.models.validations.interfaces.CPF;
import com.oficinaMenezes.backoficina.models.validations.interfaces.Telefone;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateEntradaDTO(
    
    @NotBlank(message = "A placa é obrigatória.")
    String placa, 
    @NotBlank(message = "O modelo é obrigatório.")
    String modelo,
    int ano,
    int km,
    @NotBlank(message = "A cor é obrigatória.")
    String cor,

    @CPF
    @NotBlank(message = "O CPF é obrigatório.")
    String cpf,
    @Telefone
    String telefone,
    @Email
    String email, 
    @Size(min = 2, max = 100, message = "O nome do cliente deve conter entre 2 e 100 caracteres.")
    String nomeCliente,

    @NotBlank(message = "A cidade é obrigatória.")
    String cidade,
    @NotBlank(message = "O estado é obrigatório.")
    String estado,
    int cep,
    @NotBlank(message = "O bairro é obrigatório.")
    String bairro,
    @NotBlank(message = "A rua é obrigatória.")
    String rua,
    @NotBlank(message = "O número é obrigatório.")
    @Size(min = 1, max = 10, message = "O número deve conter entre 1 e 10 caracteres.")
    String numero
) {
    
}
