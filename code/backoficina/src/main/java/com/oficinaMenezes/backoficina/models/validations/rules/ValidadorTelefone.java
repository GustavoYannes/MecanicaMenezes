package com.oficinaMenezes.backoficina.models.validations.rules;

import com.oficinaMenezes.backoficina.models.validations.interfaces.Telefone;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidadorTelefone implements ConstraintValidator<Telefone, String> {

    @Override
    public void initialize(Telefone constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String telefone, ConstraintValidatorContext context) {

        if (telefone == null || telefone.isBlank())
            return false;

        // Remove qualquer coisa que não seja número ((), -, espaço)
        telefone = telefone.replaceAll("\\D", "");

        // Valida tamanho (10 ou 11 dígitos: fixo ou celular)
        if (telefone.length() < 10 || telefone.length() > 11)
            return false;

        // Evita números repetidos (ex: 11111111111)
        if (telefone.matches("(\\d)\\1{9,10}"))
            return false;

        return true;
    }
}