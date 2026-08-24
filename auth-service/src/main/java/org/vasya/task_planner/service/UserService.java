package org.vasya.task_planner.service;

import org.vasya.task_planner.dto.auth.UserDTO;

public interface UserService {
    UserDTO create(String email, String password);
}