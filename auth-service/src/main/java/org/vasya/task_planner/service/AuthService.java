package org.vasya.task_planner.service;

import jakarta.servlet.http.HttpServletResponse;
import org.vasya.task_planner.dto.auth.UserDTO;
import org.vasya.task_planner.dto.auth.UserRegistrationRequestDTO;

public interface AuthService {
    UserDTO registration(UserRegistrationRequestDTO userRegistrationRequestDto, HttpServletResponse response);
}