package org.vasya.task_planner.dto.task;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record TaskRequestDTO(

        @NotNull
        @Length(min = 0, max = 128)
        String title,

        @Nullable
        @Length(min=0, max =255)
        String description,

        boolean status)
{}