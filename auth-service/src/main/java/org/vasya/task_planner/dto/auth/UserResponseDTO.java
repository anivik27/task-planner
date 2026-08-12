package org.vasya.task_planner.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record UserResponseDTO(
        @NotBlank(message = "Id should not be empty") long id,
        @NotBlank(message = "Name should not be empty") String email) {
}