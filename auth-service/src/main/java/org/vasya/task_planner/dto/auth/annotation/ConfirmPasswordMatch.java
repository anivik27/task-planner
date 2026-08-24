package org.vasya.task_planner.dto.auth.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = ConfirmPasswordMatcher.class)
public @interface ConfirmPasswordMatch {

    String message() default "Confirm password does not match";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}