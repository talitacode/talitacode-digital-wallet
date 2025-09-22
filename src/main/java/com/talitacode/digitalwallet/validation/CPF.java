package com.talitacode.digitalwallet.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SimpleCpfValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface CPF {
    String message() default "Invalid CPF: must contain exactly 11 digits";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
