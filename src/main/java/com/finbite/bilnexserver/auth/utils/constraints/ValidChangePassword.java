package com.finbite.bilnexserver.auth.utils.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Constraint annotation for change password validation
 *
 * @author vinodjohn
 * @created 27.02.2025
 */
@Documented
@Target({ElementType.ANNOTATION_TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE})
@Constraint(validatedBy = {ChangePasswordValidator.class})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidChangePassword {
    String message() default "{messages.constraints.invalid-data}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
