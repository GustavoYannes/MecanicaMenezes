package com.oficinaMenezes.backoficina.models.validations.rules;

import com.oficinaMenezes.backoficina.models.validations.interfaces.CPF;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidadorCPF implements ConstraintValidator<CPF,String> {


    @Override
    public void initialize(CPF constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String cpf, ConstraintValidatorContext constraintValidatorContext) {
        if (cpf == null || cpf.length() != 11 || cpf.matches("(\\d)\\1{10}"))
            return false;

        try {
            int soma = 0, peso = 10;

            for (int i = 0; i < 9; i++)
                soma += Character.getNumericValue(cpf.charAt(i)) * peso--;

            int digito1 = 11 - (soma % 11);
            if (digito1 >= 10) digito1 = 0;

            soma = 0;
            peso = 11;

            for (int i = 0; i < 9; i++)
                soma += Character.getNumericValue(cpf.charAt(i)) * peso--;

            soma += digito1 * peso;

            int digito2 = 11 - (soma % 11);
            if (digito2 >= 10) digito2 = 0;

            return digito1 == Character.getNumericValue(cpf.charAt(9)) &&
                    digito2 == Character.getNumericValue(cpf.charAt(10));
        } catch (NumberFormatException e) {
            return false;
        }
    }
}