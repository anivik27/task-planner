package org.vasya.task_planner.service.impl;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vasya.task_planner.dto.auth.UserRegistrationRequestDTO;
import org.vasya.task_planner.dto.auth.UserDTO;
import org.vasya.task_planner.security.authentication.jwt.JWTCookieHelper;
import org.vasya.task_planner.security.authentication.jwt.JWTUtil;
import org.vasya.task_planner.service.AuthService;
import org.vasya.task_planner.service.UserService;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JWTUtil jwtUtil;
    private final UserService userService;
    private final JWTCookieHelper cookieHelper;

    @Transactional
    public UserDTO registration(UserRegistrationRequestDTO userRegistrationRequestDto, HttpServletResponse response) {

        log.info("registration");
        UserDTO savedUser = userService.create(userRegistrationRequestDto.email(), userRegistrationRequestDto.password());
        String jwt = jwtUtil.generateToken(savedUser.getId(), savedUser.getEmail(), savedUser.getRole());
        cookieHelper.setJWT(jwt, response);
        return savedUser;
    }
}