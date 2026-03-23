package com.oficinaMenezes.backoficina.models.dtos.auth;

import com.oficinaMenezes.backoficina.models.validations.interfaces.CPF;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegistrarFuncionarioDTO(

        @CPF
        String cpf,

        @NotBlank(message = "A senha é obrigatória.")
        @Size(min = 6, max = 20, message = "A senha deve ter entre {min} e {max} caracteres.")
        String senha,

        @NotBlank(message = "O nome é obrigatório.")
        String nome,
        String email,
        String telefone
) {
}
