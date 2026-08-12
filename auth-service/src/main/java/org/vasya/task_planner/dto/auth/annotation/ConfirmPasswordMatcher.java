package org.vasya.task_planner.dto.auth.annotation;

import jakarta.validation.ConstraintValidatorContext;
import lombok.NoArgsConstructor;
import org.vasya.task_planner.dto.auth.UserRegistrationRequestDTO;

import java.util.Objects;

@NoArgsConstructor
public class ConfirmPasswordMatcher implements jakarta.validation.ConstraintValidator<ConfirmPasswordMatch, UserRegistrationRequestDTO> {

    @Override
    public boolean isValid(UserRegistrationRequestDTO userRegistrationRequestDTO, ConstraintValidatorContext context) {

        String password = userRegistrationRequestDTO.password();
        String confirmPassword = userRegistrationRequestDTO.confirmPassword();

        boolean valid = Objects.equals(password, confirmPassword);

        if (!valid) {

            context.disableDefaultConstraintViolation();

            context.buildConstraintViolationWithTemplate(
                            context.getDefaultConstraintMessageTemplate()
                    )
                    .addPropertyNode("confirmPassword")
                    .addConstraintViolation();
        }

        return valid;
    }
}