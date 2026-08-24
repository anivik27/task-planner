package org.vasya.task_planner.dto.task;

import java.sql.Timestamp;

public record UserDTO(
        Long id,
        Long userId,
        String title,
        String description,
        Timestamp timeAdded,
        boolean status,
        Timestamp timeMarkedTrue
) {}