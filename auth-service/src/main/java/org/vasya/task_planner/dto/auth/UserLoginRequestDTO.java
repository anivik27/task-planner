package org.vasya.task_planner.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserLoginRequestDTO(

        @Email(message = "Email should be correct")
        @NotBlank(message = "Email should not be empty")
        String email,

        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        @Size(
                min = 4,
                max = 12,
                message = "Password must be between 4 and 12 characters long"
        )
        @Pattern(
                regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-={}:;\"'<>,.?/]).*$",
                message = "Password must contain at least one uppercase letter, one lowercase letter," +
                        " one number, and one special character"
        )
        @NotBlank(
                message = "Password must be not empty."
        )
        String password) {
}