package com.oficinaMenezes.backoficina.models.validations.interfaces;


import com.oficinaMenezes.backoficina.models.validations.rules.ValidadorCPF;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Constraint(validatedBy = ValidadorCPF.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface CPF {

    String message() default "CPF invalido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}