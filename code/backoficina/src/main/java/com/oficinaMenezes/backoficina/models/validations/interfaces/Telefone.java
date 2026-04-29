package com.oficinaMenezes.backoficina.models.validations.interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.auth0.jwt.interfaces.Payload;
import com.oficinaMenezes.backoficina.models.validations.rules.ValidadorTelefone;

import jakarta.validation.Constraint;

@Target(ElementType.FIELD)
@Constraint(validatedBy = ValidadorTelefone.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface Telefone {

    String message() default "Telefone invalido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    }
