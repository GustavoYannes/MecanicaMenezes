package com.oficinaMenezes.backoficina.models.dtos.auth;

import com.oficinaMenezes.backoficina.models.validations.interfaces.CPF;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthenticationDTO(
        @CPF
        String cpf,

        @NotBlank(message = "A senha é obrigatória.")
        @Size(min = 6, max = 20, message = "A senha deve ter entre {min} e {max} caracteres.")
        String senha
) {
}
